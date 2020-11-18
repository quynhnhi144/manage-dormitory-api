package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.PowerBillImport;
import com.managedormitory.models.dto.powerbill.PowerBillDetail;
import com.managedormitory.models.dto.powerbill.PowerBillDto;
import com.managedormitory.repositories.custom.PowerBillRepositoryCustom;
import com.managedormitory.utils.CalculateMoney;
import com.managedormitory.utils.DateUtil;
import com.managedormitory.utils.LimitedPower;
import org.apache.commons.math3.analysis.function.Power;
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
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PowerBillRepositoryCustomImpl implements PowerBillRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<PowerBillDto> getAllPowerBillByTime(LocalDate currentDate) {
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();
        String queryPowerBill =
                "SELECT DISTINCT ON (pb.room_id) pb.room_id AS roomId,\n" +
                        "                                r.name AS roomName,\n" +
                        "                                MAX(pb.bill_id) AS billId,\n" +
                        "                                MAX(pb.start_date) AS startDate,\n" +
                        "                                MAX(pb.end_date) AS endDate,\n" +
                        "                                pb.number_of_power_begin AS numberOfPowerBegin,\n" +
                        "                                pb.number_of_power_end AS numberOfPowerEnd,\n" +
                        "                                pb.number_of_power_used AS numberOfPowerUsed,\n" +
                        "                                pb.number_of_money_must_pay AS numberOfMoneyMustPay,\n" +
                        "                                pb.is_pay AS isPay,\n" +
                        "                                pl.id AS idPriceList,\n" +
                        "                                pl.name AS namePriceList,\n" +
                        "                                pl.price AS priceAKWH\n" +
                        "FROM power_bill pb\n" +
                        "         JOIN room r ON r.id = pb.room_id\n" +
                        "         JOIN price_list pl ON pl.id = pb.price_list_id\n" +
                        "\n" +
                        "WHERE extract(month from pb.end_date) = :month AND extract(year from pb.end_date) = :year\n" +
                        "GROUP BY pb.room_id, r.name, pb.is_pay, pl.id, pl.name, pl.price, pb.number_of_money_must_pay, pb.number_of_power_begin,\n" +
                        "         pb.number_of_power_end, pb.number_of_power_used\n" +
                        "ORDER BY pb.room_id ASC";
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
                .addScalar("numberOfMoneyMustPay", StandardBasicTypes.FLOAT)
                .addScalar("isPay", StandardBasicTypes.BOOLEAN)
                .addScalar("idPriceList", StandardBasicTypes.INTEGER)
                .addScalar("namePriceList", StandardBasicTypes.STRING)
                .addScalar("priceAKWH", StandardBasicTypes.FLOAT);

        query.setResultTransformer(new AliasToBeanResultTransformer(PowerBillDto.class));
        return safeList(query);
    }

    @Override
    public List<PowerBillDto> getAllPowerBillByMaxEndDate() {
        String queryNative =
                "SELECT DISTINCT ON (pb.room_id) pb.room_id AS roomId,\n" +
                        "                                r.name AS roomName,\n" +
                        "                                MAX(pb.bill_id) AS billId,\n" +
                        "                                MAX(pb.start_date) AS startDate,\n" +
                        "                                MAX(pb.end_date) AS endDate,\n" +
                        "                                pb.number_of_power_begin AS numberOfPowerBegin,\n" +
                        "                                pb.number_of_power_end AS numberOfPowerEnd,\n" +
                        "                                pb.number_of_power_used AS numberOfPowerUsed,\n" +
                        "                                pb.number_of_money_must_pay AS numberOfMoneyMustPay" +
                        ",\n" +
                        "                                pb.is_pay AS isPay,\n" +
                        "                                pl.id AS idPriceList,\n" +
                        "                                pl.name AS namePriceList,\n" +
                        "                                pl.price AS priceAKWH\n" +
                        "FROM power_bill pb\n" +
                        "         JOIN room r ON r.id = pb.room_id\n" +
                        "         JOIN price_list pl ON pl.id = pb.price_list_id\n" +
                        "GROUP BY pb.room_id, r.name, pb.is_pay, pl.id, pl.name, pl.price, pb.number_of_money_must_pay, pb.number_of_power_begin,\n" +
                        "         pb.number_of_power_end, pb.number_of_power_used\n" +
                        "ORDER BY roomId ASC";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryNative);
        query.addScalar("roomId", StandardBasicTypes.INTEGER)
                .addScalar("roomName", StandardBasicTypes.STRING)
                .addScalar("billId", StandardBasicTypes.INTEGER)
                .addScalar("startDate", StandardBasicTypes.DATE)
                .addScalar("endDate", StandardBasicTypes.DATE)
                .addScalar("numberOfPowerBegin", StandardBasicTypes.LONG)
                .addScalar("numberOfPowerEnd", StandardBasicTypes.LONG)
                .addScalar("numberOfPowerUsed", StandardBasicTypes.LONG)
                .addScalar("numberOfMoneyMustPay", StandardBasicTypes.FLOAT)
                .addScalar("isPay", StandardBasicTypes.BOOLEAN)
                .addScalar("idPriceList", StandardBasicTypes.INTEGER)
                .addScalar("namePriceList", StandardBasicTypes.STRING)
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
                        "    number_of_money_must_pay = :numberOfMoneyMustPay,\n" +
                        "    is_pay = :pay\n" +
                        "\n" +
                        "WHERE bill_id = :billId and room_id = :roomId";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryUpdate);
        query.setParameter("startDate", new TypedParameterValue(DateType.INSTANCE, powerBillDetail.getStartDate()))
                .setParameter("endDate", new TypedParameterValue(DateType.INSTANCE, powerBillDetail.getEndDate()))
                .setParameter("numberOfPowerBegin", new TypedParameterValue(LongType.INSTANCE, powerBillDetail.getNumberOfPowerBegin()))
                .setParameter("numberOfPowerEnd", new TypedParameterValue(LongType.INSTANCE, powerBillDetail.getNumberOfPowerEnd()))
                .setParameter("numberOfPowerUsed", new TypedParameterValue(LongType.INSTANCE, powerBillDetail.getNumberOfPowerUsed()))
                .setParameter("numberOfMoneyMustPay", new TypedParameterValue(FloatType.INSTANCE, powerBillDetail.getNumberOfMoneyMustPay()))
                .setParameter("pay", new TypedParameterValue(BooleanType.INSTANCE, powerBillDetail.isPay()))
                .setParameter("billId", new TypedParameterValue(IntegerType.INSTANCE, powerBillDetail.getBillId()))
                .setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, roomId));

        return query.executeUpdate();
    }

    @Override
    public int insertPowerBills(List<PowerBillDetail> powerBillDetails, List<PowerBillImport> powerBillImports) {
        List<PowerBillDetail> powerBillDetailsInsert = new ArrayList<>();
        for (int i = 0; i < powerBillDetails.size(); i++) {
            PowerBillDetail powerBillDetail = powerBillDetails.get(i);
            for (int j = 0; j < powerBillImports.size(); j++) {
                PowerBillImport powerBillImport = powerBillImports.get(j);
                if (powerBillImport.getRoomName().equals(powerBillDetail.getDetailRoomDto().getName())) {
                    PowerBillDetail powerBillDetailNew = new PowerBillDetail(
                            powerBillDetail.getDetailRoomDto(),
                            powerBillDetail.getBillId(),
                            powerBillDetail.getEndDate(),
                            DateUtil.getSDateFromLDate(powerBillImport.getEndDate()),
                            powerBillDetail.getNumberOfPowerEnd(),
                            powerBillImport.getNumberOfPowerEnd(),
                            powerBillImport.getNumberOfPowerEnd() - powerBillDetail.getNumberOfPowerEnd(),
                            false,
                            powerBillDetail.getPriceList(),
                            CalculateMoney.calculatePowerBill(powerBillDetail, powerBillDetail.getNumberOfPowerEnd(),
                                    powerBillImport.getNumberOfPowerEnd())
                    );
                    System.out.println(powerBillDetailNew);
                    powerBillDetailsInsert.add(powerBillDetailNew);
                }
            }
        }

        String queryInsert = "INSERT INTO power_bill(start_date, end_date, number_of_power_begin, number_of_power_end, number_of_power_used, number_of_money_must_pay, is_pay, room_id, price_list_id)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return getCurrentSession().doReturningWork(
                connection -> {
                    int rowCount = 0;
                    int batchCount = 0;
                    PreparedStatement s = connection.prepareStatement(queryInsert);
                    for (PowerBillDetail powerBillDetail : powerBillDetailsInsert) {
                        s.setDate(1, powerBillDetail.getStartDate());
                        s.setDate(2, powerBillDetail.getEndDate());
                        s.setLong(3, powerBillDetail.getNumberOfPowerBegin());
                        s.setLong(4, powerBillDetail.getNumberOfPowerEnd());
                        s.setLong(5, powerBillDetail.getNumberOfPowerUsed());
                        s.setFloat(6, powerBillDetail.getNumberOfMoneyMustPay());
                        s.setBoolean(7, powerBillDetail.isPay());
                        s.setInt(8, powerBillDetail.getDetailRoomDto().getId());
                        s.setInt(9, powerBillDetail.getPriceList().getId());
                        s.addBatch();
                        batchCount += 1;
                        if (batchCount % LimitedPower.BATCH_SIZE == 0) {
                            rowCount += s.executeBatch().length;
                        }
                    }
                    rowCount += s.executeBatch().length;
                    return rowCount;
                });
    }

    @Override
    public int insertPowerBill(Integer roomId, PowerBillDetail powerBillDetail) {
        String queryInsert = "INSERT INTO power_bill(start_date, end_date, number_of_power_begin, number_of_power_end, number_of_power_used, number_of_money_must_pay, is_pay, room_id, price_list_id)" +
                "VALUES(:startDate, :endDate, :numberOfPowerBegin, :numberOfPowerEnd, :numberOfPowerUsed, :numberOfMoneyMustPay, :isPay, :roomId, :idPriceList)";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryInsert);
        query.setParameter("startDate", new TypedParameterValue(DateType.INSTANCE, powerBillDetail.getStartDate()))
                .setParameter("endDate", new TypedParameterValue(DateType.INSTANCE, powerBillDetail.getEndDate()))
                .setParameter("numberOfPowerBegin", new TypedParameterValue(LongType.INSTANCE, powerBillDetail.getNumberOfPowerBegin()))
                .setParameter("numberOfPowerEnd", new TypedParameterValue(LongType.INSTANCE, powerBillDetail.getNumberOfPowerEnd()))
                .setParameter("numberOfPowerUsed", new TypedParameterValue(LongType.INSTANCE, powerBillDetail.getNumberOfPowerUsed()))
                .setParameter("numberOfMoneyMustPay", new TypedParameterValue(FloatType.INSTANCE, powerBillDetail.getNumberOfMoneyMustPay()))
                .setParameter("isPay", new TypedParameterValue(BooleanType.INSTANCE, powerBillDetail.isPay()))
                .setParameter("roomId", new TypedParameterValue(IntegerType.INSTANCE, roomId))
                .setParameter("idPriceList", new TypedParameterValue(IntegerType.INSTANCE, powerBillDetail.getPriceList().getId()));

        return query.executeUpdate();
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
