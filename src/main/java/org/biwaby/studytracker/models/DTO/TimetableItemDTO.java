package org.biwaby.studytracker.models.DTO;

import lombok.Data;


@Data
public class TimetableItemDTO {

    private Long id;
    private Long subjectId;
    private Long teacherId;
    private Long classTypeId;
    private boolean visitStatus;
    private Long classroomId;
    private String date;
    private String beginTime;
    private String endTime;
}
