package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.TimerNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimerNoteRepo extends JpaRepository<TimerNote, Long> {
}
