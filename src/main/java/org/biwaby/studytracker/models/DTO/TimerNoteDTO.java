package org.biwaby.studytracker.models.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class TimerNoteDTO {

    private Long id;
    private Long subjectId;
    private Date date;
    private String duration;
}
