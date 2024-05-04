package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.Classmate;
import org.biwaby.studytracker.repositories.ClassmateRepo;
import org.biwaby.studytracker.services.interfaces.ClassmateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClassmateServiceImpl implements ClassmateService {

    private final ClassmateRepo classmateRepo;

    @Override
    public Classmate addClassmate(Classmate classmate) {
        return classmateRepo.saveAndFlush(classmate);
    }

    @Override
    public List<Classmate> getAllClassmates() {
        return classmateRepo.findAll();
    }

    @Override
    public boolean deleteClassmate(Long id) {
        Optional<Classmate> classmate = classmateRepo.findById(id);
        if (classmate.isPresent()) {
            classmateRepo.delete(classmate.get());
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean editClassmate(Long id, Classmate classmate) {
        Optional<Classmate> editableClassmate = classmateRepo.findById(id);
        if (editableClassmate.isPresent()) {
            Classmate newClassmate = editableClassmate.get();
            newClassmate.setLastName(classmate.getLastName());
            newClassmate.setFirstName(classmate.getFirstName());
            newClassmate.setPatronymic(classmate.getPatronymic());
            classmateRepo.save(newClassmate);
            return true;
        }
        else {
            return false;
        }
    }
}
