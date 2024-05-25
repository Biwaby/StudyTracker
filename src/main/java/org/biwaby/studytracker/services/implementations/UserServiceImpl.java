package org.biwaby.studytracker.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.*;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.RoleNotFoundException;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.UserNotFoundException;
import org.biwaby.studytracker.models.DTO.UserDTO;
import org.biwaby.studytracker.models.DTO.UserRegistrationDTO;
import org.biwaby.studytracker.models.Project;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.Tag;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.*;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    private final TagRepo tagRepo;
    private final ProjectRepo projectRepo;
    private final ProjectTaskRepo projectTaskRepo;
    private final TimerRecordRepo timerRecordRepo;

    @Override
    public Page<User> getAllUsers(int page) {
        return userRepo.findAll(PageRequest.of(page, 5));
    }

    @Override
    public User getUserByAuth() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public UserDTO registerUser(UserRegistrationDTO dto) {
        if (dto == null) {
            throw new IncorrectUserException();
        }
        if (dto.getUsername() == null || dto.getPassword() == null) {
            throw new IncorrectUserException();
        }
        if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User user = new User();
        Role role = roleRepo.findByAuthority("USER").get();

        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);
        user.setRoles(new HashSet<>(List.of(role)));
        userRepo.save(user);
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.isEnabled(),
                user.getRoles()
        );
    }

    @Override
    public UserDTO grantRole(Long userId, Long roleId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        Optional<Role> optionalRole = roleRepo.findById(roleId);
        if (optionalUser.isPresent()) {
            if (optionalRole.isPresent()) {
                User user = optionalUser.get();
                Role role = optionalRole.get();
                user.getRoles().add(role);

                userRepo.save(user);
                return new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.isEnabled(),
                        user.getRoles()
                );
            }
            else {
                throw new RoleNotFoundException();
            }
        }
        else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public UserDTO revokeRole(Long userId, Long roleId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        Optional<Role> optionalRole = roleRepo.findById(roleId);
        if (optionalUser.isPresent()) {
            if (optionalRole.isPresent()) {
                User user = optionalUser.get();
                Role role = optionalRole.get();

                if (!user.getRoles().contains(role)) {
                    throw new UserDoesNotHaveRoleException();
                }

                user.getRoles().remove(role);
                userRepo.save(user);
                return new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.isEnabled(),
                        user.getRoles()
                );
            }
            else {
                throw new RoleNotFoundException();
            }
        }
        else {
            throw new UserNotFoundException();
        }
    }

    @Override
    @Transactional
    public void deleteUser() {
        User user = getUserByAuth();

        tagRepo.deleteAllByUser(user);
        List<Project> projects = projectRepo.findAllByUser(user);
        for (Project project : projects) {
            if (!project.getTasks().isEmpty()) {
                projectTaskRepo.deleteAllByProject(project);
            }
        }
        projectRepo.deleteAllByUser(user);
        timerRecordRepo.deleteAllByUser(user);
        userRepo.delete(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);

        tagRepo.deleteAllByUser(user);
        List<Project> projects = projectRepo.findAllByUser(user);
        for (Project project : projects) {
            if (!project.getTasks().isEmpty()) {
                projectTaskRepo.deleteAllByProject(project);
            }
        }
        projectRepo.deleteAllByUser(user);
        timerRecordRepo.deleteAllByUser(user);
        userRepo.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
