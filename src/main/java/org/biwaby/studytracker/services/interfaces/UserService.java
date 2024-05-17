package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.DTO.UserDTO;
import org.biwaby.studytracker.models.DTO.UserRegistrationDTO;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    Page<User> getAllUsers(int page);
    User getUserByAuth();
    UserDTO registerUser(UserRegistrationDTO dto);
    UserDTO grantRole(Long userId, Long roleId);
    UserDTO revokeRole(Long userId, Long roleId);
    void deleteUser(Long userId);
}
