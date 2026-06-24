package com.equipment.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

// Класс "Свет" - осветительное оборудование
@Entity
@Data
@Table(name = "lights")
@PrimaryKeyJoinColumn(name = "equipment_id")
@DiscriminatorValue("Light")
public class Light extends Equipment {
    
    @Column
    private Integer brightness = 50; // яркость в процентах (0-100, значение по умолчанию)
    
    @Column(name = "color_temperature")
    private Integer colorTemperature = 5600; // цветовая температура в Кельвинах (значение по умолчанию)
    
    // КОНСТРУКТОР ПО УМОЛЧАНИЮ
    public Light() {
        
    }
    
    // КОНСТРУКТОР С ПАРАМЕТРАМИ
    public Light(String name, String serialNumber, StudioRoom studioRoom,
                 Integer brightness, Integer colorTemperature) {
        super(name, serialNumber, studioRoom);
        this.brightness = brightness;
        this.colorTemperature = colorTemperature;
    }
    
    // КОНСТРУКТОР КОПИРОВАНИЯ
    public Light(Light other) {
        super(other.getName() + " (Копия)", other.getSerialNumber() + "_copy_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000), other.getStudioRoom());
        this.brightness = other.getBrightness();
        this.colorTemperature = other.getColorTemperature();
    }
    
    // Метод: активировать авто-яркость
    public String enableAutoBrightness() {
        int oldBrightness = brightness;
        setBrightness(100);
        return String.format("[*] Авто-яркость активирована!\n[>] Яркость: %d%% -> %d%%", oldBrightness, brightness);
    }
    
    // Метод: переключить цветовую температуру
    public String switchColor() {
        setColorTemperature(colorTemperature == 3000 ? 6000 : 3000);
        return String.format("[Настройка] Цвет изменен на %dK", colorTemperature);
    }
    
    @Override
    public String work() {
        setBrightness(brightness > 5 ? brightness - 5 : 100);
        String colorLog = switchColor();
        return String.format("========================================\n" +
                   "СВЕТ: %s\n" +
                   "========================================\n" +
                   "[>] Устройство начало работу...\n" +
                   "%s\n" +
                   "[Текущая яркость]: %d%%\n" +
                   "[OK] Освещение активно", getName(), colorLog, brightness);
    }
    
    @Override
    public String getDescription() {
        String lightType = (colorTemperature <= 4000) ? "Тёплый" : "Холодный";
        return String.format("Тип: Свет | Название: %s | Серийный номер: %s | Яркость: %d%% | Цвет: %dK (%s)",
                getName(), getSerialNumber(), brightness, colorTemperature, lightType);
    }
    
    @Override
    public List<String> getAvailableActions() {
        return Arrays.asList("enableAutoBrightness", "switchColor");
    }
    
    @Override
    public String performAction(String action) {
        switch (action) {
            case "enableAutoBrightness":
                return enableAutoBrightness();
            case "switchColor":
                return switchColor();
            case "work":
                return work();
            default:
                return "[X] Неизвестное действие: " + action;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Light{id=%d, name='%s', serialNumber='%s', " +
                        "brightness=%d%%, colorTemperature=%dK, studioRoom='%s'}",
                getId(), getName(), getSerialNumber(),
                brightness, colorTemperature,
                getStudioRoom() != null ? getStudioRoom().getName() : "null");
    }
}
