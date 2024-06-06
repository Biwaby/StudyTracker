package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.dto.UserDTO;
import org.biwaby.studytracker.models.dto.UserRegistrationDTO;
import org.biwaby.studytracker.models.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Page<User> getAllUsers(int page);
    User getUserByAuth();
    UserDTO registerUser(UserRegistrationDTO dto);
    UserDTO grantRole(Long userId, Long roleId);
    UserDTO revokeRole(Long userId, Long roleId);
    void deleteUser();
    void deleteUserById(Long userId);
}
