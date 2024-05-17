package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.SubjectNotFoundException;
import org.biwaby.studytracker.models.Subject;
import org.biwaby.studytracker.repositories.SubjectRepo;
import org.biwaby.studytracker.services.interfaces.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepo subjectRepo;

    @Override
    public Subject addSubject(Subject subject) {
        return subjectRepo.saveAndFlush(subject);
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepo.findAll();
    }

    @Override
    public Subject getSubjectById(Long id) {
        return subjectRepo.findById(id).orElseThrow(SubjectNotFoundException::new);
    }

    @Override
    public boolean deleteSubject(Long id) {
        Optional<Subject> subject = subjectRepo.findById(id);
        if (subject.isPresent()) {
            subjectRepo.delete(subject.get());
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean editSubject(Long id, Subject subject) {
        Optional<Subject> editableSubject = subjectRepo.findById(id);
        if (editableSubject.isPresent()) {
            Subject newSubject = editableSubject.get();
            newSubject.setTitle(subject.getTitle());
            newSubject.setCourse(subject.getCourse());
            newSubject.setSemester(subject.getSemester());
            subjectRepo.save(newSubject);
            return true;
        }
        else {
            return false;
        }
    }
}
