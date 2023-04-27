package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {

        List<User> userList = userRepository.findAll(Sort.by("firstName"));
        return userList.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        return userMapper.convertToDTO(userRepository.findByUserName(username));
    }

    @Override
    public void save(UserDTO user) {
        userRepository.save(userMapper.convertToEntity(user));
    }

//    @Override // since this is hard deletion, we do not use this method
//    public void deleteByUserName(String username) {
//        userRepository.deleteByUserName(username);
//    }

    @Override
    public UserDTO update(UserDTO user) {

        // Find current user
        User user1 = userRepository.findByUserName(user.getUserName()); // has id
        // Map update user dto to entity object
        User convertedUser = userMapper.convertToEntity(user);
        // Set id to the converted object
        convertedUser.setId(user1.getId());
        // Save the updated user in the db
        userRepository.save(convertedUser);
        return findByUserName(user.getUserName());

    }

    @Override
    public void delete(String username) {

        // go to db and get that user with username
        // change the isDeleted field from base entity to change as true
        // save the object in the db

        User user = userRepository.findByUserName(username);

        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" + user.getId());  // harold@manager.com-2
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {

        return userRepository.findByRoleDescriptionIgnoreCase(role).stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    private boolean checkIfUserCanBeDeleted(User user) {
        switch (user.getRole().getDescription()) {
            case "Manager":
                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDTO(user));
                return projectDTOList.size() == 0;
            case "Employee":
                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDTO(user));
                return taskDTOList.size() == 0;
            default:
                return true;
        }
    }
}
