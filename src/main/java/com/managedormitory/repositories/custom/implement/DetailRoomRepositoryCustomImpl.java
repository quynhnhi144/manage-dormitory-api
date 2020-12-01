package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.repositories.custom.DetailRoomRepositoryCustom;
import com.managedormitory.utils.DateUtil;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.DateType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Component
public class DetailRoomRepositoryCustomImpl implements DetailRoomRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public int addDetailRoom(RoomBillDto roomBillDto) {
        LocalDate currentDate = LocalDate.now();
        String queryAdd = "INSERT INTO detail_room(start_date, end_date, student_id, payed_money, create_date)\n" +
                "VALUES(:startDate, :endDate, :studentId, :payedMoney, :createDate)";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryAdd);
        query.setParameter("startDate", new TypedParameterValue(DateType.INSTANCE, roomBillDto.getStartDate()))
                .setParameter("endDate", new TypedParameterValue(DateType.INSTANCE, roomBillDto.getEndDate()))
                .setParameter("studentId", new TypedParameterValue(IntegerType.INSTANCE, roomBillDto.getStudentId()))
                .setParameter("payedMoney", new TypedParameterValue(FloatType.INSTANCE, roomBillDto.getPrice()))
                .setParameter("currentDate", new TypedParameterValue(DateType.INSTANCE, DateUtil.getSDateFromLDate(currentDate)));
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
