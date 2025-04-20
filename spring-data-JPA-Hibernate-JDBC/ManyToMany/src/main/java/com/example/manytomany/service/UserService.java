package com.example.manytomany.service;

import com.example.manytomany.entities.Role;
import com.example.manytomany.entities.User;

public interface UserService {
    User addNewUser(User user);
    Role addNewRole(Role role);
    User findUserByName(String name);
    Role findRoleByName(String name);
    void addRoleToUser(String userName, String roleName);

    User authenticate(String userName, String Password);
}
