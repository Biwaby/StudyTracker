package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepo extends JpaRepository<Subject, Long> {
}
