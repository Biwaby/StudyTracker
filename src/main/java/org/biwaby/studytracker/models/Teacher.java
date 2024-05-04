package org.biwaby.studytracker.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "teachers")
@Table(name = "teachers")
public class Teacher {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "teacher_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "teacher_seq", name = "teacher_seq", allocationSize = 1)
    private Long id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String  firstName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "email")
    private String email;
}
