package org.biwaby.studytracker.utils.MapperUtils;

import org.biwaby.studytracker.models.dto.TimerRecordDTO;
import org.biwaby.studytracker.models.TimerRecord;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class TimerRecordMapper {

    public static TimerRecordDTO toDTO(TimerRecord record) {
        TimerRecordDTO dto = new TimerRecordDTO();
        dto.setId(record.getId());

        if (record.getTitle() != null) {
            dto.setTitle(record.getTitle());
        }
        if (record.getStartTime() != null) {
            dto.setStartTime(new SimpleDateFormat("HH:mm:ss").format(record.getStartTime()));
        }
        if (record.getEndTime() != null) {
            dto.setEndTime(new SimpleDateFormat("HH:mm:ss").format(record.getEndTime()));
        }
        if (record.getRecordDate() != null) {
            dto.setRecordDate(new SimpleDateFormat("dd-MM-yyyy").format(record.getRecordDate()));
        }

        return dto;
    }

    public TimerRecord toEntity(TimerRecordDTO dto) throws ParseException {
        TimerRecord record = new TimerRecord();
        record.setTitle(dto.getTitle());
        record.setStartTime(new SimpleDateFormat("HH:mm:ss").parse(dto.getStartTime()));
        record.setEndTime(new SimpleDateFormat("HH:mm:ss").parse(dto.getEndTime()));
        record.setRecordDate(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getRecordDate()));
        record.setProject(null);
        record.setProjectTask(null);

        return record;
    }

    public void updateDataFromDTO(TimerRecord record, TimerRecordDTO dto) throws ParseException {
        if (dto == null) {
            return;
        }
        if (dto.getTitle() != null) {
            record.setTitle(dto.getTitle());
        }
        if  (dto.getStartTime() != null) {
            record.setStartTime(new SimpleDateFormat("HH:mm:ss").parse(dto.getStartTime()));
        }
        if (dto.getEndTime() != null) {
            record.setEndTime(new SimpleDateFormat("HH:mm:ss").parse(dto.getEndTime()));
        }
        if (dto.getRecordDate() != null) {
            record.setRecordDate(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getRecordDate()));
        }
    }
}
