#include "Camera.h"
#include <iostream>

Camera::Camera() : Equipment(), resolution("HD"), charge(0) {}

Camera::Camera(string n, string sn, string res, int ch) 
    : Equipment(n, sn), resolution(res), charge(ch) {}

Camera::Camera(const Camera& other) 
    : Equipment(other), resolution(other.resolution), charge(other.charge) {}

string Camera::getResolution() const { return resolution; }
void Camera::setResolution(string val) { resolution = val; }
int Camera::getCharge() const { return charge; }
void Camera::setCharge(int val) { charge = val; }

void Camera::takePhoto() {
    if (charge > 0) {
        cout << "  [*] Фото успешно сделано!" << endl;
        cout << "  [^] Заряд уменьшен на 1% (осталось: " << (charge-1) << "%)" << endl;
        charge--;
    } else {
        cout << "  [X] ОШИБКА: Батарея разряжена! Требуется подзарядка." << endl;
    }
}

void Camera::recordVideo() {
    cout << "  [#] Начата запись видео в разрешении " << resolution << endl;
}

void Camera::recharge() {
    int oldCharge = charge;
    charge = 100;
    cout << "  [+] Подзарядка завершена!" << endl;
    cout << "  [>] Было: " << oldCharge << "% -> Стало: " << charge << "%" << endl;
}

void Camera::work() {
    cout << endl;
    cout << "========================================" << endl;
    cout << "КАМЕРА: " << getName() << endl;
    cout << "========================================" << endl;
    cout << "  [>] Устройство начало работу..." << endl;
    takePhoto();
    cout << "  [OK] Камера готова к съемке" << endl;
}

string Camera::getDescription() {
    return "Тип: Камера | Название: " + getName() + 
           " | Серийный номер: " + getSerialNumber() + 
           " | Разрешение: " + resolution + 
           " | Заряд: " + to_string(charge) + "%";
}