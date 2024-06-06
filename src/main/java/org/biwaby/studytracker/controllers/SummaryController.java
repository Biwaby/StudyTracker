package org.biwaby.studytracker.controllers;

import lombok.AllArgsConstructor;
import org.biwaby.studytracker.models.dto.DateSummaryDTO;
import org.biwaby.studytracker.models.dto.ProjectSummaryDTO;
import org.biwaby.studytracker.services.interfaces.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@AllArgsConstructor
@RequestMapping("/summary")
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping("/today")
    public ResponseEntity<DateSummaryDTO> getTodaySummary() throws ParseException {
        return ResponseEntity.ok(
                summaryService.getSummary(
                    new SimpleDateFormat("dd/MM/yyyy").parse(new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
            )
        );
    }

    @GetMapping("/date")
    public ResponseEntity<DateSummaryDTO> getDateSummary(@RequestParam String day, @RequestParam String month, @RequestParam String year) throws ParseException {
        return ResponseEntity.ok(
                summaryService.getSummary(
                        new SimpleDateFormat("dd-MM-yyyy").parse(day + "-" + month + "-" + year)
                )
        );
    }

    @GetMapping("/project")
    public ResponseEntity<ProjectSummaryDTO> getProjectSummary(@RequestParam Long projectId) {
        return ResponseEntity.ok(summaryService.getProjectSummary(projectId));
    }
}
