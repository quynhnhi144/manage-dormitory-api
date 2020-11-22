package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.VehicleBillDto;
import com.managedormitory.repositories.custom.VehicleBillRepositoryCustom;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.DateType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class VehicleBillRepositoryCustomImpl implements VehicleBillRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public int addVehicleBillRepository(VehicleBillDto vehicleBillDto) {
        String queryAdd = "INSERT INTO vehicle_bill(start_date, end_date, student_id, payed_money)\n" +
                "VALUES(:startDate, :endDate, :studentId, :payedMoney)";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryAdd);
        query.setParameter("startDate", new TypedParameterValue(DateType.INSTANCE, vehicleBillDto.getStartDate()))
                .setParameter("endDate", new TypedParameterValue(DateType.INSTANCE, vehicleBillDto.getEndDate()))
                .setParameter("studentId", new TypedParameterValue(IntegerType.INSTANCE, vehicleBillDto.getStudentId()))
                .setParameter("payedMoney", new TypedParameterValue(FloatType.INSTANCE, vehicleBillDto.getPrice()));
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
