package org.biwaby.studytracker.models.DTO.ViewDTO;

import lombok.Data;
import org.biwaby.studytracker.models.ClassType;
import org.biwaby.studytracker.models.Classroom;
import org.biwaby.studytracker.models.Subject;
import org.biwaby.studytracker.models.Teacher;

@Data
public class TimetableDTO {
    private Long id;
    private String user;
    private Subject subject;
    private Teacher teacher;
    private ClassType classtype;
    private String visitStatus;
    private Classroom classroom;
    private String date;
    private String beginTime;
    private String endTime;
}
