package org.biwaby.studytracker.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "buildings")
@Table(name = "buildings")
public class Building {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "building_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "building_seq", name = "building_seq", allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title;
}
