package com.example.manytomany.service;

import com.example.manytomany.entities.Role;
import com.example.manytomany.entities.User;
import com.example.manytomany.repositories.RoleRepository;
import com.example.manytomany.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;


    @Override
    public User addNewUser(User user) {
        user.setUserId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    @Override
    public Role addNewRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByRoleName(name);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        User user = findUserByName(userName);
        Role role = findRoleByName(roleName);
        if(user.getRoles() != null){
            user.getRoles().add(role);
            role.getUsers().add(user);
        }
        //userRepository.save(user);
    }

    @Override
    public User authenticate(String userName, String Password) {
        User user = findUserByName(userName);
        if(user == null){
            throw new RuntimeException("Bad Credentials");
        }
        if(user.getPassword().equals(Password)){
            return user;
        }
        throw new RuntimeException("Bad Credentials");
    }
}
