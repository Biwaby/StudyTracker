package org.biwaby.studytracker.models.DTO;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TimerRecordDTO {
    Long id;
    String title;
    String startTime;
    String endTime;
    String recordDate;
    Set<TagDTO> tags = new HashSet<>();
}
