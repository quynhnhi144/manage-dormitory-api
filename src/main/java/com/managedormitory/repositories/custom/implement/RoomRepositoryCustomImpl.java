package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.dto.room.RoomDto;
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
                        "v.vehicle_price_id AS vehiclePriceId,\n" +
                        "dr.month AS month,\n" +
                        "dr.year AS year\n" +
                        "FROM room r\n" +
                        "join campus c on c.id = r.campus_id\n" +
                        "join users u on u.user_id = c.user_id\n" +
                        "left join type_room tr on tr.id = r.type_room_id\n" +
                        "left join student s on r.id = s.room_id\n" +
                        "left join water_bill wb on s.id = wb.student_id\n" +
                        "left join vehicle v on s.id = v.student_id\n" +
                        "left join vehicle_bill vb on v.id = vb.vehicle_id\n" +
                        "left join detail_room dr on s.id = dr.student_id\n" +
                        "where month = :month and year = :year\n" +
                        "  and wb.end_date >= :currentDate\n" +
                        "  and vb.end_date >= :currentDate\n" +
                        "\n" +
                        "group by r.id, r.name, r.quantity_student, c.name, tr.name, u.full_name, s.water_price_id, v.vehicle_price_id, dr.month, dr.year\n" +
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
                "UPDATE room\n" +
                        "SET quantity_student = (select count(s.id)\n" +
                        "                        from student s\n" +
                        "                                 join room r on r.id = s.room_id\n" +
                        "                        where r.id = :roomId)\n" +
                        "WHERE id = :roomId";
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

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
