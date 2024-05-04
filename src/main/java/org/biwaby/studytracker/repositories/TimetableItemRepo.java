package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepo extends JpaRepository<Timetable, Long> {
}
