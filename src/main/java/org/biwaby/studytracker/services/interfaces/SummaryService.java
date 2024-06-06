package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.dto.DateSummaryDTO;
import org.biwaby.studytracker.models.dto.ProjectSummaryDTO;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface SummaryService {
    DateSummaryDTO getSummary(Date date);
    ProjectSummaryDTO getProjectSummary(Long projectId);
}
