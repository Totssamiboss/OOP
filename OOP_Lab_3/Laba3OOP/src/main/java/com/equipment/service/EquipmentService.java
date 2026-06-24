package com.equipment.service;

import com.equipment.entity.Camera;
import com.equipment.entity.Equipment;
import com.equipment.entity.Light;
import com.equipment.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Сервисный класс для работы с оборудованием
// Использует EquipmentRepository для доступа к данным
@Service
public class EquipmentService {
    
    private final EquipmentRepository equipmentRepository;
    
    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }
    
    // Сохранение оборудования
    @Transactional
    public Equipment saveEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }
    
    // Удаление оборудования по ID
    @Transactional
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }
    
    // Копирование оборудования по ID
    // Использует конструктор копирования соответствующего типа
    @Transactional
    public Equipment copyEquipment(Long id) {
        Optional<Equipment> equipmentOptional = equipmentRepository.findById(id);
        
        if (equipmentOptional.isEmpty()) {
            throw new RuntimeException("Оборудование с ID " + id + " не найдено");
        }
        
        Equipment original = equipmentOptional.get();
        Equipment copy;
        
        // Определяем тип оборудования и используем соответствующий конструктор копирования
        if (original instanceof Camera camera) {
            copy = new Camera(camera);
        } else if (original instanceof Light light) {
            copy = new Light(light);
        } else {
            throw new RuntimeException("Неизвестный тип оборудования: " + original.getClass().getName());
        }
        
        // Сохраняем копию в базу данных
        return equipmentRepository.save(copy);
    }
    
    // Получение списка всего оборудования
    @Transactional
    public List<Equipment> getAll() {
        List<Equipment> equipmentList = new ArrayList<>();
        equipmentRepository.findAll().forEach(equipmentList::add);
        return equipmentList;
    }
    
    // Получение оборудования по ID
    @Transactional
    public Optional<Equipment> findById(Long id) {
        return equipmentRepository.findById(id);
    }
    
    // Получение оборудования по ID помещения
    @Transactional
    public List<Equipment> getByStudioRoom(Long studioRoomId) {
        return equipmentRepository.findByStudioRoomId(studioRoomId);
    }
}
