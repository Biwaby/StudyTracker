package org.biwaby.studytracker.utils.MapperUtils.PresentationMappers;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.ViewDTO.TimerNotePresentationDTO;
import org.biwaby.studytracker.models.TimerNote;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class TimerNotePresentationMapper {

    private final UserService userService;

    public TimerNotePresentationDTO toDTO(TimerNote timerNote) {
        TimerNotePresentationDTO dto = new TimerNotePresentationDTO();
        dto.setId(timerNote.getId());
        //dto.setUser(userService.getUserByAuth().getUsername());
        dto.setUser(timerNote.getUser().getUsername());
        dto.setSubject(timerNote.getSubject());
        dto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(timerNote.getDate()));
        dto.setDuration(timerNote.getDuration());
        return dto;
    }
}
