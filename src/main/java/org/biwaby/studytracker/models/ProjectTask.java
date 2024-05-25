package org.biwaby.studytracker.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "project_tasks")
@Table(name = "project_tasks")
public class ProjectTask {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "project_task_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "project_task_id_seq", name = "project_task_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "project_id")
    private Project project;

    @Lob
    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "completed")
    boolean completed;
}
