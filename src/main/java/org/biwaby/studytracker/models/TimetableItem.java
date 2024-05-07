package org.biwaby.studytracker.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "timetable")
@Table(name = "timetable")
public class TimetableItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "timetable_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "timetable_seq", name = "timetable_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "classType_id")
    private ClassType classType;

    @Column(name = "visit_status")
    private String visitStatus;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Moscow")
    @Column(name = "date")
    private Date date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Europe/Moscow")
    @Column(name = "begin_time")
    private Date beginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Europe/Moscow")
    @Column(name = "end_time")
    private Date endTime;
}
