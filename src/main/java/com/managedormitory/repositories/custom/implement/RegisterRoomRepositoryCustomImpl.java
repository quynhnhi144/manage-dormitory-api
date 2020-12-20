package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.registerRoom.RegisterRoomDto;
import com.managedormitory.repositories.custom.RegisterRoomRepositoryCustom;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class RegisterRoomRepositoryCustomImpl implements RegisterRoomRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<RegisterRoomDto> getAllRegisterRoom() {
        String queryGet = "SELECT rr.id AS id, rr.address AS address, rr.birthday AS birthday,\n" +
                "       rr.email AS email, rr.id_card AS idCard, rr.phone AS phone,\n" +
                "       rr.starting_date_of_stay AS startingDateOfStay, rr.student_name AS studentName,\n" +
                "       r.name AS roomName, r.id AS roomId, tr.name AS typeRoomName , c.name as campusName\n" +
                "FROM register_room rr\n" +
                "         JOIN room r ON r.id = rr.room_id\n" +
                "         JOIN type_room tr ON tr.id = r.type_room_id\n" +
                "         JOIN campus c on r.campus_id = c.id\n" +
                "ORDER BY rr.id ASC";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryGet);
        query.addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("address", StandardBasicTypes.STRING)
                .addScalar("birthday", StandardBasicTypes.DATE)
                .addScalar("email", StandardBasicTypes.STRING)
                .addScalar("idCard", StandardBasicTypes.STRING)
                .addScalar("phone", StandardBasicTypes.STRING)
                .addScalar("startingDateOfStay", StandardBasicTypes.DATE)
                .addScalar("studentName", StandardBasicTypes.STRING)
                .addScalar("roomId", StandardBasicTypes.INTEGER)
                .addScalar("roomName", StandardBasicTypes.STRING)
                .addScalar("typeRoomName", StandardBasicTypes.STRING)
                .addScalar("campusName", StandardBasicTypes.STRING);
        query.setResultTransformer(new AliasToBeanResultTransformer(RegisterRoomDto.class));
        return safeList(query);

    }

    @Override
    public int addRegisterRoom(RegisterRoomDto registerRoomDto) {
        String queryAddStudentForRemainingRoom = "INSERT INTO register_room (address, birthday, email, id_card, phone, starting_date_of_stay, student_name, room_id)\n" +
                "VALUES(:address, :birthday, :email, :idCard, :phone, :startingDateOfStay, :studentName, :roomId)";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryAddStudentForRemainingRoom);
        query.setParameter("address", new TypedParameterValue(StringType.INSTANCE, registerRoomDto.getAddress()))
                .setParameter("birthday", new TypedParameterValue(DateType.INSTANCE, registerRoomDto.getBirthday()))
                .setParameter("email", new TypedParameterValue(StringType.INSTANCE, registerRoomDto.getEmail()))
                .setParameter("idCard", new TypedParameterValue(StringType.INSTANCE, registerRoomDto.getIdCard()))
                .setParameter("phone", new TypedParameterValue(StringType.INSTANCE, registerRoomDto.getPhone()))
                .setParameter("startingDateOfStay", new TypedParameterValue(DateType.INSTANCE, registerRoomDto.getStartingDateOfStay()))
                .setParameter("studentName", new TypedParameterValue(StringType.INSTANCE, registerRoomDto.getStudentName()))
                .setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, registerRoomDto.getRoomId()));

        return query.executeUpdate();
    }

    @Override
    public int deleteRegisterRoom(Integer id) {
        String queryDelete = "DELETE FROM register_room WHERE id = :id";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryDelete);
        query.setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, id));

        return query.executeUpdate();
    }

    @Override
    public int countRegisterOfARoom(Integer roomId) {
        String queryCount = "select count(rr.room_id) from register_room rr left join room r on r.id = rr.room_id\n" +
                "where rr.room_id = :roomId";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryCount);
        query.setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, roomId));
      return  ((Number) query.getSingleResult()).intValue();
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }


    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
