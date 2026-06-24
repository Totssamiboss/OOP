package com.equipment.repository;

import com.equipment.entity.StudioRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// Репозиторий для работы с помещениями фотостудии
// Наследует CrudRepository для стандартных CRUD операций
@Repository
public interface StudioRoomRepository extends CrudRepository<StudioRoom, Long> {
    StudioRoom findByName(String name);
}
