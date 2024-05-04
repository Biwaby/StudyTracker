package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubjectService {

    Subject addSubject(Subject subject);

    List<Subject> getAllSubjects();

    boolean deleteSubject(Long id);

    boolean editSubject(Long id, Subject subject);
}
