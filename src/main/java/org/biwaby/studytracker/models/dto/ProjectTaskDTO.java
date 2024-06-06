package org.biwaby.studytracker.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTaskDTO {
    Long id;
    String title;
    String description;
    boolean completed;
}
