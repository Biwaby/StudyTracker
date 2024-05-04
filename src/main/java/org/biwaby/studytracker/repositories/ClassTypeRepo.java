package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.ClassType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassTypeRepo extends JpaRepository<ClassType, Long> {
}
