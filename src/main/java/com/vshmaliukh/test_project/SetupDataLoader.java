package com.vshmaliukh.test_project;

import com.vshmaliukh.test_project.entities.Privilege;
import com.vshmaliukh.test_project.entities.Role;
import com.vshmaliukh.test_project.entities.User;
import com.vshmaliukh.test_project.repositories.PrivilegeRepository;
import com.vshmaliukh.test_project.repositories.RoleRepository;
import com.vshmaliukh.test_project.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

//    boolean alreadySetup = false;

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PrivilegeRepository privilegeRepository;

//    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (false)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Collections.singletonList(readPrivilege));

        saveDefaultAdmin();
        saveDefaultUser();

//        alreadySetup = true;
    }

    private void saveDefaultAdmin() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        User user = new User();
        user.setName("admin");
        user.setPassword(passwordEncoder.encode("000"));
        user.setEmail("admin@mail.com");
        user.setRoles(Collections.singletonList(adminRole));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private void saveDefaultUser() {
        Role adminRole = roleRepository.findByName("ROLE_USER");
        User user = new User();
        user.setName("user");
        user.setPassword(passwordEncoder.encode("000"));
        user.setEmail("user@mail.com");
        user.setRoles(Collections.singletonList(adminRole));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}