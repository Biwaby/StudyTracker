package org.biwaby.studytracker.models.DTO.ViewDTO;

import lombok.Data;
import org.biwaby.studytracker.models.Subject;

@Data
public class TaskPresentationDTO {

    private Long id;
    private String user;
    private String title;
    private String description;
    private Subject subject;
    private boolean completed;
    private String completionDate;
    private String deadlineDate;
}
