package com.cydeo.service;

import com.cydeo.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username);
    void save(UserDTO user);
    void deleteByUserName(String username);
    UserDTO update(UserDTO user);
    void delete(String username); // added this method later to make sure we want to save "deleted username" in the db.
    // to prevent confliction, i created seperate this delete method instead of using deleteByUserName(String username)
    List<UserDTO> listAllByRole(String role);
}
