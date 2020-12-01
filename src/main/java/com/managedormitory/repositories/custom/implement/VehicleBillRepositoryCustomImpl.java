package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.vehicle.VehicleBillDto;
import com.managedormitory.repositories.custom.VehicleBillRepositoryCustom;
import com.managedormitory.utils.DateUtil;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DateType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class VehicleBillRepositoryCustomImpl implements VehicleBillRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public int addVehicleBillRepository(VehicleBillDto vehicleBillDto) {
        LocalDate currentDate = LocalDate.now();
        String queryAdd = "INSERT INTO vehicle_bill(start_date, end_date, vehicle_id, payed_money,create_date)\n" +
                "VALUES(:startDate, :endDate, :vehicleId, :payedMoney, :createDate)";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryAdd);
        query.setParameter("startDate", new TypedParameterValue(DateType.INSTANCE, vehicleBillDto.getStartDate()))
                .setParameter("endDate", new TypedParameterValue(DateType.INSTANCE, vehicleBillDto.getEndDate()))
                .setParameter("vehicleId", new TypedParameterValue(IntegerType.INSTANCE, vehicleBillDto.getVehicleId()))
                .setParameter("payedMoney", new TypedParameterValue(FloatType.INSTANCE, vehicleBillDto.getPrice()))
                .setParameter("createDate", new TypedParameterValue(DateType.INSTANCE, DateUtil.getSDateFromLDate(currentDate)));

        return query.executeUpdate();
    }

    @Override
    public Optional<VehicleBillDto> getVehicleBillRecentlyByVehicleId(Integer vehicleId) {
        String queryVehicleBillMaxDate =
                "with max_date as (\n" +
                        "    select MAX(vb.end_date) max_date_water_bill\n" +
                        "    from vehicle_bill vb\n" +
                        "             join vehicle v on v.id = vb.vehicle_id\n" +
                        "             left join student s2 on s2.id = v.student_id\n" +
                        "    where v.id = :vehicleId)\n" +
                        "select vb.bill_id       as billId,\n" +
                        "       s.name           as studentName,\n" +
                        "       v2.id             as vehicleId,\n" +
                        "       v2.student_id    as studentId,\n" +
                        "       vb.start_date    as startDate,\n" +
                        "       vb.end_date      as endDate,\n" +
                        "       pl.price         as price,\n" +
                        "       r.id as roomId\n" +
                        "from vehicle_bill vb\n" +
                        "         join max_date md on vb.end_date = md.max_date_water_bill\n" +
                        "         join vehicle v2 on v2.id = vb.vehicle_id\n" +
                        "         left join student s on s.id = v2.student_id\n" +
                        "         left join room r on r.id = s.room_id\n" +
                        "         join price_list pl on pl.id = v2.vehicle_price_id\n" +
                        "where v2.id = :vehicleId";
        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryVehicleBillMaxDate);
        query.setParameter("vehicleId", new TypedParameterValue(IntegerType.INSTANCE, vehicleId))
                .addScalar("billId", StandardBasicTypes.INTEGER)
                .addScalar("studentName", StandardBasicTypes.STRING)
                .addScalar("studentId", StandardBasicTypes.INTEGER)
                .addScalar("startDate", StandardBasicTypes.DATE)
                .addScalar("endDate", StandardBasicTypes.DATE)
                .addScalar("price", StandardBasicTypes.FLOAT)
                .addScalar("roomId", StandardBasicTypes.INTEGER)
                .addScalar("vehicleId", StandardBasicTypes.INTEGER);
        query.setResultTransformer(new AliasToBeanResultTransformer(VehicleBillDto.class));

        return safeObject(query);
    }

    @Override
    public Optional<VehicleBillDto> getVehicleBillRecentlyByStudentId(Integer id) {
        String queryVehicleBillMaxDate =
                "with max_date as (\n" +
                        "    select MAX(vb.end_date) max_date_water_bill\n" +
                        "    from vehicle_bill vb\n" +
                        "             join vehicle v on v.id = vb.vehicle_id\n" +
                        "            left join student s2 on s2.id = v.student_id\n" +
                        "    where v.student_id = :id)\n" +
                        "select vb.bill_id    as billId,\n" +
                        "       s.name        as studentName,\n" +
                        "       v2.student_id as studentId,\n" +
                        "       vb.start_date as startDate,\n" +
                        "       vb.end_date   as endDate,\n" +
                        "       pl.price      as price,\n" +
                        "       r.id          as roomId\n" +
                        "from vehicle_bill vb\n" +
                        "         join max_date md on vb.end_date = md.max_date_water_bill\n" +
                        "         join vehicle v2 on v2.id = vb.vehicle_id\n" +
                        "         left join student s on s.id = v2.student_id\n" +
                        "         left join room r on r.id = s.room_id\n" +
                        "         join price_list pl on pl.id = v2.vehicle_price_id\n" +
                        "where v2.student_id = :id";
        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryVehicleBillMaxDate);
        query.setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, id))
                .addScalar("billId", StandardBasicTypes.INTEGER)
                .addScalar("studentName", StandardBasicTypes.STRING)
                .addScalar("studentId", StandardBasicTypes.INTEGER)
                .addScalar("startDate", StandardBasicTypes.DATE)
                .addScalar("endDate", StandardBasicTypes.DATE)
                .addScalar("price", StandardBasicTypes.FLOAT)
                .addScalar("roomId", StandardBasicTypes.INTEGER);
        query.setResultTransformer(new AliasToBeanResultTransformer(VehicleBillDto.class));

        return safeObject(query);
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    public static <T> Optional<T> safeObject(Query query) {
        return query.getResultStream().findFirst();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
