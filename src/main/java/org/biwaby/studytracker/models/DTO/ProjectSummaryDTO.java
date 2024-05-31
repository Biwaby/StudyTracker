package org.biwaby.studytracker.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSummaryDTO {
    String totalTime;
    String topTask;
    String topTaskTotalTime;
    Map<String, String> tasks = new HashMap<>();
    String withoutTasksTotalTime;
}
