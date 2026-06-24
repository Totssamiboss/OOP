package com.equipment.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

// Базовый класс "Оборудование" - абстрактный класс для иерархии оборудования
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "equipment_type", discriminatorType = DiscriminatorType.STRING)
@Data
@Table(name = "equipment")
public abstract class Equipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "studio_room_id", nullable = false)
    private StudioRoom studioRoom;
    
    public Equipment() {
    }
    
    public Equipment(String name, String serialNumber, StudioRoom studioRoom) {
        this.name = name;
        this.serialNumber = serialNumber;
        this.studioRoom = studioRoom;
    }
    
    // Абстрактный метод работы 
    public abstract String work();
    
    // Абстрактный метод получения описания
    public abstract String getDescription();
    
    // Метод для получения списка доступных действий
    public abstract List<String> getAvailableActions();
    
    // Метод для выполнения конкретного действия
    public abstract String performAction(String action);
    
    @Override
    public String toString() {
        return String.format("Equipment{id=%d, name='%s', serialNumber='%s', studioRoom='%s'}", 
                id, name, serialNumber, 
                studioRoom != null ? studioRoom.getName() : "null");
    }
}
