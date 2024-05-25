package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.Project;
import org.biwaby.studytracker.models.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectTaskRepo extends JpaRepository<ProjectTask, Long> {
    List<ProjectTask> findAllByProject(Project project);
    void deleteAllByProject(Project project);
}
