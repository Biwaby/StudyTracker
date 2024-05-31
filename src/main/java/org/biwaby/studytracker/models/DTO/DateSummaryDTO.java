package org.biwaby.studytracker.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateSummaryDTO {
    String totalTime;
    String topProject;
    String topProjectTotalTime;
    Map<String, List<String>> projects = new HashMap<>();
    String withoutProjectsTotalTime;
}
