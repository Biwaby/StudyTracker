package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.Classmate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassmateService {

    Classmate addClassmate(Classmate classmate);

    List<Classmate> getAllClassmates();

    boolean deleteClassmate(Long id);

    boolean editClassmate(Long id, Classmate classmate);
}
