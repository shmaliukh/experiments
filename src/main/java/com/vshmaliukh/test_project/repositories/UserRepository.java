package com.vshmaliukh.test_project.repositories;

import com.vshmaliukh.test_project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

}