package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.Building;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BuildingService {
    Building addBuilding(Building building);

    List<Building> getAllBuildings();

    Building getBuildingById(Long id);

    boolean deleteBuilding(Long id);

    boolean editBuilding(Long id, Building building);
}
