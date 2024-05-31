package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.Project;
import org.biwaby.studytracker.models.TimerRecord;
import org.biwaby.studytracker.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;


public interface TimerRecordRepo extends PagingAndSortingRepository<TimerRecord, Long>, JpaRepository<TimerRecord, Long> {
    Page<TimerRecord> findAllByUser(Pageable pageable, User user);
    List<TimerRecord> findAllByUser(User user);
    List<TimerRecord> findAllByProject(Project project);
    List<TimerRecord> findAllByRecordDateAndUser(Date date, User user);
    void deleteAllByUser(User user);
}
