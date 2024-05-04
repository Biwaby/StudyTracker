package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.DTO.TimetableItemDTO;
import org.biwaby.studytracker.models.TimetableItem;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface TimetableItemService {

    TimetableItem addItemInTimetable(TimetableItemDTO dto) throws ParseException;

    List<TimetableItem> getAllItemsFromTimetable();

    boolean deleteItemFromTimetable(Long id);

    boolean editItemInTimetable(Long id, TimetableItemDTO dto) throws ParseException;
}
