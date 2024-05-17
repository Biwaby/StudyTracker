package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.TimetableItemNotFoundException;
import org.biwaby.studytracker.models.DTO.TimetableItemDTO;
import org.biwaby.studytracker.models.DTO.ViewDTO.TimetableDTO;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.TimetableItem;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.repositories.TimetableItemRepo;
import org.biwaby.studytracker.services.interfaces.TimetableItemService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.PresentationMappers.TimetableMapper;
import org.biwaby.studytracker.utils.MapperUtils.TimetableComparator;
import org.biwaby.studytracker.utils.MapperUtils.TimetableItemMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TimetableItemImpl implements TimetableItemService {

    private final TimetableItemRepo timetableItemRepo;
    private final TimetableItemMapper mapper;
    private final TimetableMapper timetableMapper;
    private final UserService userService;
    private final RoleRepo roleRepo;

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
    public TimetableDTO getTimetableItemById(Long id) {
        return timetableMapper.toDTO(timetableItemRepo.findById(id).orElseThrow(TimetableItemNotFoundException::new));
    }

    @Override
    public void deleteItemFromTimetable(Long id) {
        TimetableItem timetableItem = timetableItemRepo.findById(id).orElseThrow(TimetableItemNotFoundException::new);
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();

        if (!user.getId().equals(timetableItem.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("Нет доступа");
        }

        timetableItemRepo.delete(timetableItem);
    }

    @Override
    public void editItemInTimetable(Long id, TimetableItemDTO dto) throws ParseException {
        TimetableItem timetableItem = timetableItemRepo.findById(id).orElseThrow(TimetableItemNotFoundException::new);
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();

        if (!user.getId().equals(timetableItem.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("Нет доступа");
        }

        mapper.updateDataFromDTO(timetableItem, dto);
        timetableItemRepo.save(timetableItem);
    }
}
