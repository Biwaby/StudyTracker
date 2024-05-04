package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.TimetableItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableItemRepo extends JpaRepository<TimetableItem, Long> {
}
