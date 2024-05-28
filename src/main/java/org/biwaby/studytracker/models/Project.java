package org.biwaby.studytracker.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "projects")
@Table(name = "projects")
public class Project {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "project_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "project_id_seq", name = "project_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Lob
    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ProjectTask> tasks = new ArrayList<>();
}
