package org.biwaby.studytracker.utils.MapperUtils.PresentationMappers;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.ViewDTO.TimetableDTO;
import org.biwaby.studytracker.models.TimetableItem;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class TimetableMapper {

    public TimetableDTO toDTO(TimetableItem timetableItem) {
        TimetableDTO dto = new TimetableDTO();
        dto.setId(timetableItem.getId());
        dto.setUser(timetableItem.getUser().getUsername());
        dto.setSubject(timetableItem.getSubject());
        dto.setTeacher(timetableItem.getTeacher());
        dto.setClasstype(timetableItem.getClassType());
        dto.setVisitStatus(timetableItem.getVisitStatus());
        dto.setClassroom(timetableItem.getClassroom());
        dto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(timetableItem.getDate()));
        dto.setBeginTime(new SimpleDateFormat("HH:mm").format(timetableItem.getBeginTime()));
        dto.setEndTime(new SimpleDateFormat("HH:mm").format(timetableItem.getEndTime()));
        return dto;
    }
}
