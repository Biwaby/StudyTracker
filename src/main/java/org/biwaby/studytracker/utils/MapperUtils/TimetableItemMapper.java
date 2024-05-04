package org.biwaby.studytracker.utils.MapperUtils;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.*;
import org.biwaby.studytracker.models.DTO.TimetableDTO;
import org.biwaby.studytracker.repositories.ClassTypeRepo;
import org.biwaby.studytracker.repositories.ClassroomRepo;
import org.biwaby.studytracker.repositories.SubjectRepo;
import org.biwaby.studytracker.repositories.TeacherRepo;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimetableMapper {

    private final SubjectRepo subjectRepo;
    private final TeacherRepo teacherRepo;
    private final ClassTypeRepo classTypeRepo;
    private final ClassroomRepo classroomRepo;

    public TimetableDTO mapToTimetableDTO(Timetable timetable) {
        TimetableDTO dto = new TimetableDTO();
        dto.setSubjectId(timetable.getSubject().getId());
        dto.setTeacherId(timetable.getTeacher().getId());
        dto.setClassTypeId(timetable.getClassType().getId());
        dto.setClassroomId(timetable.getClassroom().getId());
        dto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(timetable.getDate()));
        dto.setBeginTime(new SimpleDateFormat("HH:mm").format(timetable.getBeginTime()));
        dto.setEndTime(new SimpleDateFormat("HH:mm").format(timetable.getEndTime()));
        return dto;
    }

    public Timetable mapToTimetableEntity(TimetableDTO dto) throws ParseException {
        Timetable timetable = new Timetable();

        Optional<Subject> optionalSubject = subjectRepo.findById(dto.getSubjectId());
        Optional<Teacher> optionalTeacher = teacherRepo.findById(dto.getTeacherId());
        Optional<ClassType> optionalClassType = classTypeRepo.findById(dto.getClassTypeId());
        Optional<Classroom> optionalClassroom = classroomRepo.findById(dto.getClassroomId());

        if (optionalSubject.isPresent()) { timetable.setSubject(optionalSubject.get()); }
        else { timetable.setSubject(null); }

        if (optionalTeacher.isPresent()) { timetable.setTeacher(optionalTeacher.get()); }
        else { timetable.setTeacher(null); }

        if (optionalClassType.isPresent()) { timetable.setClassType(optionalClassType.get()); }
        else { timetable.setClassType(null); }

        if (optionalClassroom.isPresent()) { timetable.setClassroom(optionalClassroom.get()); }
        else { timetable.setClassroom(null); }

        timetable.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getDate()));
        timetable.setBeginTime(new SimpleDateFormat("HH:mm").parse(dto.getBeginTime()));
        timetable.setEndTime(new SimpleDateFormat("HH:mm").parse(dto.getEndTime()));
        return timetable;
    }
}
