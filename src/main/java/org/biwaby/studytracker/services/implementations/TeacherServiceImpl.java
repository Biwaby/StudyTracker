package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.Teacher;
import org.biwaby.studytracker.repositories.TeacherRepo;
import org.biwaby.studytracker.services.interfaces.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepo teacherRepo;

    @Override
    public Teacher addTeacher(Teacher teacher) {
        return teacherRepo.saveAndFlush(teacher);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepo.findAll();
    }

    @Override
    public boolean deleteTeacher(Long id) {
        Optional<Teacher> teacher = teacherRepo.findById(id);
        if (teacher.isPresent()) {
            teacherRepo.delete(teacher.get());
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean editTeacher(Long id, Teacher teacher) {
        Optional<Teacher> editableTeacher = teacherRepo.findById(id);
        if (editableTeacher.isPresent()) {
            Teacher newTeacher = editableTeacher.get();
            newTeacher.setLastName(teacher.getLastName());
            newTeacher.setFirstName(teacher.getFirstName());
            newTeacher.setPatronymic(teacher.getPatronymic());
            newTeacher.setEmail(teacher.getEmail());
            teacherRepo.save(newTeacher);
            return true;
        }
        else {
            return false;
        }
    }
}
