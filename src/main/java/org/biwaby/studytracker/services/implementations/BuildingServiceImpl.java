package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.BuildingNotFoundException;
import org.biwaby.studytracker.models.Building;
import org.biwaby.studytracker.repositories.BuildingRepo;
import org.biwaby.studytracker.services.interfaces.BuildingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepo buildingRepo;

    @Override
    public Building addBuilding(Building building) {
        return buildingRepo.saveAndFlush(building);
    }

    @Override
    public List<Building> getAllBuildings() {
        return buildingRepo.findAll();
    }

    @Override
    public Building getBuildingById(Long id) {
        return buildingRepo.findById(id).orElseThrow(BuildingNotFoundException::new);
    }

    @Override
    public boolean deleteBuilding(Long id) {
        Optional<Building> building = buildingRepo.findById(id);
        if (building.isPresent()) {
            buildingRepo.delete(building.get());
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean editBuilding(Long id, Building building) {
        Optional<Building> editableBuilding = buildingRepo.findById(id);
        if (editableBuilding.isPresent()) {
            Building newBuilding = editableBuilding.get();
            newBuilding.setTitle(building.getTitle());
            buildingRepo.save(newBuilding);
            return true;
        }
        else {
            return false;
        }
    }
}
