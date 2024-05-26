package org.biwaby.studytracker.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimerRecordDTO {
    Long id;
    String title;
    String startTime;
    String endTime;
    String recordDate;
    ProjectDTO project;
    Set<TagDTO> tags = new HashSet<>();
}
