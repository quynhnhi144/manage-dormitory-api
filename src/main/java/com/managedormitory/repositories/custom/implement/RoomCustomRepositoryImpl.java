package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.RoomDto;
import com.managedormitory.models.dto.RoomFilterDto;
import com.managedormitory.repositories.custom.RoomCustomRepository;
import com.managedormitory.utils.QueryUtil;
import com.managedormitory.utils.StringUtil;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class RoomCustomRepositoryImpl implements RoomCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Query filterRoom(RoomFilterDto roomFilterDto) {
        String queryRoom = QueryUtil.QUERY_ALL_ROOMS.toString();

        if (roomFilterDto.getCampusName() != null) {
            queryRoom += "where c.name = " + StringUtil.QUOTE + roomFilterDto.getCampusName() + StringUtil.QUOTE + "\n";
        } else {
            queryRoom += "where c.name is not null\n";
        }
        if (roomFilterDto.getQuantityStudent() != null) {
            if (roomFilterDto.getQuantityStudent() == -1) {
                queryRoom += "and r.quantity_student is not null " + "\n";
            } else {
                queryRoom += "and r.quantity_student = " + roomFilterDto.getQuantityStudent() + "\n";
            }
        }
        if (roomFilterDto.getUserManager() != null) {
            queryRoom += "and lower(u.full_name) like N"
                    + StringUtil.QUOTE + StringUtil.PERCENTAGE + roomFilterDto.getUserManager().toLowerCase() + StringUtil.PERCENTAGE + StringUtil.QUOTE + "\n";
        }
        if (roomFilterDto.getTypeRoom() != null) {
            if (roomFilterDto.getTypeRoom().equals("All")) {
                queryRoom += "and tr.name is not null ";
            } else {
                queryRoom += "and tr.name = " + StringUtil.QUOTE + roomFilterDto.getTypeRoom() + StringUtil.QUOTE + "\n";
            }
        }

        queryRoom += "group by r.id, r.name, r.quantity_student, tr.name, c.name, u.full_name, dr.is_pay\n" +
                "order by r.id asc\n";

        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryRoom);

        query.addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("quantityStudent", StandardBasicTypes.INTEGER)
                .addScalar("typeRoomName", StandardBasicTypes.STRING)
                .addScalar("campusName", StandardBasicTypes.STRING)
                .addScalar("userManager", StandardBasicTypes.STRING)
                .addScalar("isPay", StandardBasicTypes.BOOLEAN);
        query.setResultTransformer(new AliasToBeanResultTransformer(RoomDto.class));
        return query;
    }

//    public static <Entity> List<Entity> safeList(Query query) {
//        return query.getResultList();
//    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
