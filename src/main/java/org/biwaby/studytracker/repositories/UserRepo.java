package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends PagingAndSortingRepository<User, Long>, JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByUsername(String username);
}
