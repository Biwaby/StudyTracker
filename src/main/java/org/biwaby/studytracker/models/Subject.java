package org.biwaby.studytracker.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "subjects")
@Table(name = "subjects")
public class Subject {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "subject_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "subject_seq", name = "subject_seq", allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "course")
    private int course;

    @Column(name = "semester")
    private int semester;
}
