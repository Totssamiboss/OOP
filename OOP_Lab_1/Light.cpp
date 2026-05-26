#include "Light.h"
#include <iostream>

Light::Light() : Equipment(), brightness(50), colorTemp(3000) {}

Light::Light(string n, string sn, int br, int ct) 
    : Equipment(n, sn), brightness(br), colorTemp(ct) {}

Light::Light(const Light& other) 
    : Equipment(other), brightness(other.brightness), colorTemp(other.colorTemp) {}

int Light::getBrightness() const { return brightness; }
void Light::setBrightness(int val) { brightness = val; }
int Light::getColorTemp() const { return colorTemp; }
void Light::setColorTemp(int val) { colorTemp = val; }

void Light::enableAutoBrightness() {
    int oldBrightness = brightness;
    brightness = 100;
    cout << "  [*] Авто-яркость активирована!" << endl;
    cout << "  [>] Яркость: " << oldBrightness << "% -> " << brightness << "%" << endl;
}

void Light::switchColor() {
    int oldTemp = colorTemp;
    colorTemp = (colorTemp == 3000) ? 6000 : 3000;
    cout << "  [#] Режим цвета изменён!" << endl;
    cout << "  [>] Температура: " << oldTemp << "K -> " << colorTemp << "K" << endl;
}

void Light::work() {
    cout << endl;
    cout << "========================================" << endl;
    cout << "СВЕТ: " << getName() << endl;
    cout << "========================================" << endl;
    cout << "  [>] Устройство начало работу..." << endl;
    enableAutoBrightness();
    cout << "  [OK] Освещение активно" << endl;
}

string Light::getDescription() {
    string lightType = (colorTemp <= 4000) ? "Тёплый" : "Холодный";
    return "Тип: Свет | Название: " + getName() + 
           " | Серийный номер: " + getSerialNumber() + 
           " | Яркость: " + to_string(brightness) + "%" +
           " | Цвет: " + to_string(colorTemp) + "K (" + lightType + ")";
}