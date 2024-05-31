package org.biwaby.studytracker.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.ProjectNotFoundException;
import org.biwaby.studytracker.models.*;
import org.biwaby.studytracker.models.DTO.DateSummaryDTO;
import org.biwaby.studytracker.models.DTO.ProjectSummaryDTO;
import org.biwaby.studytracker.repositories.ProjectRepo;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.repositories.TimerRecordRepo;
import org.biwaby.studytracker.services.interfaces.SummaryService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final RoleRepo roleRepo;
    private final UserService userService;
    private final TimerRecordRepo timerRecordRepo;
    private final ProjectRepo projectRepo;

    @Override
    @Transactional
    public DateSummaryDTO getDateSummary(Date date) {
        List<TimerRecord> recordList = timerRecordRepo.findAllByRecordDateAndUser(date, userService.getUserByAuth());
        DateSummaryDTO dto = new DateSummaryDTO();

        // Total time
        Date totalTime = new Date();
        long duration = 0;
        for (TimerRecord record : recordList) {
            duration += (record.getEndTime().getTime()) - (record.getStartTime().getTime());
        }
        totalTime.setTime(duration - 10800000);
        dto.setTotalTime(new SimpleDateFormat("HH:mm:ss").format(totalTime));

        // Top project
        Map<Project, Integer> countMap = new HashMap<>();
        for (TimerRecord record : recordList) {
            Project project = record.getProject();
            if (project != null) {
                countMap.put(project, countMap.getOrDefault(project, 0) + 1);
            }
        }
        Project topProject = null;
        int maxCount = 0;
        for (Map.Entry<Project, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                topProject = entry.getKey();
            }
        }

        // Top project total time
        Date topProjectTime = new Date();
        long topProjectDur = 0;
        for (TimerRecord record : recordList) {
            if (record.getProject() != null && record.getProject().equals(topProject)) {
                topProjectDur += record.getEndTime().getTime() - record.getStartTime().getTime();
            }
        }
        if (topProjectDur == 0) {
            dto.setTopProject("-");
        }
        else {
            topProjectTime.setTime(topProjectDur - 10800000);
            dto.setTopProject(topProject.getTitle());
            dto.setTopProjectTotalTime(new SimpleDateFormat("HH:mm:ss").format(topProjectTime));
        }

        // Project-project time list with tasks + without projects total time
        Date withoutProjectTime = new Date();
        long withoutProjectDur = 0;
        Map<Project, Long> projectTime = new HashMap<>();
        Map<ProjectTask, Long> projectTaskTime = new HashMap<>();
        for (TimerRecord record : recordList) {
            if (record.getProject() != null) {
                Project project = record.getProject();
                long projectTotalTime = record.getEndTime().getTime() - record.getStartTime().getTime();

                if (projectTime.containsKey(project)) {
                    long sum = projectTime.get(project);
                    projectTime.put(project, sum + projectTotalTime);
                }
                else {
                    projectTime.put(project, projectTotalTime);
                }

                if (record.getProjectTask() != null) {
                    ProjectTask task = record.getProjectTask();
                    long taskTotalTime = record.getEndTime().getTime() - record.getStartTime().getTime();

                    if (projectTaskTime.containsKey(task)) {
                        long sum = projectTaskTime.get(task);
                        projectTaskTime.put(task, sum + taskTotalTime);
                    }
                    else {
                        projectTaskTime.put(task, taskTotalTime);
                    }
                }
            }
            else {
                withoutProjectDur += (record.getEndTime().getTime()) - (record.getStartTime().getTime());
            }
        }

        for (Map.Entry<Project, Long> entry : projectTime.entrySet()) {
            List<String> tasksList = new ArrayList<>();
            Date totalProjectTime = new Date();
            totalProjectTime.setTime(entry.getValue() - 10800000);

            for (Map.Entry<ProjectTask, Long> taskEntry : projectTaskTime.entrySet()) {
                if (taskEntry.getKey().getProject().equals(entry.getKey())) {
                    Date totalTaskTime = new Date();
                    totalTaskTime.setTime(taskEntry.getValue() - 10800000);
                    tasksList.add(taskEntry.getKey().getTitle() + ": " + new SimpleDateFormat("HH:mm:ss").format(totalTaskTime));
                }
            }
            dto.getProjects().put(
                    entry.getKey().getTitle() + ": " + new SimpleDateFormat("HH:mm:ss").format(totalProjectTime),
                    tasksList
            );
        }

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

        // Total time
        Date totalTime = new Date();
        long duration = 0;
        for (TimerRecord record : recordList) {
            duration += (record.getEndTime().getTime()) - (record.getStartTime().getTime());
        }
        totalTime.setTime(duration - 10800000);
        dto.setTotalTime(new SimpleDateFormat("HH:mm:ss").format(totalTime));

        // Top task
        Map<ProjectTask, Integer> countMap = new HashMap<>();
        for (TimerRecord record : recordList) {
            ProjectTask task = record.getProjectTask();
            if (task != null) {
                countMap.put(task, countMap.getOrDefault(task, 0) + 1);
            }
        }
        ProjectTask topTask = null;
        int maxCount = 0;
        for (Map.Entry<ProjectTask, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                topTask = entry.getKey();
            }
        }

        // Top task total time
        Date topTaskTime = new Date();
        long topTaskDur = 0;
        for (TimerRecord record : recordList) {
            if (record.getProjectTask() != null && record.getProjectTask().equals(topTask)) {
                topTaskDur += record.getEndTime().getTime() - record.getStartTime().getTime();
            }
        }
        if (topTaskDur == 0) {
            dto.setTopTask("-");
        }
        else {
            topTaskTime.setTime(topTaskDur - 10800000);
            dto.setTopTask(topTask.getTitle());
            dto.setTopTaskTotalTime(new SimpleDateFormat("HH:mm:ss").format(topTaskTime));
        }

        // Task - total time list + with tasks total time
        Date withoutTaskTime = new Date();
        long withoutTaskDur = 0;
        Map<ProjectTask, Long> projectTaskLongMap = new HashMap<>();
        for (TimerRecord record : recordList) {
            if (record.getProjectTask() != null) {
                ProjectTask task = record.getProjectTask();
                long taskTotalTime = record.getEndTime().getTime() - record.getStartTime().getTime();

                if (projectTaskLongMap.containsKey(task)) {
                    long sum = projectTaskLongMap.get(task);
                    projectTaskLongMap.put(task, sum + taskTotalTime);
                }
                else {
                    projectTaskLongMap.put(task, taskTotalTime);
                }
            }
            else {
                withoutTaskDur += record.getEndTime().getTime() - record.getStartTime().getTime();
            }
        }

        for (Map.Entry<ProjectTask, Long> entry : projectTaskLongMap.entrySet()) {
            Date totalTaskTime = new Date();
            totalTaskTime.setTime(entry.getValue() - 10800000);

            dto.getTasks().put(
                    entry.getKey().getTitle(),
                    new SimpleDateFormat("HH:mm:ss").format(totalTaskTime)
            );
        }

        withoutTaskTime.setTime(withoutTaskDur - 10800000);
        dto.setWithoutTasksTotalTime(new SimpleDateFormat("HH:mm:ss").format(withoutTaskTime));

        return dto;
    }
}
