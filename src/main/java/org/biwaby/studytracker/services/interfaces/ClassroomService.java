package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.Classroom;
import org.biwaby.studytracker.models.DTO.ClassroomDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassroomService {

    Classroom addClassroom(ClassroomDTO dto);

    List<Classroom> getALlClassrooms();

    Classroom getClassroomById(Long id);

    boolean deleteClassroom(Long id);

    boolean editClassroom(Long id, ClassroomDTO dto);
}
