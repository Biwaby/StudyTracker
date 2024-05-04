package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.Classmate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassmateRepo extends JpaRepository<Classmate, Long> {
}
