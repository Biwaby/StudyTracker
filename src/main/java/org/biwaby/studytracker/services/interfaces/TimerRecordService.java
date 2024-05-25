package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.DTO.TimerRecordDTO;
import org.biwaby.studytracker.models.TimerRecord;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.ParseException;


@Service
public interface TimerRecordService {

    TimerRecordDTO addRecord(TimerRecordDTO dto) throws ParseException;
    Page<TimerRecordDTO> getAllRecords(int page);
    TimerRecordDTO getRecordById(Long id);
    void deleteRecord(Long id);
    void editRecord(Long id, TimerRecordDTO dto) throws ParseException;
    TimerRecordDTO addTagToRecord(Long recordId, Long tagId);
    TimerRecordDTO removeTagFromRecord(Long recordId, Long tagId);
    Page<TimerRecord> getAllRecordsByUser(String username, int page);
}