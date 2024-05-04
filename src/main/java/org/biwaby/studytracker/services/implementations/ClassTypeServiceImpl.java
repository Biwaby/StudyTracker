package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.ClassType;
import org.biwaby.studytracker.repositories.ClassTypeRepo;
import org.biwaby.studytracker.services.interfaces.ClassTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassTypeServiceImpl implements ClassTypeService {

    private final ClassTypeRepo classTypeRepo;

    @Override
    public ClassType addClassType(ClassType classType) {
        return classTypeRepo.saveAndFlush(classType);
    }

    @Override
    public List<ClassType> getAllClassTypes() {
        return classTypeRepo.findAll();
    }

    @Override
    public boolean deleteClassType(Long id) {
        Optional<ClassType> classType = classTypeRepo.findById(id);
        if (classType.isPresent()) {
            classTypeRepo.delete(classType.get());
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean editClassType(Long id, ClassType classType) {
        Optional<ClassType> editableClassType = classTypeRepo.findById(id);
        if (editableClassType.isPresent()) {
            ClassType newClassType = editableClassType.get();
            newClassType.setType(classType.getType());
            classTypeRepo.save(newClassType);
            return true;
        }
        else {
            return false;
        }
    }
}
