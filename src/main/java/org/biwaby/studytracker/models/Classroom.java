package org.biwaby.studytracker.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "classrooms")
@Table(name = "classrooms")
public class Classroom {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "classroom_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "classroom_seq", name = "classroom_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @Column(name = "number")
    private String number;
}
