package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.ClassroomNotFoundException;
import org.biwaby.studytracker.models.Classroom;
import org.biwaby.studytracker.models.DTO.ClassroomDTO;
import org.biwaby.studytracker.repositories.ClassroomRepo;
import org.biwaby.studytracker.services.interfaces.ClassroomService;
import org.biwaby.studytracker.utils.MapperUtils.ClassroomMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepo classroomRepo;
    private final ClassroomMapper mapper;

    @Override
    public Classroom addClassroom(ClassroomDTO dto) {
        return classroomRepo.saveAndFlush(mapper.toEntity(dto));
    }

    @Override
    public List<Classroom> getALlClassrooms() {
        return classroomRepo.findAll();
    }

    @Override
    public Classroom getClassroomById(Long id) {
        return classroomRepo.findById(id).orElseThrow(ClassroomNotFoundException::new);
    }

    @Override
    public boolean deleteClassroom(Long id) {
        Optional<Classroom> classroom = classroomRepo.findById(id);
        if (classroom.isPresent()) {
            classroomRepo.delete(classroom.get());
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean editClassroom(Long id, ClassroomDTO dto) {
        Optional<Classroom> editableClassroom = classroomRepo.findById(id);
        if (editableClassroom.isPresent()) {
            Classroom newClassroom = editableClassroom.get();
            mapper.updateDataFromDTO(newClassroom, dto);
            classroomRepo.save(newClassroom);
            return true;
        }
        return false;
    }
}
