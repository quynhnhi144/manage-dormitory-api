package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.StudentDetailDto;
import com.managedormitory.repositories.custom.StudentRepositoryCustom;
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
public class StudentRepositoryImplCustom implements StudentRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<StudentDetailDto> getAllStudentByTime() {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();
        String queryStudent =
                "SELECT s.id AS id,\n" +
                        "s.name AS name,\n" +
                        "s.birthday AS birthday,\n" +
                        "s.phone AS phone,\n" +
                        "s.email AS email,\n" +
                        "s.address AS address,\n" +
                        "s.starting_date_of_stay AS startingDateOfStay,\n" +
                        "s.ending_date_of_stay AS endingDateOfStay,\n" +
                        "r.name AS roomName,\n" +
                        "c.name AS campusName,\n" +
                        "u.full_name AS userManager,\n" +
                        "dr.is_pay AS isPayRoom,\n" +
                        "wb.is_pay AS isPayWaterBill,\n" +
                        "vb.is_pay AS isPayVehicleBill,\n" +
                        "pb.is_pay AS isPayPowerBill\n" +
                        "from student s\n" +
                        QueryUtil.JOIN_ROOM +
                        QueryUtil.JOIN_CAMPUS +
                        QueryUtil.JOIN_USERS +
                        QueryUtil.LEFTJOIN_TYPEROOM +
                        QueryUtil.LEFTJOIN_DETAILROOM +
                        QueryUtil.LEFTJOIN_WATERBILL +
                        QueryUtil.LEFTJOIN_VEHICLE +
                        QueryUtil.LEFTJOIN_VEHICLEBILL +
                        QueryUtil.LEFTJOIN_POWERBILL +
                        "where dr.month = :month\n" +
                        "and dr.year = :year\n" +
                        "and wb.end_date >= :currentDate\n" +
                        "and vb.end_date >= :currentDate\n" +
                        "group by s.id, s.name, r.id, s.birthday, s.phone, s.email, s.address, s.starting_date_of_stay, s.ending_date_of_stay, r.name, c.name, u.full_name, dr.is_pay, wb.is_pay, vb.is_pay, pb.is_pay\n" +
                        "order by s.id asc";

        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryStudent);
        query.setParameter("month", new TypedParameterValue(IntegerType.INSTANCE, month))
                .setParameter("year", new TypedParameterValue(IntegerType.INSTANCE, year))
                .setParameter("currentDate", new TypedParameterValue(LocalDateType.INSTANCE, currentDate))
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("birthday", StandardBasicTypes.DATE)
                .addScalar("phone", StandardBasicTypes.STRING)
                .addScalar("email", StandardBasicTypes.STRING)
                .addScalar("address", StandardBasicTypes.STRING)
                .addScalar("startingDateOfStay", StandardBasicTypes.DATE)
                .addScalar("endingDateOfStay", StandardBasicTypes.DATE)
                .addScalar("roomName", StandardBasicTypes.STRING)
                .addScalar("campusName", StandardBasicTypes.STRING)
                .addScalar("userManager", StandardBasicTypes.STRING)
                .addScalar("isPayRoom", StandardBasicTypes.BOOLEAN)
                .addScalar("isPayWaterBill", StandardBasicTypes.BOOLEAN)
                .addScalar("isPayVehicleBill", StandardBasicTypes.BOOLEAN)
                .addScalar("isPayPowerBill", StandardBasicTypes.BOOLEAN);

        query.setResultTransformer(new AliasToBeanResultTransformer(StudentDetailDto.class));
        return safeList(query);
    }

    @Override
    public int updateRoomIdOfStudent(Integer studentId, Integer roomId) {
        String queryUpdate =
                "UPDATE student \n" +
                        "SET room_id = :roomId\n" +
                        "WHERE id = :studentId";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryUpdate);
        query.setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, roomId))
                .setParameter("studentId", new TypedParameterValue(IntegerType.INSTANCE, studentId));
        int result = query.executeUpdate();

        return result;
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
