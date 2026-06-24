package com.equipment.repository;

import com.equipment.entity.Equipment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Репозиторий для работы с оборудованием (Equipment)
// Наследует CrudRepository для стандартных CRUD операций
@Repository
public interface EquipmentRepository extends CrudRepository<Equipment, Long> {
    List<Equipment> findByStudioRoomId(Long studioRoomId);
}
