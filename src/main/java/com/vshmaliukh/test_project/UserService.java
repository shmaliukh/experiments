package com.vshmaliukh.test_project;

import com.vshmaliukh.test_project.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;


}
