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
                        "tv.name AS typeVehicle,\n" +
                        "s.id AS studentId,\n" +
                        "s.name AS studentName,\n" +
                        "r.name AS roomName,\n" +
                        "c.name AS campusName,\n" +
                        "u.full_name AS userManager,\n" +
                        "vb.is_pay AS isPayVehicleBill\n" +
                        "FROM vehicle v\n" +
                        QueryUtil.JOIN_STUDENT +
                        QueryUtil.LEFTJOIN_VEHICLEBILL +
                        QueryUtil.JOIN_ROOM +
                        QueryUtil.JOIN_CAMPUS +
                        QueryUtil.JOIN_USERS +
                        QueryUtil.JOIN_TYPEVEHICLE +
                        "WHERE vb.end_date >= :currentDate\n" +
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
                .addScalar("isPayVehicleBill", StandardBasicTypes.BOOLEAN);
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
