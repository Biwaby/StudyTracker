package org.biwaby.studytracker.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "classmates")
@Table(name = "classmates")
public class Classmate {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "classmate_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "classmate_seq", name = "classmate_seq", allocationSize = 1)
    private Long id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "patronymic")
    private String patronymic;
}
