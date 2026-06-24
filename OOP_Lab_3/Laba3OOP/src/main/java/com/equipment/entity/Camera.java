package com.equipment.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

// Класс "Камера" - оборудование для съемки
@Entity
@Data
@Table(name = "cameras")
@PrimaryKeyJoinColumn(name = "equipment_id")
@DiscriminatorValue("Camera")
public class Camera extends Equipment {
    
    @Column
    private Integer resolution = 24; // разрешение в мегапикселях (значение по умолчанию)
    
    @Column
    private Integer charge = 100; // заряд батареи в процентах (значение по умолчанию)
    
    // КОНТРУКТОР ПО УМОЛЧАНИЮ 
    public Camera() {
        
    // КОНСТРУКТОР ПО ПАРАМЕТРАМ 
    }
    public Camera(String name, String serialNumber, StudioRoom studioRoom, 
                  Integer resolution, Integer charge) {
        super(name, serialNumber, studioRoom);
        this.resolution = resolution;
        this.charge = charge;
    }
    
    // КОНСТРУКТОР КОПИРОВАНИЯ 
    public Camera(Camera other) {
        super(other.getName() + " (Копия)", other.getSerialNumber() + "_copy_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000), other.getStudioRoom());
        this.resolution = other.getResolution();
        this.charge = other.getCharge();
    }
    
    // Метод: сделать фото
    public String takePhoto() {
        if (charge > 0) {
            setCharge(charge - 1);
            return String.format("[*] Фото успешно сделано!\n[^] Заряд уменьшен на 1%% (осталось: %d%%)", charge);
        } else {
            return "[X] ОШИБКА: Батарея разряжена! Требуется подзарядка.";
        }
    }
    
    // Метод: начать запись видео
    public String recordVideo() {
        return String.format("[#] Начата запись видео в разрешении %d MP", resolution);
    }
    
    // Метод: зарядить батарею
    public String recharge() {
        int oldCharge = charge;
        setCharge(100);
        return String.format("[+] Подзарядка завершена!\n[>] Было: %d%% -> Стало: %d%%", oldCharge, charge);
    }
    
    @Override
    public String work() {
        String photoLog = takePhoto();
        return String.format("========================================\n" +
                   "КАМЕРА: %s\n" +
                   "========================================\n" +
                   "[>] Устройство начало работу...\n" +
                   "%s\n" +
                   "[OK] Камера готова к съемке", getName(), photoLog);
    }
    
    @Override
    public String getDescription() {
        return String.format("Тип: Камера | Название: %s | Серийный номер: %s | Разрешение: %d MP | Заряд: %d%%",
                getName(), getSerialNumber(), resolution, charge);
    }
    
    @Override
    public List<String> getAvailableActions() {
        return Arrays.asList("takePhoto", "recordVideo", "recharge");
    }
    
    @Override
    public String performAction(String action) {
        switch (action) {
            case "takePhoto":
                return takePhoto();
            case "recordVideo":
                return recordVideo();
            case "recharge":
                return recharge();
            case "work":
                return work();
            default:
                return "[X] Неизвестное действие: " + action;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Camera{id=%d, name='%s', serialNumber='%s', " +
                        "resolution=%d MP, charge=%d%%, studioRoom='%s'}",
                getId(), getName(), getSerialNumber(),
                resolution, charge,
                getStudioRoom() != null ? getStudioRoom().getName() : "null");
    }
}
