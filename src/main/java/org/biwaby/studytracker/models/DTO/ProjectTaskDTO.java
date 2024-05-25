package org.biwaby.studytracker.models.DTO;

import lombok.Data;

@Data
public class ProjectTaskDTO {
    Long id;
    String title;
    String description;
    boolean completed;
}
