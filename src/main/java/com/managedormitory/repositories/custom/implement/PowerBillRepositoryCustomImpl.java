package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.powerbill.PowerBillDto;
import com.managedormitory.repositories.custom.PowerBillRepositoryCustom;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.LocalDateType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Component
public class PowerBillRepositoryCustomImpl implements PowerBillRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<PowerBillDto> getAllPowerBillByTime() {
        LocalDate currentDate = LocalDate.now();
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
                        "WHERE pb.start_date <= :currentDate and pb.end_date > :currentDate \n" +
                        "GROUP BY roomId, roomName, pb.bill_id, startDate, endDate, numberOfPowerBegin, numberOfPowerEnd, numberOfPowerUsed, isPay,\n" +
                        "         priceAKWH\n" +
                        "ORDER BY roomId ASC";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryPowerBill);
        query.setParameter("currentDate", new TypedParameterValue(LocalDateType.INSTANCE, currentDate))
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

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
