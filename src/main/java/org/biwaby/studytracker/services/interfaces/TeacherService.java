package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.Teacher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TeacherService {

    Teacher addTeacher(Teacher teacher);

    List<Teacher> getAllTeachers();

    Teacher getTeacherById(Long id);

    boolean deleteTeacher(Long id);

    boolean editTeacher(Long id, Teacher teacher);
}
