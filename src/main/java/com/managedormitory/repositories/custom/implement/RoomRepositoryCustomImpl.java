package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.room.RoomDto;
import com.managedormitory.models.dto.room.RoomPriceAndWaterPrice;
import com.managedormitory.repositories.custom.RoomRepositoryCustom;
import com.managedormitory.utils.QueryUtil;
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
import java.util.function.Function;

@Component
public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<RoomDto> getAllRoomByTime() {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();
        String queryRoom =
                "SELECT r.id AS id,\n" +
                        "r.name AS name,\n" +
                        "r.quantity_student AS quantityStudent,\n" +
                        "c.name AS campusName,\n" +
                        "tr.name AS typeRoomName,\n" +
                        "u.full_name AS userManager,\n" +
                        "s.water_price_id AS waterPriceId,\n" +
                        "v.vehicle_price_id AS vehiclePriceId\n" +
                        "FROM room r\n" +
                        "join campus c on c.id = r.campus_id\n" +
                        "join users u on u.id = c.user_id\n" +
                        "left join type_room tr on tr.id = r.type_room_id\n" +
                        "left join student s on r.id = s.room_id\n" +
                        "left join water_bill wb on s.id = wb.student_id\n" +
                        "left join vehicle v on s.id = v.student_id\n" +
                        "left join vehicle_bill vb on v.id = vb.vehicle_id\n" +
                        "left join detail_room dr on s.id = dr.student_id\n" +
                        "where extract(month from dr.end_date) = :month and extract(year from dr.end_date) = :year and dr.end_date >= :currentDate\n" +
                        "  and extract(month from wb.end_date) = :month and extract(year from wb.end_date) = :year and wb.end_date >= :currentDate\n" +
                        "  and extract(month from vb.end_date) = :month and extract(year from vb.end_date) = :year and vb.end_date >= :currentDate\n" +
                        "group by r.id, r.name, r.quantity_student, c.name, tr.name, u.full_name, s.water_price_id, v.vehicle_price_id\n" +
                        "order by r.id asc";

        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryRoom);
        query.setParameter("month", new TypedParameterValue(IntegerType.INSTANCE, month))
                .setParameter("year", new TypedParameterValue(IntegerType.INSTANCE, year))
                .setParameter("currentDate", new TypedParameterValue(LocalDateType.INSTANCE, currentDate))
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("quantityStudent", StandardBasicTypes.INTEGER)
                .addScalar("typeRoomName", StandardBasicTypes.STRING)
                .addScalar("campusName", StandardBasicTypes.STRING)
                .addScalar("userManager", StandardBasicTypes.STRING)
                .addScalar("waterPriceId", StandardBasicTypes.INTEGER)
                .addScalar("vehiclePriceId", StandardBasicTypes.INTEGER);
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
    public Optional<RoomPriceAndWaterPrice> getRoomPriceAndWaterPrice(Integer roomId) {
        String queryRoomPriceAndWaterPrice =
                "with water_price as (select s.id, p.price, s.water_price_id\n" +
                        "                     from student s\n" +
                        "                              join price_list p on p.id = s.water_price_id\n" +
                        "),\n" +
                        "     vehicle_price as (select s2.id, pl2.price, v2.vehicle_price_id\n" +
                        "                       from student s2\n" +
                        "                                left join vehicle v2 on s2.id = v2.student_id\n" +
                        "                                left join price_list pl2 on v2.vehicle_price_id = pl2.id\n" +
                        "                                left join vehicle_bill vb on v2.id = vb.vehicle_id\n" +
                        "     )\n" +
                        "\n" +
                        "select distinct r.id                as roomId,\n" +
                        "                r.name              as roomName,\n" +
                        "                tr.max_quantity     as maxQuantityStudent,\n" +
                        "                MAX(dr.end_date)    as maxDateRoomBill,\n" +
                        "                MAX(wb.end_date)    as maxDateWaterBill,\n" +
                        "                MAX(b.end_date)     as maxDateVehicleBill,\n" +
                        "                pl.price            as roomPrice,\n" +
                        "                wp.price            as waterPrice,\n" +
                        "                vp.price            as vehiclePrice,\n" +
                        "                wp.water_price_id   as waterPriceId,\n" +
                        "                vp.vehicle_price_id as vehicleId\n" +
                        "from room r\n" +
                        "         left join student s on r.id = s.room_id\n" +
                        "         left join detail_room dr on s.id = dr.student_id\n" +
                        "         left join water_bill wb on s.id = wb.student_id\n" +
                        "         left join price_list pl on pl.id = r.price_list_id\n" +
                        "         left join water_price wp on s.id = wp.id\n" +
                        "         left join type_room tr on tr.id = r.type_room_id\n" +
                        "         left join vehicle v on v.id = s.id\n" +
                        "         left join vehicle_bill b on v.id = b.vehicle_id\n" +
                        "         left join vehicle_price vp on v.id = vp.id\n" +
                        "where (r.id = :roomId\n" +
                        "  and b.end_date is not null) or r.id = :roomId\n" +
                        "group by r.id,\n" +
                        "         s.id,\n" +
                        "         tr.max_quantity,\n" +
                        "         pl.price,\n" +
                        "         wp.price,\n" +
                        "         vp.price,\n" +
                        "         wp.water_price_id,\n" +
                        "         vp.vehicle_price_id";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryRoomPriceAndWaterPrice);
        query.setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, roomId))
                .addScalar("roomId", StandardBasicTypes.INTEGER)
                .addScalar("roomName", StandardBasicTypes.STRING)
                .addScalar("maxDateRoomBill", StandardBasicTypes.DATE)
                .addScalar("maxDateWaterBill", StandardBasicTypes.DATE)
                .addScalar("maxDateVehicleBill", StandardBasicTypes.DATE)
                .addScalar("roomPrice", StandardBasicTypes.FLOAT)
                .addScalar("waterPrice", StandardBasicTypes.FLOAT)
                .addScalar("vehicleId", StandardBasicTypes.INTEGER)
                .addScalar("vehiclePrice", StandardBasicTypes.FLOAT)
                .addScalar("maxQuantityStudent", StandardBasicTypes.INTEGER)
                .addScalar("waterPriceId", StandardBasicTypes.INTEGER);
        query.setResultTransformer(new AliasToBeanResultTransformer(RoomPriceAndWaterPrice.class));

        return safeObject(query);
    }

    @Override
    public Optional<RoomBillDto> getDetailRoomRecently(Integer id) {
        String queryMaxDate = "with max_date as (\n" +
                "    select MAX(dr.end_date) max_date\n" +
                "    from detail_room dr\n" +
                "    where dr.student_id = :id)\n" +
                "select dr.id         as billId,\n" +
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
