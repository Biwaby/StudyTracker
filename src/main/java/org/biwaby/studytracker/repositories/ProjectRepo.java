package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.Project;
import org.biwaby.studytracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    List<Project> findAllByUser(User user);
    void deleteAllByUser(User user);
}
