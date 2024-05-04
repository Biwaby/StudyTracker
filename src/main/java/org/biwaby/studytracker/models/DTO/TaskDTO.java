package org.biwaby.studytracker.models.DTO;

import lombok.Data;

@Data
public class TaskDTO {

    private Long id;
    private String title;
    private String description;
    private Long subjectId;
    private boolean status;
    private String deadlineDate;
}
