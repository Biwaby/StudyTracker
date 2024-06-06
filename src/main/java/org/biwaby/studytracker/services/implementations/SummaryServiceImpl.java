package org.biwaby.studytracker.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.notFoundExceptions.ProjectNotFoundException;
import org.biwaby.studytracker.models.*;
import org.biwaby.studytracker.models.dto.DateSummaryDTO;
import org.biwaby.studytracker.models.dto.ProjectSummaryDTO;
import org.biwaby.studytracker.repositories.ProjectRepo;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.repositories.TimerRecordRepo;
import org.biwaby.studytracker.services.interfaces.SummaryService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final RoleRepo roleRepo;
    private final UserService userService;
    private final TimerRecordRepo timerRecordRepo;
    private final ProjectRepo projectRepo;

    @Override
    @Transactional
    public DateSummaryDTO getSummary(Date date) {
        List<TimerRecord> recordList = timerRecordRepo.findAllByRecordDateAndUser(date, userService.getUserByAuth());
        DateSummaryDTO dto = new DateSummaryDTO();

        // Calc total time
        Date totalTime = new Date();
        long duration = recordList.stream().mapToLong(record -> record.getEndTime().getTime() - record.getStartTime().getTime()).sum();
        totalTime.setTime(duration - 10800000);
        dto.setTotalTime(new SimpleDateFormat("HH:mm:ss").format(totalTime));

        // Find top project
        Map<Project, Long> countMap = recordList.stream()
                .filter(record -> record.getProject() != null)
                .collect(Collectors.groupingBy(TimerRecord::getProject, Collectors.counting()));
        Project topProject = countMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Calc top project total time
        Date topProjectTime = new Date();
        long topProjectDur = recordList.stream()
                .filter(record -> record.getProject() != null && record.getProject().equals(topProject))
                .mapToLong(record -> record.getEndTime().getTime() - record.getStartTime().getTime()).sum();
        if (topProjectDur != 0) {
            topProjectTime.setTime(topProjectDur - 10800000);
            dto.setTopProject(topProject.getTitle());
            dto.setTopProjectTotalTime(new SimpleDateFormat("HH:mm:ss").format(topProjectTime));
        }

        // Find all tasks for each project and calc total time
        Map<Project, Long> projectTime = new HashMap<>();
        Map<ProjectTask, Long> projectTaskTime = new HashMap<>();

        recordList.forEach(record -> {
            if (record.getProject() != null) {
                Project project = record.getProject();
                long projectTotalTime = record.getEndTime().getTime() - record.getStartTime().getTime();
                projectTime.merge(project, projectTotalTime, Long::sum);

                if (record.getProjectTask() != null) {
                    ProjectTask task = record.getProjectTask();
                    long taskTotalTime = record.getEndTime().getTime() - record.getStartTime().getTime();
                    projectTaskTime.merge(task, taskTotalTime, Long::sum);
                }
            }
        });

        projectTime.forEach((project, projectTotalTime) -> {
            List<String> tasksList = projectTaskTime.entrySet().stream()
                    .filter(entry -> entry.getKey().getProject().equals(project))
                    .map(entry -> entry.getKey().getTitle() + ": " + new SimpleDateFormat("HH:mm:ss").format(entry.getValue() - 10800000))
                    .toList();

            dto.getProjects().put(
                    project.getTitle() + ": " + new SimpleDateFormat("HH:mm:ss").format(projectTotalTime - 10800000),
                    tasksList
            );
        });

        // Calc total time without projects
        Date withoutProjectTime = new Date();
        long withoutProjectDur = recordList.stream()
                .collect(Collectors.partitioningBy(record -> record.getProject() == null))
                .get(true).stream()
                .mapToLong(record -> record.getEndTime().getTime() - record.getStartTime().getTime()).sum();
        withoutProjectTime.setTime(withoutProjectDur - 10800000);
        dto.setWithoutProjectsTotalTime(new SimpleDateFormat("HH:mm:ss").format(withoutProjectTime));

        return dto;
    }

    @Override
    @Transactional
    public ProjectSummaryDTO getProjectSummary(Long projectId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        Project project = projectRepo.findById(projectId).orElseThrow(ProjectNotFoundException::new);

        if (!user.getId().equals(project.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        List<TimerRecord> recordList = timerRecordRepo.findAllByProject(project);
        ProjectSummaryDTO dto = new ProjectSummaryDTO();

        // Calc project total time
        Date totalTime = new Date();
        long duration = recordList.stream()
                .mapToLong(record -> record.getEndTime().getTime() - record.getStartTime().getTime()).sum();
        totalTime.setTime(duration - 10800000);
        dto.setTotalTime(new SimpleDateFormat("HH:mm:ss").format(totalTime));

        // Find top task
        Map<ProjectTask, Long> taskTimeMap = recordList.stream()
                .filter(record -> record.getProjectTask() != null)
                .collect(
                        Collectors.groupingBy(
                                TimerRecord::getProjectTask,
                                Collectors.summingLong(record -> record.getEndTime().getTime() - record.getStartTime().getTime())
                        )
                );

        ProjectTask topTask = taskTimeMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Calc top task total time
        Date topTaskTime = new Date();
        long topTaskDur = taskTimeMap.getOrDefault(topTask, 0L);
        if (topTaskDur != 0) {
            topTaskTime.setTime(topTaskDur - 10800000);
            dto.setTopTask(topTask.getTitle());
            dto.setTopTaskTotalTime(new SimpleDateFormat("HH:mm:ss").format(topTaskTime));
        }

        // Find all tasks and calc total time
        for (Map.Entry<ProjectTask, Long> entry : taskTimeMap.entrySet()) {
            Date totalTaskTime = new Date();
            totalTaskTime.setTime(entry.getValue() - 10800000);

            dto.getTasks().put(
                    entry.getKey().getTitle(),
                    new SimpleDateFormat("HH:mm:ss").format(totalTaskTime)
            );
        }

        // Calc total time without tasks
        Date withoutTaskTime = new Date();
        long withoutTaskDur = recordList.stream()
                .collect(Collectors.partitioningBy(record -> record.getProjectTask() == null))
                .get(true).stream()
                .mapToLong(record -> record.getEndTime().getTime() - record.getStartTime().getTime()).sum();
        withoutTaskTime.setTime(withoutTaskDur - 10800000);
        dto.setWithoutTasksTotalTime(new SimpleDateFormat("HH:mm:ss").format(withoutTaskTime));

        return dto;
    }
}
