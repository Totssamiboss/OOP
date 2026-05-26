#pragma once
#include "Equipment.h"

class Light : public Equipment {
protected:
    int brightness;
    int colorTemp;

public:
    Light();
    Light(string n, string sn, int br, int ct);
    Light(const Light& other);

    int getBrightness() const;
    void setBrightness(int val);
    int getColorTemp() const;
    void setColorTemp(int val);

    void enableAutoBrightness();
    void switchColor();

    void work() override;
    string getDescription() override;
};