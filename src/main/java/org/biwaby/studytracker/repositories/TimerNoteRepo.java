package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.TimerNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimerNoteRepo extends JpaRepository<TimerNote, Long> {
    Optional<TimerNote> findBySubjectId(Long id);
}
