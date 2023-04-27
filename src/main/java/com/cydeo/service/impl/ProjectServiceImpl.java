package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        return projectMapper.convertToDTO(projectRepository.findByProjectCode(code));
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        return projectRepository.findAll(Sort.by("projectCode")).stream().map(projectMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO project) {

        project.setProjectStatus(Status.OPEN);

        projectRepository.save(projectMapper.convertToEntity(project));

    }

    @Override
    public void update(ProjectDTO project) {

        Project project1 = projectRepository.findByProjectCode(project.getProjectCode());
        Project convertedProject = projectMapper.convertToEntity(project);
        convertedProject.setId(project1.getId());
        convertedProject.setProjectStatus(project1.getProjectStatus());
        projectRepository.save(convertedProject);

    }

    @Override
    public void delete(String code) {

        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);

        project.setProjectCode(project.getProjectCode() + "-" + project.getId()); // depend on the business logic, we use combining id
        // SP00-1, so i can use after deleted, able to use again SP00 as same project code
        projectRepository.save(project);

        taskService.deleteByProject(projectMapper.convertToDTO(project)); // delete all tasks related to deleted project

    }

    @Override
    public void complete(String projectCode) {

        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);

        taskService.completeByProject(projectMapper.convertToDTO(project));
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        // DB, give me all projects assigned to manager login in the system

        UserDTO currentUserDTO = userService.findByUserName("harold@manager.com"); // get one of manager from db
        User user = userMapper.convertToEntity(currentUserDTO);

        List<Project> list = projectRepository.findByAssignedManager(user);

        // since there are completeTaskCounts and unfinishedTaskCounts fields in ProjectDTO,
        // i need to convert to DTO first, set these two fields.

        return list.stream().map(project -> {

            ProjectDTO obj = projectMapper.convertToDTO(project);
            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
            return obj;
        }).collect(Collectors.toList());
    }
}
