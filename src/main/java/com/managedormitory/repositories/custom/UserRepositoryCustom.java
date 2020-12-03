package com.managedormitory.repositories.custom;

import com.managedormitory.models.dao.User;
import com.managedormitory.models.dto.UserUpdate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryCustom {

    int updateUser(Integer id, UserUpdate userUpdate);

    int updateUserWithoutPassword(Integer id, UserUpdate userUpdate);

    int updateCampus(UserUpdate userUpdate);

    Optional<User> getUser(Integer id);

    int deleteUser(Integer id);

    int deleteCampusForDeleteUser(Integer campusId);
}
