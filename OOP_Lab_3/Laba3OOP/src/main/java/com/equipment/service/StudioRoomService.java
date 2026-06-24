package com.equipment.service;

import com.equipment.entity.StudioRoom;
import com.equipment.repository.StudioRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Сервисный класс для работы с помещениями фотостудии
@Service
public class StudioRoomService {
    
    private final StudioRoomRepository studioRoomRepository;
    
    @Autowired
    public StudioRoomService(StudioRoomRepository studioRoomRepository) {
        this.studioRoomRepository = studioRoomRepository;
    }
    
    // Сохранение помещения
    @Transactional
    public StudioRoom save(StudioRoom studioRoom) {
        return studioRoomRepository.save(studioRoom);
    }
    
    // Получение всех помещений
    @Transactional
    public List<StudioRoom> getAll() {
        List<StudioRoom> studioRoomList = new ArrayList<>();
        studioRoomRepository.findAll().forEach(studioRoomList::add);
        return studioRoomList;
    }
    
    // Получение помещения по ID
    @Transactional
    public Optional<StudioRoom> findById(Long id) {
        return studioRoomRepository.findById(id);
    }
    
    // Удаление помещения по ID
    @Transactional
    public void delete(Long id) {
        studioRoomRepository.deleteById(id);
    }
    
    // Поиск помещения по имени
    public StudioRoom findByName(String name) {
        return studioRoomRepository.findByName(name);
    }
    
    // Обновление помещения
    @Transactional
    public StudioRoom update(StudioRoom studioRoom) {
        return studioRoomRepository.save(studioRoom);
    }
}
