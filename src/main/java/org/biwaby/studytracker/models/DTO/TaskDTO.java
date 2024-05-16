package org.biwaby.studytracker.models.DTO;

import lombok.Data;
import org.biwaby.studytracker.models.Subject;

@Data
public class TaskDTO {

    private Long id;
    private String title;
    private String description;
    private Long subjectId;
    private boolean completed;
    private String completionDate;
    private String deadlineDate;
}
