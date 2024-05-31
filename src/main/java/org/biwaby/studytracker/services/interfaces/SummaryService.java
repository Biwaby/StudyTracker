package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.DTO.DateSummaryDTO;
import org.biwaby.studytracker.models.DTO.ProjectSummaryDTO;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface SummaryService {
    DateSummaryDTO getDateSummary(Date date);
    ProjectSummaryDTO getProjectSummary(Long projectId);
}
