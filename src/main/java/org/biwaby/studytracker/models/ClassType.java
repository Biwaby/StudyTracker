package org.biwaby.studytracker.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "class_types")
@Table(name = "class_types")
public class ClassType {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "classType_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "classType_seq", name = "classType_seq", allocationSize = 1)
    private Long id;

    @Column(name = "type")
    private String type;
}
