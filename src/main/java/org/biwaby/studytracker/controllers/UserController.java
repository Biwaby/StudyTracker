package org.biwaby.studytracker.controllers;

import lombok.AllArgsConstructor;
import org.biwaby.studytracker.models.DTO.UserDTO;
import org.biwaby.studytracker.models.DTO.UserRegistrationDTO;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<Page<User>> getAllUsers(@RequestParam int page) {
        return ResponseEntity.ok(userService.getAllUsers(page));
    }

    @PostMapping("/register")
    ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationDTO dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }

    @PutMapping("/grant")
    ResponseEntity<UserDTO> grantRole(@RequestParam Long userId, @RequestParam Long roleId) {
        return ResponseEntity.ok(userService.grantRole(userId, roleId));
    }

    @PutMapping("/revoke")
    ResponseEntity<UserDTO> revokeRole(@RequestParam Long userId, @RequestParam Long roleId) {
        return ResponseEntity.ok(userService.revokeRole(userId, roleId));
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
