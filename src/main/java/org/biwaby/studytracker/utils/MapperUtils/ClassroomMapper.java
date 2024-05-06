package org.biwaby.studytracker.utils.MapperUtils;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.Building;
import org.biwaby.studytracker.models.Classroom;
import org.biwaby.studytracker.models.DTO.ClassroomDTO;
import org.biwaby.studytracker.repositories.BuildingRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomMapper {

    private final BuildingRepo buildingRepo;

    public ClassroomDTO toDTO(Classroom classroom) {
        ClassroomDTO dto = new ClassroomDTO();
        dto.setBuildingId(classroom.getBuilding().getId());
        dto.setNumber(classroom.getNumber());
        return dto;
    }

    public Classroom toEntity(ClassroomDTO dto) {
        Classroom classroom = new Classroom();
        Optional<Building> optionalBuilding = buildingRepo.findById(dto.getBuildingId());
        if (optionalBuilding.isPresent()){
            classroom.setBuilding(optionalBuilding.get());
        }
        else {
            classroom.setBuilding(null);
        }
        classroom.setNumber(dto.getNumber());
        return classroom;
    }

    public void updateDataFromDTO(Classroom classroom, ClassroomDTO dto) {
        if (dto == null) {
            return;
        }
        if (dto.getBuildingId() != null) {
            Optional<Building> optionalBuilding = buildingRepo.findById(dto.getBuildingId());
            optionalBuilding.ifPresent(classroom::setBuilding);
        }
        if (dto.getNumber() != null) {
            classroom.setNumber(dto.getNumber());
        }
    }
}
