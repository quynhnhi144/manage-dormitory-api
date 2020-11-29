package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.vehicle.VehicleBillDto;
import com.managedormitory.models.dto.WaterBillDto;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentMoveDto;
import com.managedormitory.repositories.custom.StudentRepositoryCustom;
import com.managedormitory.utils.DateUtil;
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
import java.util.Optional;

@Component
public class StudentRepositoryImplCustom implements StudentRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<StudentDto> getAllStudentByTime() {
        LocalDate currentDate = LocalDate.now();
        String queryStudent =
                "SELECT MAX(wb.create_date),\n" +
                        "s.id AS id,\n" +
                        "s.id_card AS idCard,\n" +
                        "s.name AS name,\n" +
                        "s.birthday AS birthday,\n" +
                        "s.phone AS phone,\n" +
                        "s.email AS email,\n" +
                        "s.address AS address,\n" +
                        "s.starting_date_of_stay AS startingDateOfStay,\n" +
                        "s.ending_date_of_stay AS endingDateOfStay,\n" +
                        "r.id AS roomId,\n" +
                        "r.name AS roomName,\n" +
                        "c.name AS campusName,\n" +
                        "u.full_name AS userManager,\n" +
                        "s.water_price_id AS waterPriceId,\n" +
                        "v.student_id AS vehicleId\n" +
                        "from student s\n" +
                        "         left join room r on r.id = s.room_id\n" +
                        "         join campus c on r.campus_id = c.id\n" +
                        "         left join type_room tr on r.type_room_id = tr.id\n" +
                        "         join users u on u.id = c.user_id\n" +
                        "         join detail_room dr on s.id = dr.student_id\n" +
                        "         join water_bill wb on s.id = wb.student_id\n" +
                        "         left join vehicle v on v.student_id = s.id\n" +
                        "where dr.end_date >= :currentDate\n" +
                        "  and wb.end_date >= :currentDate\n" +
                        "group by s.id, r.id, c.name, u.full_name, v.student_id\n" +
                        "order by s.id asc;";

        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryStudent);
        query.setParameter("currentDate", new TypedParameterValue(LocalDateType.INSTANCE, currentDate))
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("idCard", StandardBasicTypes.STRING)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("birthday", StandardBasicTypes.DATE)
                .addScalar("phone", StandardBasicTypes.STRING)
                .addScalar("email", StandardBasicTypes.STRING)
                .addScalar("address", StandardBasicTypes.STRING)
                .addScalar("startingDateOfStay", StandardBasicTypes.DATE)
                .addScalar("endingDateOfStay", StandardBasicTypes.DATE)
                .addScalar("roomId", StandardBasicTypes.INTEGER)
                .addScalar("waterPriceId", StandardBasicTypes.INTEGER)
                .addScalar("vehicleId", StandardBasicTypes.INTEGER);

        query.setResultTransformer(new AliasToBeanResultTransformer(StudentDto.class));
        return safeList(query);
    }

    @Override
    public int updateStudent(Integer studentId, StudentDto studentDto) {
        String queryUpdateStudent = "UPDATE student\n" +
                "SET id_card = :idCard, name = :name, birthday = :birthday, address = :address, phone = :phone, email = :email, starting_date_of_stay = :startingDateOfStay, ending_date_of_stay = :endingDateOfStay, room_id = :roomId\n" +
                "WHERE id = :studentId";

        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryUpdateStudent);
        query.setParameter("idCard", new TypedParameterValue(StringType.INSTANCE, studentDto.getIdCard()))
                .setParameter("name", new TypedParameterValue(StringType.INSTANCE, studentDto.getName()))
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
    public int updateRoomIdForStudent(Integer studentId, Integer newRoomId) {
        String queryUpdateStudent = "UPDATE student\n" +
                "SET room_id = :newRoomId\n" +
                "WHERE id = :studentId";
        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryUpdateStudent);
        query.setParameter("studentId", new TypedParameterValue(IntegerType.INSTANCE, studentId))
                .setParameter("newRoomId", new TypedParameterValue(IntegerType.INSTANCE, newRoomId));
        return query.executeUpdate();
    }

    @Override
    public Optional<WaterBillDto> getWaterBillRecently(Integer id) {
        String queryWaterBillMaxDate =
                "with max_date as (\n" +
                        "    select MAX(wb.end_date) max_date_water_bill\n" +
                        "    from water_bill wb\n" +
                        "    where wb.student_id = :id)\n" +
                        "select wb.bill_id    as billId,\n" +
                        "       s.name        as studentName,\n" +
                        "       wb.student_id as studentId,\n" +
                        "       wb.start_date as startDate,\n" +
                        "       wb.end_date   as endDate,\n" +
                        "       pl.price      as price,\n" +
                        "       r.id          as roomId\n" +
                        "from water_bill wb\n" +
                        "         join max_date md on wb.end_date = md.max_date_water_bill\n" +
                        "         join student s on wb.student_id = s.id\n" +
                        "         join room r on r.id = s.room_id\n" +
                        "         join price_list pl on pl.id = s.water_price_id\n" +
                        "where wb.student_id = :id";
        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryWaterBillMaxDate);
        query.setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, id))
                .addScalar("billId", StandardBasicTypes.INTEGER)
                .addScalar("studentName", StandardBasicTypes.STRING)
                .addScalar("studentId", StandardBasicTypes.INTEGER)
                .addScalar("startDate", StandardBasicTypes.DATE)
                .addScalar("endDate", StandardBasicTypes.DATE)
                .addScalar("price", StandardBasicTypes.FLOAT)
                .addScalar("roomId", StandardBasicTypes.INTEGER);
        query.setResultTransformer(new AliasToBeanResultTransformer(WaterBillDto.class));

        return safeObject(query);
    }

    @Override
    public int addStudentLeft(StudentMoveDto studentMoveDto) {
        String queryAdd = "INSERT INTO student_left(id, leaving_date, number_of_give_of_take_room_money, number_of_give_of_take_water_money, number_of_give_of_take_vehicle_money)\n" +
                "VALUES(:id, :leavingDate, :numberOfGiveOfTakeRoomMoney, :numberOfGiveOfTakeWaterMoney, :numberOfGiveOfTakeVehicleMoney)";
        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryAdd);
        query.setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, studentMoveDto.getId()))
                .setParameter("leavingDate", new TypedParameterValue(DateType.INSTANCE, DateUtil.getSDateFromLDate(studentMoveDto.getLeavingDate())))
                .setParameter("numberOfGiveOfTakeRoomMoney", new TypedParameterValue(FloatType.INSTANCE, studentMoveDto.getNumberOfRoomMoney()))
                .setParameter("numberOfGiveOfTakeWaterMoney", new TypedParameterValue(FloatType.INSTANCE, studentMoveDto.getNumberOfWaterMoney()))
                .setParameter("numberOfGiveOfTakeVehicleMoney", new TypedParameterValue(FloatType.INSTANCE, studentMoveDto.getNumberOfVehicleMoney()));
        return query.executeUpdate();
    }

    @Override
    public int addStudent(StudentDto studentDto) {
        System.out.println("newStudent: " + studentDto);
        String queryAdd = "INSERT INTO student(name, birthday, address, phone, email, starting_date_of_stay,ending_date_of_stay, room_id, water_price_id)" +
                "VALUES(:name, :birthday, :address, :phone, :email, :startingDateOfStay, :endingDateOfStay, :roomId, :waterPriceId)";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryAdd);
        query.setParameter("name", new TypedParameterValue(StringType.INSTANCE, studentDto.getName()))
                .setParameter("birthday", new TypedParameterValue(DateType.INSTANCE, studentDto.getBirthday()))
                .setParameter("address", new TypedParameterValue(StringType.INSTANCE, studentDto.getAddress()))
                .setParameter("phone", new TypedParameterValue(StringType.INSTANCE, studentDto.getPhone()))
                .setParameter("email", new TypedParameterValue(StringType.INSTANCE, studentDto.getEmail()))
                .setParameter("startingDateOfStay", new TypedParameterValue(DateType.INSTANCE, studentDto.getStartingDateOfStay()))
                .setParameter("endingDateOfStay", new TypedParameterValue(DateType.INSTANCE, studentDto.getEndingDateOfStay()))
                .setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, studentDto.getRoomId()))
                .setParameter("waterPriceId", new TypedParameterValue(IntegerType.INSTANCE, studentDto.getWaterPriceId()));
        return query.executeUpdate();
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    public static <T> Optional<T> safeObject(Query query) {
        return query.getResultStream().findFirst();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
