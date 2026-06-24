package com.equipment.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Сущность "Фотостудия" - помещение, в котором находится оборудование
@Entity
@Data
@Table(name = "studio_rooms")
public class StudioRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private String description;
    
    @Column
    private Double area;
    
    @OneToMany(mappedBy = "studioRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Equipment> equipmentList = new ArrayList<>();
    
    public StudioRoom() {
    }
    
    public StudioRoom(String name, String description, Double area) {
        this.name = name;
        this.description = description;
        this.area = area;
    }
    
    @Override
    public String toString() {
        return String.format("StudioRoom{id=%d, name='%s', description='%s', area=%.1f}", 
                id, name, description, area);
    }
}
