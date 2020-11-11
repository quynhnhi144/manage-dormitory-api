package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.VehicleDto;
import com.managedormitory.repositories.custom.VehicleRepositoryCustom;
import com.managedormitory.utils.QueryUtil;
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
                        "r.name AS roomName,\n" +
                        "c.name AS campusName,\n" +
                        "vb.start_date AS startDate,\n" +
                        "vb.end_date AS endDate,\n" +
                        "u.full_name AS userManager,\n" +
                        "tv.name AS typeVehicle\n" +
                        "FROM vehicle v\n" +
                        "         JOIN student s on s.id = v.student_id\n" +
                        "         JOIN type_vehicle tv on tv.id = v.type_vehicle_id\n" +
                        "         LEFT JOIN vehicle_bill vb on v.id = vb.vehicle_id\n" +
                        "         LEFT JOIN room r on r.id = s.room_id\n" +
                        "         LEFT JOIN campus c on c.id = r.campus_id\n" +
                        "         LEFT JOIN users u on u.user_id = c.user_id\n" +
                        "WHERE vb.end_date >= :currentDate\n" +
                        "GROUP BY v.id,\n" +
                        "         v.license_plates,\n" +
                        "         s.id,\n" +
                        "         r.name, c.name, vb.start_date,\n" +
                        "         vb.end_date, u.full_name, tv.name\n" +
                        "ORDER BY v.id ASC";
        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryVehicle);
        query.setParameter("currentDate", new TypedParameterValue(LocalDateType.INSTANCE, currentDate))
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("licensePlates", StandardBasicTypes.STRING)
                .addScalar("typeVehicle", StandardBasicTypes.STRING)
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

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}
