package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.ClassType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassTypeService {

    ClassType addClassType(ClassType classType);

    List<ClassType> getAllClassTypes();

    boolean deleteClassType(Long id);

    boolean editClassType(Long id, ClassType classType);
}
