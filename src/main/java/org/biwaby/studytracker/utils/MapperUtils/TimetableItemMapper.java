package org.biwaby.studytracker.utils.MapperUtils;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.*;
import org.biwaby.studytracker.models.DTO.TimetableItemDTO;
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
public class TimetableItemMapper {

    private final SubjectRepo subjectRepo;
    private final TeacherRepo teacherRepo;
    private final ClassTypeRepo classTypeRepo;
    private final ClassroomRepo classroomRepo;

    public TimetableItemDTO toDTO(TimetableItem timetable) {
        TimetableItemDTO dto = new TimetableItemDTO();
        dto.setSubjectId(timetable.getSubject().getId());
        dto.setTeacherId(timetable.getTeacher().getId());
        dto.setClassTypeId(timetable.getClassType().getId());
        dto.setClassroomId(timetable.getClassroom().getId());
        dto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(timetable.getDate()));
        dto.setBeginTime(new SimpleDateFormat("HH:mm").format(timetable.getBeginTime()));
        dto.setEndTime(new SimpleDateFormat("HH:mm").format(timetable.getEndTime()));
        return dto;
    }

    public TimetableItem toEntity(TimetableItemDTO dto) throws ParseException {
        TimetableItem timetable = new TimetableItem();

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

    public void updateDataFromDTO(TimetableItem timetableItem, TimetableItemDTO dto) throws ParseException {
        if (dto == null) {
            return;
        }
        if (dto.getSubjectId() != null) {
            Optional<Subject> optionalSubject = subjectRepo.findById(dto.getSubjectId());
            optionalSubject.ifPresent(timetableItem::setSubject);
        }
        if (dto.getTeacherId() != null) {
            Optional<Teacher> optionalTeacher = teacherRepo.findById(dto.getTeacherId());
            optionalTeacher.ifPresent(timetableItem::setTeacher);
        }
        if (dto.getClassTypeId() != null) {
            Optional<ClassType> optionalClassType = classTypeRepo.findById(dto.getClassTypeId());
            optionalClassType.ifPresent(timetableItem::setClassType);
        }
        if (dto.getClassroomId() != null) {
            Optional<Classroom> optionalClassroom = classroomRepo.findById(dto.getClassroomId());
            optionalClassroom.ifPresent(timetableItem::setClassroom);
        }
        if (dto.getDate() != null) {
            timetableItem.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getDate()));
        }
        if (dto.getBeginTime() != null) {
            timetableItem.setBeginTime(new SimpleDateFormat("HH:mm").parse(dto.getBeginTime()));
        }
        if (dto.getEndTime() != null) {
            timetableItem.setEndTime(new SimpleDateFormat("HH:mm").parse(dto.getEndTime()));
        }
    }
}
