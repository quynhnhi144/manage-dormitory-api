package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.vehicle.VehicleDetailDto;
import com.managedormitory.models.dto.vehicle.VehicleDto;
import com.managedormitory.models.dto.vehicle.VehicleMoveDto;
import com.managedormitory.models.dto.vehicle.VehicleNew;
import com.managedormitory.repositories.custom.VehicleRepositoryCustom;
import com.managedormitory.utils.DateUtil;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Component
public class VehicleRepositoryCustomImpl implements VehicleRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<VehicleDto> getAllVehicleByTime() {
        LocalDate currentDate = LocalDate.now();
        String queryVehicle =
                "SELECT v.id AS id,\n" +
                        "v.license_plates AS licensePlates,\n" +
                        "s.id AS studentId,\n" +
                        "s.name AS studentName,\n" +
                        "r.name AS roomName,\n" +
                        "c.name AS campusName,\n" +
                        "vb.start_date AS startDate,\n" +
                        "vb.end_date AS endDate,\n" +
                        "u.full_name AS userManager,\n" +
                        "tv.name AS typeVehicle,\n" +
                        "tv.id AS typeVehicleId\n" +
                        "FROM vehicle v\n" +
                        "         JOIN student s on s.id = v.student_id\n" +
                        "         JOIN type_vehicle tv on tv.id = v.type_vehicle_id\n" +
                        "         LEFT JOIN vehicle_bill vb on v.id = vb.vehicle_id\n" +
                        "         LEFT JOIN room r on r.id = s.room_id\n" +
                        "         LEFT JOIN campus c on c.id = r.campus_id\n" +
                        "         LEFT JOIN users u on u.id = c.user_id\n" +
                        "WHERE vb.end_date >= :currentDate\n" +
                        "GROUP BY v.id,\n" +
                        "         v.license_plates,\n" +
                        "         s.id,\n" +
                        "         r.name, c.name, vb.start_date,\n" +
                        "         vb.end_date, u.full_name, tv.name, tv.id\n" +
                        "ORDER BY v.id ASC";
        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryVehicle);
        query.setParameter("currentDate", new TypedParameterValue(LocalDateType.INSTANCE, currentDate))
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("licensePlates", StandardBasicTypes.STRING)
                .addScalar("typeVehicle", StandardBasicTypes.STRING)
                .addScalar("typeVehicleId", StandardBasicTypes.INTEGER)
                .addScalar("studentId", StandardBasicTypes.INTEGER)
                .addScalar("studentName", StandardBasicTypes.STRING)
                .addScalar("roomName", StandardBasicTypes.STRING)
                .addScalar("campusName", StandardBasicTypes.STRING)
                .addScalar("userManager", StandardBasicTypes.STRING)
                .addScalar("startDate", StandardBasicTypes.DATE)
                .addScalar("endDate", StandardBasicTypes.DATE);
        query.setResultTransformer(new AliasToBeanResultTransformer(VehicleDto.class));

        return safeList(query);
    }

    @Override
    public int addVehicle(VehicleNew vehicleNew) {
        String queryAdd = "INSERT INTO vehicle(license_plates, student_id, type_vehicle_id, vehicle_price_id)\n" +
                "VALUES(:licensePlates, :studentId, :typeVehicleId, :vehiclePriceId)";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryAdd);
        query.setParameter("licensePlates", new TypedParameterValue(StringType.INSTANCE, vehicleNew.getLicensePlates()))
                .setParameter("studentId", new TypedParameterValue(IntegerType.INSTANCE, vehicleNew.getVehicleBillDto().getStudentId()))
                .setParameter("typeVehicleId", new TypedParameterValue(IntegerType.INSTANCE, vehicleNew.getTypeVehicleId()))
                .setParameter("vehiclePriceId", new TypedParameterValue(IntegerType.INSTANCE, vehicleNew.getVehiclePriceId()));

        return query.executeUpdate();
    }

    @Override
    public int updateVehicle(Integer id, VehicleDetailDto vehicleDetailDto) {
        String queryUpdate = "UPDATE vehicle\n" +
                "SET license_plates = :licensePlates, type_vehicle_id= :typeVehicleId\n" +
                "WHERE id = :id";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryUpdate);
        query.setParameter("licensePlates", new TypedParameterValue(StringType.INSTANCE, vehicleDetailDto.getLicensePlates()))
                .setParameter("typeVehicleId", new TypedParameterValue(IntegerType.INSTANCE, vehicleDetailDto.getStudentDto().getId()))
                .setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, id));
        return query.executeUpdate();
    }

    @Override
    public int addVehicleLeft(VehicleMoveDto vehicleMoveDto) {
        String queryAdd = "INSERT INTO vehicle_left\n" +
                "VALUES(:id, :leavingDate, :numberOfVehicleMoney)";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryAdd);
        query.setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, vehicleMoveDto.getId()))
                .setParameter("leavingDate", new TypedParameterValue(DateType.INSTANCE, DateUtil.getSDateFromLDate(vehicleMoveDto.getLeavingDate())))
                .setParameter("numberOfVehicleMoney", new TypedParameterValue(FloatType.INSTANCE, vehicleMoveDto.getNumberOfVehicleMoney()));
        return query.executeUpdate();
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
