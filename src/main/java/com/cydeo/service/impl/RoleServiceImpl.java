package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.mapper.RoleMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final MapperUtil mapperUtil;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper, MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<RoleDTO> listAllRoles() {

        // Controller called me and requested all RoleDTO, so it can show in the drop-down in the ui
        // I need to make a call to db and get all the roles from table
        // Go to repository find a service which gives me the roles from db

        List<Role> roleList = roleRepository.findAll();

        // to convert Role to RoleDTO, we add a mapper dependency from library, and so mapper package

//         return roleList.stream().map(roleMapper::convertToDTO).collect(Collectors.toList());

        // we put MapperUtil class to convert using gneric, either can be used for the implementation.
        return roleList.stream().map(role -> mapperUtil.convert(role, new RoleDTO())).collect(Collectors.toList()); // 1.WAY
//        return roleList.stream().map(role -> mapperUtil.convert(role, RoleDTO.class)).collect(Collectors.toList()); // 2.WAY
    }

    @Override
    public RoleDTO findById(Long id) {
        return roleMapper.convertToDTO(roleRepository.findById(id).orElseThrow());
    }
}
