package org.biwaby.studytracker.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "timerNotes")
@Table(name = "timerNotes")
public class TimerNote {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "timerNote_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "timerNotes_seq", name = "timerNotes_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Moscow")
    @Column(name = "date")
    private Date date;

    @Column(name = "duration")
    private String duration;
}
