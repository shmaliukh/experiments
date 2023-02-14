package com.vshmaliukh.test_project;

import com.vshmaliukh.test_project.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

}
