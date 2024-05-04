package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task, Long> {
}
