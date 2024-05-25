package org.biwaby.studytracker.controllers;

import lombok.AllArgsConstructor;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.services.interfaces.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/users/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping("/add")
    ResponseEntity<Role> addRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.addRole(role));
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteRole(@RequestParam Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok().build();
    }
}
