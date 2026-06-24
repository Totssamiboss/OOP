package com.equipment;

import com.equipment.entity.Camera;
import com.equipment.entity.Equipment;
import com.equipment.entity.Light;
import com.equipment.entity.StudioRoom;
import com.equipment.repository.EquipmentRepository;
import com.equipment.repository.StudioRoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final StudioRoomRepository studioRoomRepository;
    private final EquipmentRepository equipmentRepository;

    public DatabaseInitializer(StudioRoomRepository studioRoomRepository, 
                               EquipmentRepository equipmentRepository) {
        this.studioRoomRepository = studioRoomRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, есть ли уже данные
        if (studioRoomRepository.count() == 0) {
            // Создаем помещения
            StudioRoom room1 = new StudioRoom();
            room1.setName("Студия 1");
            room1.setArea(50.0);
            room1.setDescription("Основная фотостудия");
            studioRoomRepository.save(room1);

            StudioRoom room2 = new StudioRoom();
            room2.setName("Студия 2");
            room2.setArea(30.0);
            room2.setDescription("Малая фотостудия");
            studioRoomRepository.save(room2);

            // Создаем оборудование для первой студии
            Camera camera1 = new Camera();
            camera1.setName("Canon EOS R5");
            camera1.setSerialNumber("CAN-001");
            camera1.setStudioRoom(room1);
            camera1.setCharge(100);
            camera1.setResolution(45);
            equipmentRepository.save(camera1);

            Light light1 = new Light();
            light1.setName("Softbox Light 1");
            light1.setSerialNumber("LGT-001");
            light1.setStudioRoom(room1);
            light1.setBrightness(80);
            light1.setColorTemperature(5600);
            equipmentRepository.save(light1);

            // Создаем оборудование для второй студии
            Camera camera2 = new Camera();
            camera2.setName("Sony A7 IV");
            camera2.setSerialNumber("SON-001");
            camera2.setStudioRoom(room2);
            camera2.setCharge(95);
            camera2.setResolution(33);
            equipmentRepository.save(camera2);

            Light light2 = new Light();
            light2.setName("LED Panel");
            light2.setSerialNumber("LGT-002");
            light2.setStudioRoom(room2);
            light2.setBrightness(90);
            light2.setColorTemperature(5000);
            equipmentRepository.save(light2);

            System.out.println("База данных заполнена начальными данными");
        }
    }
}
