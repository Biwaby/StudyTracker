package org.biwaby.studytracker.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "timer_records")
@Table(name = "timer_records")
public class TimerRecord {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "timer_record_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "timer_record_id_seq", name = "timer_record_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Europe/Moscow")
    private Date startTime;

    @Column(name = "end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Europe/Moscow")
    private Date endTime;

    @Column(name = "record_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Moscow")
    private Date recordDate;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @Nullable
    private Project project;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "timer_records_tags",
            joinColumns = @JoinColumn(name = "timer_record_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();
}
