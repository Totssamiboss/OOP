#pragma once
#include <string>
#include <iostream>

using namespace std;

class Equipment {
protected:
    string name;
    string serialNumber;

public:
    Equipment();
    Equipment(string n, string sn);
    Equipment(const Equipment& other);

    string getName() const;
    void setName(string n);
    
    string getSerialNumber() const;
    void setSerialNumber(string sn);

    virtual void work() = 0; 
    virtual string getDescription() = 0;

    virtual ~Equipment();
};