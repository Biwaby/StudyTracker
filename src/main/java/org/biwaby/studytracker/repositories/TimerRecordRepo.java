package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.TimerRecord;
import org.biwaby.studytracker.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface TimerRecordRepo extends PagingAndSortingRepository<TimerRecord, Long>, JpaRepository<TimerRecord, Long> {
    Page<TimerRecord> findAllByUser(Pageable pageable, User user);
    void deleteAllByUser(User user);
}
