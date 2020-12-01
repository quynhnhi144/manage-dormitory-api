package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dao.WaterBill;
import com.managedormitory.models.dto.WaterBillDto;
import com.managedormitory.repositories.custom.WaterBillRepositoryCustom;
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
public class WaterBillRepositoryCustomImpl implements WaterBillRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public int addWaterBill(WaterBillDto waterBillDto) {
        LocalDate currentDate = LocalDate.now();
        String queryAdd = "INSERT INTO water_bill(start_date, end_date, student_id, payed_money, create_date)\n" +
                "VALUES(:startDate, :endDate, :studentId, :payedMoney, :createDate)";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryAdd);
        query.setParameter("startDate", new TypedParameterValue(DateType.INSTANCE, waterBillDto.getStartDate()))
                .setParameter("endDate", new TypedParameterValue(DateType.INSTANCE, waterBillDto.getEndDate()))
                .setParameter("studentId", new TypedParameterValue(IntegerType.INSTANCE, waterBillDto.getStudentId()))
                .setParameter("payedMoney", new TypedParameterValue(FloatType.INSTANCE, waterBillDto.getPrice()))
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
