package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepo extends JpaRepository<Classroom, Long> {
}
