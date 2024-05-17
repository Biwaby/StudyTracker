package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.RoleNotFoundException;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.services.interfaces.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    @Override
    public Role addRole(Role role) {
        return roleRepo.saveAndFlush(role);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepo.findById(id).orElseThrow(RoleNotFoundException::new);
        roleRepo.delete(role);
    }
}
