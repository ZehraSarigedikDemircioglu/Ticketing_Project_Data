package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {

    ProjectDTO getByProjectCode(String code);
    List<ProjectDTO> listAllProjects();
    void save(ProjectDTO project);
    void update(ProjectDTO project);
    void delete(String code);
    void complete(String projectCode);

    List<ProjectDTO> listAllProjectDetails();
}
