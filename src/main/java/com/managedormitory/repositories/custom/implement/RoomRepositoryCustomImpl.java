package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.room.*;
import com.managedormitory.repositories.custom.RoomRepositoryCustom;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LocalDateType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<RoomDto> getAllRoomByTime() {
        LocalDate currentDate = LocalDate.now();
        String queryRoom =
                "SELECT r.id AS id,\n" +
                        "       r.name             AS name,\n" +
                        "       r.quantity_student AS quantityStudent,\n" +
                        "       c.name             AS campusName,\n" +
                        "       tr.name            AS typeRoomName,\n" +
                        "       u.full_name        AS userManager,\n" +
                        "       s.water_price_id   AS waterPriceId\n" +
                        "FROM room r\n" +
                        "         join campus c on c.id = r.campus_id\n" +
                        "         left join users u on u.id = c.user_id\n" +
                        "         left join type_room tr on tr.id = r.type_room_id\n" +
                        "         left join student s on r.id = s.room_id\n" +
                        "         left join water_bill wb on s.id = wb.student_id\n" +
                        "         left join vehicle v on s.id = v.student_id\n" +
                        "         left join vehicle_bill vb on v.id = vb.vehicle_id\n" +
                        "         left join detail_room dr on s.id = dr.student_id\n" +
                        "where (dr.end_date >= :currentDate\n" +
                        "    and wb.end_date >= :currentDate\n" +
                        "    and vb.end_date >= :currentDate)\n" +
                        "   or (dr.end_date >= :currentDate\n" +
                        "    and wb.end_date >= :currentDate and vb.end_date is null)\n" +
                        "group by r.id, r.name, r.quantity_student, c.name, tr.name, s.water_price_id, u.full_name\n" +
                        "order by r.id asc;";

        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryRoom);
        query.setParameter("currentDate", new TypedParameterValue(LocalDateType.INSTANCE, currentDate))
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("quantityStudent", StandardBasicTypes.INTEGER)
                .addScalar("typeRoomName", StandardBasicTypes.STRING)
                .addScalar("campusName", StandardBasicTypes.STRING)
                .addScalar("userManager", StandardBasicTypes.STRING)
                .addScalar("waterPriceId", StandardBasicTypes.INTEGER);
        query.setResultTransformer(new AliasToBeanResultTransformer(RoomDto.class));

        return safeList(query);
    }

    @Override
    public int updateQuantityStudent(Integer roomId) {
        String queryNative =
                "UPDATE room r\n" +
                        "SET quantity_student = (select count(s.id)\n" +
                        "from room r\n" +
                        "         join student s on r.id = s.room_id\n" +
                        "         left join student_left sl on s.id = sl.id\n" +
                        "where r.id = :roomId\n" +
                        "  and s.id not in (select sl.id from student_left sl))\n" +
                        "where r.id = :roomId";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryNative);
        query.setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, roomId));
        return query.executeUpdate();
    }

    @Override
    public int updateTypeRoom(Integer roomId, DetailRoomDto room) {
        Integer typeRoomId = room.getTypeRoom().getId();
        String queryNative =
                "UPDATE room\n" +
                        "SET type_room_id = " +
                        "CASE\n" +
                        "WHEN (quantity_student = 0) THEN null\n" +
                        "WHEN (quantity_student > 0) THEN :typeRoomId\n" +
                        "END\n" +
                        "WHERE id = :roomId";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryNative);
        query.setParameter("typeRoomId", new TypedParameterValue(IntegerType.INSTANCE, typeRoomId))
                .setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, roomId));
        return query.executeUpdate();
    }

    @Override
    public Optional<InfoMoney> getInfoLatestBillForNewStudent(Integer roomId) {
        String queryGet =
                "with water_price as (select s.id, p.price, s.water_price_id\n" +
                        "                     from student s\n" +
                        "                              join price_list p on p.id = s.water_price_id)\n" +
                        "select r.id                as roomId,\n" +
                        "       r.name              as roomName,\n" +
                        "       MAX(dr.end_date)    as maxDateRoomBill,\n" +
                        "       MAX(wb.end_date)    as maxDateWaterBill,\n" +
                        "       MAX(vb.end_date)    as maxDateVehicleBill,\n" +
                        "       pl.price            as roomPrice,\n" +
                        "       wp.water_price_id   as waterPriceId,\n" +
                        "       wp.price            as waterPrice,\n" +
                        "       tr.max_quantity     as maxQuantityStudent\n"+
                        "from room r\n" +
                        "         left join student s on r.id = s.room_id\n" +
                        "         left join student_left sl on s.id = sl.id\n" +
                        "         left join type_room tr on tr.id = r.type_room_id\n" +
                        "         left join detail_room dr on dr.student_id = s.id\n" +
                        "         left join price_list pl on r.price_list_id = pl.id\n" +
                        "         left join water_bill wb on s.id = wb.student_id\n" +
                        "         left join vehicle v on s.id = v.student_id\n" +
                        "         left join vehicle_left vl on v.id = vl.id\n" +
                        "         left join vehicle_bill vb on v.id = vb.vehicle_id\n" +
                        "         left join water_price wp on s.id = wp.id\n" +
                        "where r.id = :roomId\n" +
                        "  and sl.id is null\n" +
                        "  and vl.id is null\n" +
                        "group by r.id, roomPrice, waterPriceId, waterPrice,maxQuantityStudent";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryGet);
        query.setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, roomId))
                .addScalar("roomId", StandardBasicTypes.INTEGER)
                .addScalar("roomName", StandardBasicTypes.STRING)
                .addScalar("maxDateRoomBill", StandardBasicTypes.DATE)
                .addScalar("maxDateWaterBill", StandardBasicTypes.DATE)
                .addScalar("maxDateVehicleBill", StandardBasicTypes.DATE)
                .addScalar("waterPriceId", StandardBasicTypes.INTEGER)
                .addScalar("roomPrice", StandardBasicTypes.FLOAT)
                .addScalar("waterPrice", StandardBasicTypes.FLOAT)
                .addScalar("maxQuantityStudent", StandardBasicTypes.INTEGER);
        query.setResultTransformer(new AliasToBeanResultTransformer(InfoMoney.class));

        return safeObject(query);
    }

    @Override
    public Optional<RoomBillDto> getDetailRoomRecently(Integer id) {
        String queryMaxDate = "with max_date as (\n" +
                "    select MAX(dr.end_date) max_date\n" +
                "    from detail_room dr\n" +
                "    where dr.student_id = :id)\n" +
                "select dr.id         as billId,\n" +
                "       r.name as roomName,\n" +
                "       s.name        as studentName,\n" +
                "       dr.student_id as studentId,\n" +
                "       dr.start_date as startDate,\n" +
                "       dr.end_date   as endDate,\n" +
                "       pl.price      as price,\n" +
                "       r.id          as roomId,\n" +
                "       tr.max_quantity as maxQuantity\n" +
                "from detail_room dr\n" +
                "         join max_date md on dr.end_date = md.max_date\n" +
                "         join student s on dr.student_id = s.id\n" +
                "         join room r on r.id = s.room_id\n" +
                "         join price_list pl on pl.id = r.price_list_id\n" +
                "         left join type_room tr on r.type_room_id = tr.id\n" +
                "where dr.student_id = :id";
        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryMaxDate);
        query.setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, id))
                .addScalar("billId", StandardBasicTypes.INTEGER)
                .addScalar("roomName", StandardBasicTypes.STRING)
                .addScalar("studentName", StandardBasicTypes.STRING)
                .addScalar("studentId", StandardBasicTypes.INTEGER)
                .addScalar("startDate", StandardBasicTypes.DATE)
                .addScalar("endDate", StandardBasicTypes.DATE)
                .addScalar("price", StandardBasicTypes.FLOAT)
                .addScalar("roomId", StandardBasicTypes.INTEGER)
                .addScalar("maxQuantity", StandardBasicTypes.INTEGER);
        query.setResultTransformer(new AliasToBeanResultTransformer(RoomBillDto.class));

        return safeObject(query);
    }

    public static <T> Optional<T> safeObject(Query query) {
        return query.getResultStream().findFirst();
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
