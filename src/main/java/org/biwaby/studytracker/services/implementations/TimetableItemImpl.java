package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.TimetableItemDTO;
import org.biwaby.studytracker.models.DTO.ViewDTO.TimetableDTO;
import org.biwaby.studytracker.models.TimetableItem;
import org.biwaby.studytracker.repositories.TimetableItemRepo;
import org.biwaby.studytracker.services.interfaces.TimetableItemService;
import org.biwaby.studytracker.utils.MapperUtils.PresentationMappers.TimetableMapper;
import org.biwaby.studytracker.utils.MapperUtils.TimetableComparator;
import org.biwaby.studytracker.utils.MapperUtils.TimetableItemMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TimetableItemImpl implements TimetableItemService {

    private final TimetableItemRepo timetableItemRepo;
    private final TimetableItemMapper mapper;
    private final TimetableMapper timetableMapper;

    @Override
    public TimetableDTO addItemInTimetable(TimetableItemDTO dto) throws ParseException {
        TimetableItem item = mapper.toEntity(dto);
        timetableItemRepo.save(item);
        return timetableMapper.toDTO(item);
    }

    @Override
    public List<TimetableDTO> getAllItemsFromTimetable() {
        List<TimetableItem> timetable = timetableItemRepo.findAll();
        List<TimetableDTO> dtos = new ArrayList<>();
        timetable.sort(new TimetableComparator());
        timetable.forEach(obj -> dtos.add(timetableMapper.toDTO(obj)));
        return dtos;
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
            mapper.updateDataFromDTO(newTimetableItem, dto);
            timetableItemRepo.save(newTimetableItem);
            return true;
        }
        else {
            return false;
        }
    }
}
