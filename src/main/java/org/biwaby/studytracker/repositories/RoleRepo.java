package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthority(String authority);
}
