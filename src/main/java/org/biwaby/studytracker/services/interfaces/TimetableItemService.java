package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.DTO.TimetableItemDTO;
import org.biwaby.studytracker.models.DTO.ViewDTO.TimetableDTO;
import org.biwaby.studytracker.models.TimetableItem;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface TimetableItemService {

    TimetableDTO addItemInTimetable(TimetableItemDTO dto) throws ParseException;

    List<TimetableDTO> getAllItemsFromTimetable();

    TimetableDTO getTimetableItemById(Long id);

    void deleteItemFromTimetable(Long id);

    void editItemInTimetable(Long id, TimetableItemDTO dto) throws ParseException;
}
