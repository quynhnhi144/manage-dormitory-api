package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.SwitchRoomHistoryDto;
import com.managedormitory.repositories.custom.SwitchRoomRepositoryCustom;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.DateType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class SwitchRoomRepositoryCustomImpl implements SwitchRoomRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public int addSwitchRoomHistory(SwitchRoomHistoryDto switchRoomHistoryDto) {
        String queryAdd = "INSERT INTO switch_room_history(old_room_name, new_room_name, room_money, water_money, vehicle_money, student_id, create_date)\n"
                + "VALUES(:oldRoomName, :newRoomName, :roomMoney, :waterMoney, :vehicleMoney, :studentId, :createDate)";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryAdd);
        query.setParameter("oldRoomName", new TypedParameterValue(StringType.INSTANCE, switchRoomHistoryDto.getOldRoomName()))
                .setParameter("newRoomName", new TypedParameterValue(StringType.INSTANCE, switchRoomHistoryDto.getNewRoomName()))
                .setParameter("roomMoney", new TypedParameterValue(FloatType.INSTANCE, switchRoomHistoryDto.getRoomMoney()))
                .setParameter("waterMoney", new TypedParameterValue(FloatType.INSTANCE, switchRoomHistoryDto.getWaterMoney()))
                .setParameter("vehicleMoney", new TypedParameterValue(FloatType.INSTANCE, switchRoomHistoryDto.getVehicleMoney()))
                .setParameter("studentId", new TypedParameterValue(IntegerType.INSTANCE, switchRoomHistoryDto.getStudentId()))
                .setParameter("createDate", new TypedParameterValue(DateType.INSTANCE, switchRoomHistoryDto.getCreateDate()));
        return query.executeUpdate();
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    public static Object safeObject(Query query) {
        return query.getSingleResult();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
