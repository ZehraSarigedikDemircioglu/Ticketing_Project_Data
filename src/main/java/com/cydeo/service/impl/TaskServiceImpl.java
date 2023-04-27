package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
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
    public void update(TaskDTO taskDTO) {

        // Find current user
        Optional<Task> task1 = taskRepository.findById(taskDTO.getId());
        // Map update user dto to entity object
        Task convertedTask = taskMapper.convertToEntity(taskDTO);
        // Set id to the converted object

        if(task1.isPresent()){
            convertedTask.setTaskStatus(taskDTO.getTaskStatus() == null ? task1.get().getTaskStatus() : taskDTO.getTaskStatus());
            convertedTask.setAssignedDate(task1.get().getAssignedDate());
            taskRepository.save(convertedTask);
        }
        // Save the updated user in the db
        taskRepository.save(convertedTask);
    }

    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(); // since this give me optional
        task.setIsDeleted(true);
        taskRepository.save(task);
    }

    @Override
    public TaskDTO getById(Long id) {
        return taskMapper.convertToDTO(taskRepository.findById(id).orElseThrow());
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDTO) {

        Project project = projectMapper.convertToEntity(projectDTO);
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.forEach(task -> delete(task.getId()));
    }

    @Override
    public void completeByProject(ProjectDTO projectDTO) {

        Project project = projectMapper.convertToEntity(projectDTO);
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.stream().map(taskMapper::convertToDTO).forEach(taskDTO -> {
            taskDTO.setTaskStatus(Status.COMPLETE);
            update(taskDTO);
        });

    }

    @Override
    public List<TaskDTO> findAllTasksByStatusIsNot(Status status) {
        return null;
    }

    @Override
    public List<TaskDTO> findAllTasksByStatus(Status status) {
        return null;
    }
}
