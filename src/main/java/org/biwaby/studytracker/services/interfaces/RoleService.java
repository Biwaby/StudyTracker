package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    List<Role> getAllRoles();
    Role addRole(Role role);
    void deleteRole(Long id);
}
