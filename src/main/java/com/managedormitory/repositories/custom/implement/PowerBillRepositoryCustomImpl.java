package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.dto.powerbill.PowerBillDto;
import com.managedormitory.repositories.custom.PowerBillRepositoryCustom;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Component
public class PowerBillRepositoryCustomImpl implements PowerBillRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<PowerBillDto> getAllPowerBillByTime(LocalDate currentDate) {
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();
        String queryPowerBill =
                "SELECT r.id AS roomId,\n" +
                        "r.name AS roomName,\n" +
                        "pb.bill_id AS billId,\n" +
                        "pb.start_date AS startDate,\n" +
                        "pb.end_date AS endDate,\n" +
                        "pb.number_of_power_begin AS numberOfPowerBegin,\n" +
                        "pb.number_of_power_end AS numberOfPowerEnd,\n" +
                        "pb.number_of_power_used AS numberOfPowerUsed,\n" +
                        "pb.is_pay AS isPay,\n" +
                        "pl.price AS priceAKWH\n" +
                        "FROM room r\n" +
                        "         LEFT JOIN power_bill pb ON r.id = pb.room_id\n" +
                        "         JOIN price_list pl ON pl.id = pb.price_list_id\n" +
                        "WHERE extract(month from pb.end_date) = :month and extract(year from pb.end_date) = :year \n" +
                        "GROUP BY roomId, roomName, pb.bill_id, startDate, endDate, numberOfPowerBegin, numberOfPowerEnd, numberOfPowerUsed, isPay,\n" +
                        "         priceAKWH\n" +
                        "ORDER BY roomId ASC";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryPowerBill);
        query.setParameter("month", new TypedParameterValue(IntegerType.INSTANCE, month))
                .setParameter("year", new TypedParameterValue(IntegerType.INSTANCE, year))
                .addScalar("roomId", StandardBasicTypes.INTEGER)
                .addScalar("roomName", StandardBasicTypes.STRING)
                .addScalar("billId", StandardBasicTypes.INTEGER)
                .addScalar("startDate", StandardBasicTypes.DATE)
                .addScalar("endDate", StandardBasicTypes.DATE)
                .addScalar("numberOfPowerBegin", StandardBasicTypes.LONG)
                .addScalar("numberOfPowerEnd", StandardBasicTypes.LONG)
                .addScalar("numberOfPowerUsed", StandardBasicTypes.LONG)
                .addScalar("isPay", StandardBasicTypes.BOOLEAN)
                .addScalar("priceAKWH", StandardBasicTypes.FLOAT);

        query.setResultTransformer(new AliasToBeanResultTransformer(PowerBillDto.class));
        return safeList(query);
    }

    @Override
    public int updatePowerBill(Integer roomId, PowerBillDetail powerBillDetail) {
        String queryUpdate =
                "UPDATE power_bill\n" +
                        "SET start_date = :startDate,\n" +
                        "    end_date = :endDate,\n" +
                        "    number_of_power_begin = :numberOfPowerBegin,\n" +
                        "    number_of_power_end = :numberOfPowerEnd,\n" +
                        "    number_of_power_used = :numberOfPowerUsed,\n" +
                        "    is_pay = :pay\n" +
                        "\n" +
                        "WHERE bill_id = :billId and room_id = :roomId";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryUpdate);
        query.setParameter("startDate", new TypedParameterValue(DateType.INSTANCE, powerBillDetail.getStartDate()))
                .setParameter("endDate", new TypedParameterValue(DateType.INSTANCE, powerBillDetail.getEndDate()))
                .setParameter("numberOfPowerBegin", new TypedParameterValue(LongType.INSTANCE, powerBillDetail.getNumberOfPowerBegin()))
                .setParameter("numberOfPowerEnd", new TypedParameterValue(LongType.INSTANCE, powerBillDetail.getNumberOfPowerEnd()))
                .setParameter("numberOfPowerUsed", new TypedParameterValue(LongType.INSTANCE, powerBillDetail.getNumberOfPowerUsed()))
                .setParameter("pay", new TypedParameterValue(BooleanType.INSTANCE, powerBillDetail.isPay()))
                .setParameter("billId", new TypedParameterValue(IntegerType.INSTANCE, powerBillDetail.getBillId()))
                .setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, roomId));

        return query.executeUpdate();
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
