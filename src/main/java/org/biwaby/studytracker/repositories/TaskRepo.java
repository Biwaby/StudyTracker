package org.biwaby.studytracker.repositories;

import org.biwaby.studytracker.models.DTO.ViewDTO.TaskPresentationDTO;
import org.biwaby.studytracker.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TaskRepo extends PagingAndSortingRepository<Task, Long>, JpaRepository<Task, Long> {
}
