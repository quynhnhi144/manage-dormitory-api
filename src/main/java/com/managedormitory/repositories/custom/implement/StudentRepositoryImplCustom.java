package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentUpdateDto;
import com.managedormitory.repositories.custom.StudentRepositoryCustom;
import com.managedormitory.utils.QueryUtil;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.*;
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
    public List<StudentDto> getAllStudentByTime() {
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
                        "r.id AS roomId,\n" +
                        "dr.is_pay AS isPayRoom,\n" +
                        "wb.is_pay AS isPayWaterBill,\n" +
                        "vb.is_pay AS isPayVehicleBill,\n" +
                        "pb.is_pay AS isPayPowerBill\n" +
                        "from student s\n" +
                        QueryUtil.JOIN_ROOM +
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
                        "group by s.id, s.name, r.id, s.birthday, s.phone, s.email, s.address, s.starting_date_of_stay, s.ending_date_of_stay, dr.is_pay, wb.is_pay, vb.is_pay, pb.is_pay\n" +
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
                .addScalar("roomId", StandardBasicTypes.INTEGER)
                .addScalar("isPayRoom", StandardBasicTypes.BOOLEAN)
                .addScalar("isPayWaterBill", StandardBasicTypes.BOOLEAN)
                .addScalar("isPayVehicleBill", StandardBasicTypes.BOOLEAN)
                .addScalar("isPayPowerBill", StandardBasicTypes.BOOLEAN);

        query.setResultTransformer(new AliasToBeanResultTransformer(StudentDto.class));
        return safeList(query);
    }

    @Override
    public int updateStudent(Integer studentId, StudentUpdateDto studentUpdateDto) {
        System.out.println("studentUpdate: " + studentUpdateDto);
        String queryUpdateStudent = "UPDATE student\n" +
                "SET name = :name, birthday = :birthday, address = :address, phone = :phone, email = :email, starting_date_of_stay = :startingDateOfStay, ending_date_of_stay = :endingDateOfStay, room_id = :roomId\n" +
                "WHERE id = :studentId";

        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryUpdateStudent);
        query.setParameter("name", new TypedParameterValue(StringType.INSTANCE, studentUpdateDto.getName()))
                .setParameter("birthday", new TypedParameterValue(DateType.INSTANCE, studentUpdateDto.getBirthday()))
                .setParameter("address", new TypedParameterValue(StringType.INSTANCE, studentUpdateDto.getAddress()))
                .setParameter("phone", new TypedParameterValue(StringType.INSTANCE, studentUpdateDto.getPhone()))
                .setParameter("email", new TypedParameterValue(StringType.INSTANCE, studentUpdateDto.getEmail()))
                .setParameter("startingDateOfStay", new TypedParameterValue(DateType.INSTANCE, studentUpdateDto.getStartingDateOfStay()))
                .setParameter("endingDateOfStay", new TypedParameterValue(DateType.INSTANCE, studentUpdateDto.getEndingDateOfStay()))
                .setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, studentUpdateDto.getRoomId()))
                .setParameter("studentId", new TypedParameterValue(IntegerType.INSTANCE, studentUpdateDto.getId()));

        return query.executeUpdate();
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
