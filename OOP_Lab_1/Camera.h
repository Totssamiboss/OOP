#pragma once
#include "Equipment.h"

class Camera : public Equipment {
protected:
    string resolution;
    int charge;

public:
    Camera();
    Camera(string n, string sn, string res, int ch);
    Camera(const Camera& other);

    string getResolution() const;
    void setResolution(string val);
    int getCharge() const;
    void setCharge(int val);

    void takePhoto();
    void recordVideo();
    void recharge();

    void work() override;
    string getDescription() override;
};