package com.managedormitory.repositories;

import com.managedormitory.models.dao.StudentLeft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentLeftRepository extends JpaRepository<StudentLeft, Integer> {
}
