package org.biwaby.studytracker.models;

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

    @OneToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "date")
    private Date date;

    @Column(name = "duration")
    private String duration;
}
