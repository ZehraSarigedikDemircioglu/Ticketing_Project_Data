package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;

import java.util.List;

public interface TaskService {

    List<TaskDTO> listAllTasks();
    void save(TaskDTO task);
    void update(TaskDTO task);
    void delete(Long id);
    TaskDTO getById(Long id);
    int totalNonCompletedTask(String projectCode);
    int totalCompletedTask(String projectCode);

    void deleteByProject(ProjectDTO project);
    void completeByProject(ProjectDTO project);

    List<TaskDTO> findAllTasksByStatusIsNot(Status status);
    List<TaskDTO> findAllTasksByStatus(Status status);
    List<TaskDTO> listAllNonCompletedByAssignedEmployee(TaskDTO assignedEmployee);
}
