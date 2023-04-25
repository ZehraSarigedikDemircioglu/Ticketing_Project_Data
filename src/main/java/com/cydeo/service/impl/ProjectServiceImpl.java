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

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
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
        projectRepository.save(project);

    }

    @Override
    public void complete(String projectCode) {

        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        // DB, give me all projects assigned to manager login in the system

        UserDTO currentUserDTO = userService.findByUserName("samantha@manager.com"); // get one of manager
        User user = userMapper.convertToEntity(currentUserDTO);

        List<Project> list = projectRepository.findByAssignedManager(user);

        // since there are completeTaskCounts and unfinishedTaskCounts fields in ProjectDTO,
        // i need to convert to DTO first, set these two fields.

        return list.stream().map(project -> {

            ProjectDTO obj = projectMapper.convertToDTO(project);
            obj.setUnfinishedTaskCounts(3);
            obj.setCompleteTaskCounts(5);
            return obj;
        }).collect(Collectors.toList());
    }
}
