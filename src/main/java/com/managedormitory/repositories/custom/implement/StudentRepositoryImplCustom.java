package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.repositories.custom.StudentRepositoryCustom;
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
                        "r.name AS roomName,\n" +
                        "c.name AS campusName,\n" +
                        "r.id AS roomId,\n" +
                        "u.full_name AS userManager\n" +
                        "from student s\n" +
                        "         join room r on r.id = s.room_id\n" +
                        "         join campus c on r.campus_id = c.id\n" +
                        "         left join type_room tr on r.type_room_id = tr.id\n" +
                        "         join users u on u.user_id = c.user_id\n" +
                        "         join detail_room dr on s.id = dr.student_id\n" +
                        "         join water_bill wb on s.id = wb.student_id\n" +
                        "         join vehicle v on s.id = v.student_id\n" +
                        "         join vehicle_bill vb on v.id = vb.vehicle_id\n" +
                        "\n" +
                        "where dr.month = :month\n" +
                        "  and dr.year = :year\n" +
                        "  and wb.end_date >= :currentDate\n" +
                        "  and vb.end_date >= :currentDate\n" +
                        "\n" +
                        "group by s.id,\n" +
                        "         s.name,\n" +
                        "         r.id,\n" +
                        "         s.birthday,\n" +
                        "         s.phone,\n" +
                        "         s.email,\n" +
                        "         s.address,\n" +
                        "         s.starting_date_of_stay,\n" +
                        "         s.ending_date_of_stay,\n" +
                        "         r.name,\n" +
                        "         c.name,\n" +
                        "         u.full_name\n" +
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
                .addScalar("roomId", StandardBasicTypes.INTEGER);

        query.setResultTransformer(new AliasToBeanResultTransformer(StudentDto.class));
        return safeList(query);
    }

    @Override
    public int updateStudent(Integer studentId, StudentDto studentDto) {
        System.out.println("studentUpdate: " + studentDto);
        String queryUpdateStudent = "UPDATE student\n" +
                "SET name = :name, birthday = :birthday, address = :address, phone = :phone, email = :email, starting_date_of_stay = :startingDateOfStay, ending_date_of_stay = :endingDateOfStay, room_id = :roomId\n" +
                "WHERE id = :studentId";

        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryUpdateStudent);
        query.setParameter("name", new TypedParameterValue(StringType.INSTANCE, studentDto.getName()))
                .setParameter("birthday", new TypedParameterValue(DateType.INSTANCE, studentDto.getBirthday()))
                .setParameter("address", new TypedParameterValue(StringType.INSTANCE, studentDto.getAddress()))
                .setParameter("phone", new TypedParameterValue(StringType.INSTANCE, studentDto.getPhone()))
                .setParameter("email", new TypedParameterValue(StringType.INSTANCE, studentDto.getEmail()))
                .setParameter("startingDateOfStay", new TypedParameterValue(DateType.INSTANCE, studentDto.getStartingDateOfStay()))
                .setParameter("endingDateOfStay", new TypedParameterValue(DateType.INSTANCE, studentDto.getEndingDateOfStay()))
                .setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, studentDto.getRoomId()))
                .setParameter("studentId", new TypedParameterValue(IntegerType.INSTANCE, studentDto.getId()));

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
