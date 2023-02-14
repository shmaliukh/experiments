package com.vshmaliukh.test_project.repositories;

import com.vshmaliukh.test_project.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String role);

}
