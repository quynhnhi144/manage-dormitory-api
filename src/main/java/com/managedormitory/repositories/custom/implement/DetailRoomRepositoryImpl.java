package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.DetailRoomMapDB;
import com.managedormitory.repositories.custom.DetailRoomRepository;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class DetailRoomRepositoryImpl implements DetailRoomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<DetailRoomMapDB> getDetailARoom(Integer roomId) {
        System.out.println("roomId"+ roomId);
        final String queryString =
                "SELECT r.id AS roomId,\n" +
                        "r.name AS roomName,\n" +
                        "r.quantity_student AS quantityStudent,\n" +
                        "c.name AS campusName,\n" +
                        "u.full_name as userManager,\n"+
                        "pl.price AS priceRoom,\n" +
                        "tr.name AS typeRoom,\n" +
                        "s.id AS studentId,\n " +
                        "s.name AS studentName,\n" +
                        "s.phone AS studentPhoneNumber,\n" +
                        "s.email AS studentEmail,\n" +
                        "dr.is_pay AS isPay\n" +
                        "from room r\n" +
                        "    left join detail_room dr on r.id = dr.room_id\n" +
                        "    left join student s on s.id = dr.student_id\n" +
                        "     join campus c on r.campus_id = c.id\n" +
                        "     join price_list pl on r.price_list_id = pl.id\n" +
                        "     join type_room tr on r.type_room_id = tr.id\n" +
                        "     join users u on u.user_id = c.user_id\n"+
                        "where r.id = :roomId";

        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryString);

        query.setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, roomId))
                .addScalar("roomId", StandardBasicTypes.INTEGER)
                .addScalar("roomName", StandardBasicTypes.STRING)
                .addScalar("quantityStudent", StandardBasicTypes.INTEGER)
                .addScalar("campusName", StandardBasicTypes.STRING)
                .addScalar("userManager", StandardBasicTypes.STRING)
                .addScalar("priceRoom", StandardBasicTypes.FLOAT)
                .addScalar("typeRoom", StandardBasicTypes.STRING)
                .addScalar("studentId", StandardBasicTypes.INTEGER)
                .addScalar("studentName", StandardBasicTypes.STRING)
                .addScalar("studentPhoneNumber", StandardBasicTypes.STRING)
                .addScalar("studentEmail", StandardBasicTypes.STRING)
                .addScalar("isPay", StandardBasicTypes.BOOLEAN);
        query.setResultTransformer(new AliasToBeanResultTransformer(DetailRoomMapDB.class));

        return safeList(query);
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }

}
