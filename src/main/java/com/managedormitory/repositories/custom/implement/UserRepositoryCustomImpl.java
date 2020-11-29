package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dao.User;
import com.managedormitory.models.dto.CampusDto;
import com.managedormitory.models.dto.UserUpdate;
import com.managedormitory.repositories.custom.UserRepositoryCustom;
import com.managedormitory.utils.DateUtil;
import com.managedormitory.utils.LimitedPower;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public int updateUser(Integer id, UserUpdate userUpdate) {
        String queryUpdate = "UPDATE users\n" +
                "SET username = :username,\n" +
                "full_name = :fullName,\n" +
                "email = :email,\n" +
                "birthday = :birthday,\n" +
                "phone = :phone,\n" +
                "password = :password,\n" +
                "address = :address\n" +
                "WHERE id = :id";
        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryUpdate);
        query.setParameter("username", new TypedParameterValue(StringType.INSTANCE, userUpdate.getUserDto().getUsername()))
                .setParameter("fullName", new TypedParameterValue(StringType.INSTANCE, userUpdate.getUserDto().getFullName()))
                .setParameter("email", new TypedParameterValue(StringType.INSTANCE, userUpdate.getUserDto().getEmail()))
                .setParameter("birthday", new TypedParameterValue(DateType.INSTANCE, DateUtil.getSDateFromLDate(userUpdate.getUserDto().getBirthday())))
                .setParameter("phone", new TypedParameterValue(StringType.INSTANCE, userUpdate.getUserDto().getPhone()))
                .setParameter("password", new TypedParameterValue(StringType.INSTANCE, encoder.encode(userUpdate.getNewPassword())))
                .setParameter("address", new TypedParameterValue(StringType.INSTANCE, userUpdate.getUserDto().getAddress()))
                .setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, id));

        return query.executeUpdate();
    }

    @Override
    public int updateUserWithoutPassword(Integer id, UserUpdate userUpdate) {
        String queryUpdate = "UPDATE users\n" +
                "SET username = :username,\n" +
                "full_name = :fullName,\n" +
                "email = :email,\n" +
                "birthday = :birthday,\n" +
                "phone = :phone,\n" +
                "address = :address\n" +
                "WHERE id = :id";
        NativeQuery<?> query = getCurrentSession().createNativeQuery(queryUpdate);
        query.setParameter("username", new TypedParameterValue(StringType.INSTANCE, userUpdate.getUserDto().getUsername()))
                .setParameter("fullName", new TypedParameterValue(StringType.INSTANCE, userUpdate.getUserDto().getFullName()))
                .setParameter("email", new TypedParameterValue(StringType.INSTANCE, userUpdate.getUserDto().getEmail()))
                .setParameter("birthday", new TypedParameterValue(DateType.INSTANCE, DateUtil.getSDateFromLDate(userUpdate.getUserDto().getBirthday())))
                .setParameter("phone", new TypedParameterValue(StringType.INSTANCE, userUpdate.getUserDto().getPhone()))
                .setParameter("address", new TypedParameterValue(StringType.INSTANCE, userUpdate.getUserDto().getAddress()))
                .setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, id));

        return query.executeUpdate();
    }

    @Override
    public int updateCampus(UserUpdate userUpdate) {
        System.out.println("caampus: " + userUpdate);
        String queryUpdateCampus = "UPDATE campus\n" +
                "SET user_id = ?\n" +
                "WHERE id = ?";

        List<CampusDto> campuses = userUpdate.getUserDto().getCampuses();
        return getCurrentSession().doReturningWork(
                connection -> {
                    int rowCount = 0;
                    int batchCount = 0;
                    PreparedStatement s = connection.prepareStatement(queryUpdateCampus);
                    for (CampusDto campusDto : campuses) {
                        s.setInt(1, userUpdate.getUserDto().getId());
                        s.setInt(2, campusDto.getId());
                        s.addBatch();
                        batchCount += 1;
                        if (batchCount % LimitedPower.BATCH_SIZE_CAMPUS == 0) {
                            rowCount += s.executeBatch().length;
                        }
                        System.out.println("rowcount: " + rowCount);
                    }
                    rowCount += s.executeBatch().length;
                    return rowCount;
                });
    }

    @Override
    public Optional<User> getUser(Integer id) {
        String queryGet = "Select id, address, birthday, email, full_name as fullName, password, phone, username\n" +
                "from users where id = :id";

        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryGet);
        query.setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, id))
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("address", StandardBasicTypes.STRING)
                .addScalar("birthday", StandardBasicTypes.DATE)
                .addScalar("email", StandardBasicTypes.STRING)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("password", StandardBasicTypes.STRING)
                .addScalar("phone", StandardBasicTypes.STRING)
                .addScalar("username", StandardBasicTypes.STRING);
        query.setResultTransformer(new AliasToBeanResultTransformer(User.class));
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
