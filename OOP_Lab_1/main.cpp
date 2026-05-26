#include <iostream>
#include <string>
#include <windows.h>
#include "Equipment.h"
#include "Camera.h"
#include "Light.h"

using namespace std;

int main() {
    // Настройка консоли (UTF-8 для русского языка)
    SetConsoleOutputCP(65001);
    setlocale(LC_ALL, "Russian");

    cout << "============================================================" << endl;
    cout << "|              СИСТЕМА УПРАВЛЕНИЯ ОБОРУДОВАНИЕМ            |" << endl;
    cout << "============================================================" << endl;

    Camera* camTest3 = new Camera("Canon EOS", "SN-003", "FullHD", 50);
    // camTest3->work();

    Light* lightTest = new Light("Philips Hue", "SN-101", 6, 3000);
    // lightTest->work();

    // Фиксируем (задаем) параметры прямо здесь
    Equipment* equipmentList[8] = {
        new Camera(),
        new Camera(*camTest3),
        new Camera("Canon EOS", "SN-003", "FullHD", 50),
        new Camera("Nikon Z6", "SN-004", "4K", 30),
        new Light(*lightTest),
        new Light("Xiaomi Lamp", "SN-102", 75, 4500),
        new Light("ERA Light", "SN-103", 50, 6000),
        new Light("Raylab LED", "SN-104", 20, 3200)
    };
    
    // ДОБАВЛЕНИЕ В МАССИВ
    Equipment* rrr[8];
    // rrr[0] = equipmentList[1];
    // rrr[1] = equipmentList[3];

    
  
    // equipmentList[2]->work();
    // equipmentList[2]->getDescription();
    
    cout << "============================================================" << endl;
    // static_cast<Camera*>(equipmentList[4])->takePhoto(); 
    cout << "============================================================" << endl;
     dynamic_cast<Camera*>(equipmentList[4])->takePhoto();
     cout << "============================================================" << endl;


    
    // // dynamic_cast<Camera*>(equipmentList[2])->takePhoto(); 
    // // dynamic_cast<Camera*>(equipmentList[3])->recordVideo(); 
    
    // // // Обращение как к элементам массива (по индексу)
    // cout << "\n--- РАБОТА УСТРОЙСТВ (ПОЛИМОРФИЗМ) ---" << endl;
    // equipmentList[1]->work(); // Вызов метода Camera
    // equipmentList[4]->work(); // Вызов метода Light

    // cout << "\n--- ДЕМОНСТРАЦИЯ ИНКАПСУЛЯЦИИ (СЕТТЕРЫ И ГЕТТЕРЫ) ---" << endl;
    
    // // Показываем работу сеттеров базового класса
    // cout << "[До] Имя прибора [0]: " << equipmentList[0]->getName() << endl;
    // equipmentList[0]->setName("НОВОЕ ИМЯ SONY"); // Сеттер setName
    // cout << "[После] Имя прибора [0]: " << equipmentList[0]->getName() << endl;

    

    // // STATIC_CAST: Принудительно приводим к Light (ОПАСНО, если индекс неверный)
    // Light* s_light = static_cast<Light*>(equipmentList[0]);
    // cout << "\n[Static Cast]: Успешно приведен к Light. Смена цвета..." << endl;
    // s_light->switchColor();

    cout << "\n============================================================" << endl;
    system("pause");

    // ПРИНУДИТЕЛЬНАЯ ОЧИСТКА ПАМЯТИ (Освобождаем кучу)
    cout << "\n[ОЧИСТКА ПАМЯТИ]:" << endl;
    for (int i = 0; i < 8; i++) {
        if (equipmentList[i] != nullptr) {
            cout << "  Удаление объекта [" << i << "]: " << equipmentList[i]->getName() << endl;
            delete equipmentList[i];
        }
    }

    return 0;
}