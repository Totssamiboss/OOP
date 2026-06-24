package com.equipment.controller;

import com.equipment.entity.Camera;
import com.equipment.entity.Equipment;
import com.equipment.entity.Light;
import com.equipment.entity.StudioRoom;
import com.equipment.service.EquipmentService;
import com.equipment.service.StudioRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Контроллер для обработки запросов, связанных с оборудованием
@Controller
@RequestMapping("/")
public class EquipmentController {
    
    private final EquipmentService equipmentService;
    private final StudioRoomService studioRoomService;
    
    @Autowired
    public EquipmentController(EquipmentService equipmentService, 
                               StudioRoomService studioRoomService) {
        this.equipmentService = equipmentService;
        this.studioRoomService = studioRoomService;
    }
    
    // Отображение списка оборудования (с фильтрацией по помещению)
    // GET / и GET /?studioRoomId={id}
    @GetMapping
    public String getFilteredEquipment(@RequestParam(required = false) Long studioRoomId, Model model) {
        try {
            List<Equipment> equipmentList;
            if (studioRoomId != null && studioRoomId > 0) {
                equipmentList = equipmentService.getByStudioRoom(studioRoomId);
            } else {
                equipmentList = equipmentService.getAll();
            }
            model.addAttribute("equipmentList", equipmentList);
            model.addAttribute("studioRooms", studioRoomService.getAll());
            model.addAttribute("selectedStudioRoomId", studioRoomId);
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке оборудования: " + e.getMessage());
            e.printStackTrace();
            // Возвращаем пустой список, если произошла ошибка
            model.addAttribute("equipmentList", new ArrayList<>());
            model.addAttribute("studioRooms", new ArrayList<>());
        }
        return "equipment-list";
    }
    
    // Показ формы для добавления нового оборудования
    // GET /add
    @GetMapping("/add")
    public String showAddForm(Model model) {
        try {
            model.addAttribute("studioRooms", studioRoomService.getAll());
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке помещений: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("studioRooms", new ArrayList<>());
        }
        model.addAttribute("equipmentTypes", new String[]{"Camera", "Light"});
        
        // Устанавливаем значения по умолчанию для формы
        model.addAttribute("equipmentType", "Camera"); // По умолчанию - Камера
        model.addAttribute("name", "Новое оборудование");
        model.addAttribute("serialNumber", "SN-" + System.currentTimeMillis());
        model.addAttribute("studioRoomId", null);
        
        // Значения по умолчанию для Camera (будут использоваться, если выбран тип Camera)
        model.addAttribute("resolution", 24);
        model.addAttribute("charge", 100);
        
        // Значения по умолчанию для Light (будут использоваться, если выбран тип Light)
        model.addAttribute("brightness", 50);
        model.addAttribute("colorTemperature", 5600);
        
        return "add-equipment";
    }
    
    // Обработка формы добавления оборудования
    // POST /add
    @PostMapping("/add")
    public String addEquipment(
            @RequestParam String equipmentType,
            @RequestParam String name,
            @RequestParam String serialNumber,
            @RequestParam(required = false) Long studioRoomId,
            @RequestParam(required = false) Integer resolution,
            @RequestParam(required = false) Integer charge,
            @RequestParam(required = false) Integer brightness,
            @RequestParam(required = false) Integer colorTemperature,
            Model model) {
        
        try {
            System.out.println("=== POST /add received ===");
            System.out.println("equipmentType: " + equipmentType);
            System.out.println("name: " + name);
            System.out.println("serialNumber: " + serialNumber);
            System.out.println("studioRoomId: " + studioRoomId);
            System.out.println("resolution: " + resolution);
            System.out.println("charge: " + charge);
            System.out.println("brightness: " + brightness);
            System.out.println("colorTemperature: " + colorTemperature);
            
            // Проверяем, что помещение выбрано
            if (studioRoomId == null) {
                model.addAttribute("errorMessage", "Пожалуйста, выберите помещение");
                model.addAttribute("equipmentType", equipmentType);
                model.addAttribute("name", name);
                model.addAttribute("serialNumber", serialNumber);
                model.addAttribute("resolution", resolution);
                model.addAttribute("charge", charge);
                model.addAttribute("brightness", brightness);
                model.addAttribute("colorTemperature", colorTemperature);
                model.addAttribute("studioRooms", studioRoomService.getAll());
                model.addAttribute("equipmentTypes", new String[]{"Camera", "Light"});
                return "add-equipment";
            }
            
            StudioRoom studioRoom = studioRoomService.findById(studioRoomId)
                    .orElseThrow(() -> new RuntimeException("Помещение с ID " + studioRoomId + " не найдено. Доступные помещения: " + studioRoomService.getAll().size()));
            
            // Устанавливаем значения по умолчанию, если не заполнены
            if (name == null || name.trim().isEmpty()) {
                name = "Новое оборудование";
            }
            if (serialNumber == null || serialNumber.trim().isEmpty()) {
                serialNumber = "SN-" + System.currentTimeMillis();
            }
            
            // проверяем, что все обязательные поля заполнены для выбранного типа
            if ("Camera".equals(equipmentType)) {
                if (resolution == null || charge == null) {
                    model.addAttribute("errorMessage", "Для камеры обязательны поля: разрешение и заряд батареи");
                    model.addAttribute("equipmentType", equipmentType);
                    model.addAttribute("name", name);
                    model.addAttribute("serialNumber", serialNumber);
                    model.addAttribute("studioRoomId", studioRoomId);
                    model.addAttribute("resolution", resolution);
                    model.addAttribute("charge", charge);
                    model.addAttribute("studioRooms", studioRoomService.getAll());
                    model.addAttribute("equipmentTypes", new String[]{"Camera", "Light"});
                    return "add-equipment";
                }
            } else if ("Light".equals(equipmentType)) {
                if (brightness == null || colorTemperature == null) {
                    model.addAttribute("errorMessage", "Для света обязательны поля: яркость и цветовая температура");
                    model.addAttribute("equipmentType", equipmentType);
                    model.addAttribute("name", name);
                    model.addAttribute("serialNumber", serialNumber);
                    model.addAttribute("studioRoomId", studioRoomId);
                    model.addAttribute("brightness", brightness);
                    model.addAttribute("colorTemperature", colorTemperature);
                    model.addAttribute("studioRooms", studioRoomService.getAll());
                    model.addAttribute("equipmentTypes", new String[]{"Camera", "Light"});
                    return "add-equipment";
                }
            } else {
                model.addAttribute("errorMessage", "Неизвестный тип оборудования: " + equipmentType);
                model.addAttribute("equipmentType", equipmentType);
                model.addAttribute("name", name);
                model.addAttribute("serialNumber", serialNumber);
                model.addAttribute("studioRoomId", studioRoomId);
                model.addAttribute("studioRooms", studioRoomService.getAll());
                model.addAttribute("equipmentTypes", new String[]{"Camera", "Light"});
                return "add-equipment";
            }
            
            Equipment equipment;
            
            if ("Camera".equals(equipmentType)) {
                equipment = new Camera(name, serialNumber, studioRoom, resolution, charge);
            } else {
                equipment = new Light(name, serialNumber, studioRoom, brightness, colorTemperature);
            }
            
            equipmentService.saveEquipment(equipment);
            
            return "redirect:/";
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении оборудования: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Произошла ошибка: " + e.getMessage());
            model.addAttribute("equipmentType", equipmentType);
            model.addAttribute("name", name);
            model.addAttribute("serialNumber", serialNumber);
            model.addAttribute("studioRoomId", studioRoomId);
            model.addAttribute("resolution", resolution);
            model.addAttribute("charge", charge);
            model.addAttribute("brightness", brightness);
            model.addAttribute("colorTemperature", colorTemperature);
            model.addAttribute("studioRooms", studioRoomService.getAll());
            model.addAttribute("equipmentTypes", new String[]{"Camera", "Light"});
            return "add-equipment";
        }
    }
    
    // Показ формы редактирования оборудования
    // GET /edit/{id}
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Equipment equipment = equipmentService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Оборудование с ID " + id + " не найдено"));
            model.addAttribute("equipment", equipment);
            model.addAttribute("equipmentType", equipment.getClass().getSimpleName());
            model.addAttribute("studioRooms", studioRoomService.getAll());
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке оборудования для редактирования: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/";
        }
        return "edit-equipment";
    }
    
    // Обработка формы редактирования оборудования
    // POST /edit
    @PostMapping("/edit")
    public String editEquipment(
            @RequestParam Long id,
            @RequestParam String equipmentType,
            @RequestParam String name,
            @RequestParam String serialNumber,
            @RequestParam(required = false) Long studioRoomId,
            @RequestParam(required = false) Integer resolution,
            @RequestParam(required = false) Integer charge,
            @RequestParam(required = false) Integer brightness,
            @RequestParam(required = false) Integer colorTemperature,
            Model model) {
        
        try {
            System.out.println("=== POST /edit received ===");
            System.out.println("ID: " + id);
            System.out.println("equipmentType: " + equipmentType);
            
            Equipment equipment = equipmentService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Оборудование с ID " + id + " не найдено"));
            
            // Проверяем, что помещение выбрано
            if (studioRoomId == null) {
                model.addAttribute("errorMessage", "Пожалуйста, выберите помещение");
                model.addAttribute("equipment", equipment);
                model.addAttribute("studioRooms", studioRoomService.getAll());
                return "edit-equipment";
            }
            
            StudioRoom studioRoom = studioRoomService.findById(studioRoomId)
                    .orElseThrow(() -> new RuntimeException("Помещение с ID " + studioRoomId + " не найдено"));
            
            // проверяем, что все обязательные поля заполнены для выбранного типа
            if ("Camera".equals(equipmentType)) {
                if (resolution == null || charge == null) {
                    model.addAttribute("errorMessage", "Для камеры обязательны поля: разрешение и заряд батареи");
                    model.addAttribute("equipment", equipment);
                    model.addAttribute("studioRooms", studioRoomService.getAll());
                    return "edit-equipment";
                }
            } else if ("Light".equals(equipmentType)) {
                if (brightness == null || colorTemperature == null) {
                    model.addAttribute("errorMessage", "Для света обязательны поля: яркость и цветовая температура");
                    model.addAttribute("equipment", equipment);
                    model.addAttribute("studioRooms", studioRoomService.getAll());
                    return "edit-equipment";
                }
            }
            
            // Обновляем оборудование
            equipment.setName(name);
            equipment.setSerialNumber(serialNumber);
            equipment.setStudioRoom(studioRoom);
            
            if (equipment instanceof Camera camera) {
                camera.setResolution(resolution);
                camera.setCharge(charge);
            } else if (equipment instanceof Light light) {
                light.setBrightness(brightness);
                light.setColorTemperature(colorTemperature);
            }
            
            equipmentService.saveEquipment(equipment);
            
            return "redirect:/";
        } catch (Exception e) {
            System.err.println("Ошибка при редактировании оборудования: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Произошла ошибка: " + e.getMessage());
            model.addAttribute("equipment", equipmentService.findById(id).orElse(null));
            model.addAttribute("studioRooms", studioRoomService.getAll());
            return "edit-equipment";
        }
    }
    
    // Копирование оборудования по ID
    // GET /copy/{id}
    @GetMapping("/copy/{id}")
    public String copyEquipment(@PathVariable Long id) {
        equipmentService.copyEquipment(id);
        return "redirect:/";
    }
    
    // Удаление оборудования по ID
    // GET /delete/{id}
    @GetMapping("/delete/{id}")
    public String deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return "redirect:/";
    }
    
    // Просмотр всех помещений
    // GET /rooms
    @GetMapping("/rooms")
    public String showAllRooms(Model model) {
        try {
            model.addAttribute("studioRoomList", studioRoomService.getAll());
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке помещений: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("studioRoomList", new ArrayList<>());
        }
        return "studio-rooms";
    }
    
    // Показ формы добавления помещения
    // GET /rooms/add
    @GetMapping("/rooms/add")
    public String showAddRoomForm(Model model) {
        return "add-room";
    }
    
    // Обработка формы добавления помещения
    // POST /rooms/add
    @PostMapping("/rooms/add")
    public String addRoom(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double area,
            Model model) {
        try {
            StudioRoom room = new StudioRoom(name, description, area);
            studioRoomService.save(room);
            return "redirect:/rooms";
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении помещения: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Произошла ошибка: " + e.getMessage());
            model.addAttribute("name", name);
            model.addAttribute("description", description);
            model.addAttribute("area", area);
            return "add-room";
        }
    }
    
    // Показ формы редактирования помещения
    // GET /rooms/edit/{id}
    @GetMapping("/rooms/edit/{id}")
    public String showEditRoomForm(@PathVariable Long id, Model model) {
        try {
            StudioRoom room = studioRoomService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Помещение с ID " + id + " не найдено"));
            model.addAttribute("room", room);
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке помещения для редактирования: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/rooms";
        }
        return "edit-room";
    }
    
    // Обработка формы редактирования помещения
    // POST /rooms/edit
    @PostMapping("/rooms/edit")
    public String editRoom(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double area,
            Model model) {
        try {
            StudioRoom room = studioRoomService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Помещение с ID " + id + " не найдено"));
            room.setName(name);
            room.setDescription(description);
            room.setArea(area);
            studioRoomService.update(room);
            return "redirect:/rooms";
        } catch (Exception e) {
            System.err.println("Ошибка при редактировании помещения: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Произошла ошибка: " + e.getMessage());
            model.addAttribute("room", studioRoomService.findById(id).orElse(null));
            return "edit-room";
        }
    }
    
    // Удаление помещения
    // GET /rooms/delete/{id}
    @GetMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        try {
            studioRoomService.delete(id);
        } catch (Exception e) {
            System.err.println("Ошибка при удалении помещения: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/rooms";
    }
    
    // Показ формы для выбора действия оборудования
    // GET /work/{id}
    @GetMapping("/work/{id}")
    public String showWorkForm(@PathVariable Long id, Model model) {
        try {
            Equipment equipment = equipmentService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Оборудование с ID " + id + " не найдено"));
            model.addAttribute("equipment", equipment);
            model.addAttribute("equipmentType", equipment.getClass().getSimpleName());
            
            List<String> availableActions = equipment.getAvailableActions();
            model.addAttribute("availableActions", availableActions);
            
            // Создаем мапу для отображения имен действий
            Map<String, String> actionNames = new java.util.HashMap<>();
            actionNames.put("takePhoto", "Сделать фото");
            actionNames.put("recordVideo", "Записать видео");
            actionNames.put("recharge", "Зарядить батарею");
            actionNames.put("enableAutoBrightness", "Включить авто-яркость");
            actionNames.put("switchColor", "Переключить цветовую температуру");
            model.addAttribute("actionNames", actionNames);
            
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке оборудования для работы: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/";
        }
        return "work-form";
    }
    
    // Выполнение действия с оборудованием
    // POST /work
    @PostMapping("/work")
    public String performWorkAction(
            @RequestParam Long id,
            @RequestParam String action,
            Model model) {
        try {
            Equipment equipment = equipmentService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Оборудование с ID " + id + " не найдено"));
            
            String result = equipment.performAction(action);
            
            // Сохраняем изменения в базе данных
            equipmentService.saveEquipment(equipment);
            
            model.addAttribute("equipment", equipment);
            model.addAttribute("equipmentType", equipment.getClass().getSimpleName());
            model.addAttribute("action", action);
            model.addAttribute("result", result);
            
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении действия: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Произошла ошибка: " + e.getMessage());
            return "redirect:/";
        }
        return "work-result";
    }
}
