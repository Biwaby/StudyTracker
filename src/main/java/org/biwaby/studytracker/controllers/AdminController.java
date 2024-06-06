package org.biwaby.studytracker.controllers;

import lombok.AllArgsConstructor;
import org.biwaby.studytracker.models.dto.UserDTO;
import org.biwaby.studytracker.models.TimerRecord;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.services.interfaces.TimerRecordService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final TimerRecordService timerRecordService;

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam int page) {
        return ResponseEntity.ok(userService.getAllUsers(page));
    }

    @PutMapping("/users/grant")
    public ResponseEntity<UserDTO> grantRole(@RequestParam Long userId, @RequestParam Long roleId) {
        return ResponseEntity.ok(userService.grantRole(userId, roleId));
    }

    @PutMapping("/users/revoke")
    public ResponseEntity<UserDTO> revokeRole(@RequestParam Long userId, @RequestParam Long roleId) {
        return ResponseEntity.ok(userService.revokeRole(userId, roleId));
    }

    @DeleteMapping("/users/delete")
    public ResponseEntity<Void> deleteUserById(@RequestParam Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }

    // Timer admin controller
    @GetMapping("/timer")
    public ResponseEntity<Page<TimerRecord>> getAllRecordsByUser(@RequestParam String username, @RequestParam int page) {
        return ResponseEntity.ok(timerRecordService.getAllRecordsByUser(username, page));
    }
}
