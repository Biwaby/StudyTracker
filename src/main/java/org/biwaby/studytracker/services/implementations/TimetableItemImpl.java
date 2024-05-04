package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.TimetableItemDTO;
import org.biwaby.studytracker.models.TimetableItem;
import org.biwaby.studytracker.repositories.TimetableItemRepo;
import org.biwaby.studytracker.services.interfaces.TimetableItemService;
import org.biwaby.studytracker.utils.MapperUtils.TimetableItemMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimetableItemImpl implements TimetableItemService {

    private final TimetableItemRepo timetableItemRepo;
    private final TimetableItemMapper mapper;

    @Override
    public TimetableItem addItemInTimetable(TimetableItemDTO dto) throws ParseException {
        return timetableItemRepo.saveAndFlush(mapper.mapToTimetableEntity(dto));
    }

    @Override
    public List<TimetableItem> getAllItemsFromTimetable() {
        return timetableItemRepo.findAll();
    }

    @Override
    public boolean deleteItemFromTimetable(Long id) {
        Optional<TimetableItem> timetableItem = timetableItemRepo.findById(id);
        if (timetableItem.isPresent()) {
            timetableItemRepo.delete(timetableItem.get());
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean editItemInTimetable(Long id, TimetableItemDTO dto) throws ParseException {
        Optional<TimetableItem> editableTimetableItem = timetableItemRepo.findById(id);
        if (editableTimetableItem.isPresent()) {
            TimetableItem newTimetableItem = editableTimetableItem.get();
            TimetableItem mappedTimetableItem = mapper.mapToTimetableEntity(dto);
            newTimetableItem.setSubject(mappedTimetableItem.getSubject());
            newTimetableItem.setTeacher(mappedTimetableItem.getTeacher());
            newTimetableItem.setClassType(mappedTimetableItem.getClassType());
            newTimetableItem.setClassroom(mappedTimetableItem.getClassroom());
            newTimetableItem.setDate(mappedTimetableItem.getDate());
            newTimetableItem.setBeginTime(mappedTimetableItem.getBeginTime());
            newTimetableItem.setEndTime(mappedTimetableItem.getEndTime());
            timetableItemRepo.save(newTimetableItem);
            return true;
        }
        else {
            return false;
        }
    }
}
