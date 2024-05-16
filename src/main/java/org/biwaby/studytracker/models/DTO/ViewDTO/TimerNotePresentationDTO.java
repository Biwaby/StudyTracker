package org.biwaby.studytracker.models.DTO.ViewDTO;

import lombok.Data;
import org.biwaby.studytracker.models.Subject;

import java.util.Date;

@Data
public class TimerNotePresentationDTO {
    private Long id;
    private String user;
    private Subject subject;
    private String date;
    private String duration;
}
