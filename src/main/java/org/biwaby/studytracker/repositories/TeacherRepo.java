package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepo extends JpaRepository<Teacher, Long> {
}
