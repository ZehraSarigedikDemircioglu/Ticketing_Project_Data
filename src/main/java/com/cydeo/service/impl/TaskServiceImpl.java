package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void save(TaskDTO task) {

        task.setTaskStatus(Status.OPEN);
        task.setAssignedDate(LocalDate.now());
        taskRepository.save(taskMapper.convertToEntity(task));
    }

    @Override
    public void update(TaskDTO task) {

//        // Find current user
//        Task task1 = taskRepository.findById(task.getId());
//        // Map update user dto to entity object
//        Task convertedTask = taskMapper.convertToEntity(task);
//        // Set id to the converted object
//        convertedTask.setId(task1.getId());
//        // Save the updated user in the db
//        taskRepository.save(convertedTask);
//        return findByUserName(task.getId());

    }

    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(); // since this give me optional
        task.setIsDeleted(true);
        taskRepository.save(task);
    }

    @Override
    public TaskDTO getById(Long id) {
        return null;
    }

}
