#include "Equipment.h"

Equipment::Equipment() : name("Без названия"), serialNumber("000000") {}

Equipment::Equipment(string n, string sn) 
    : name(n), serialNumber(sn) {}

Equipment::Equipment(const Equipment& other) 
    : name(other.name), serialNumber(other.serialNumber) {}

string Equipment::getName() const { return name; }
void Equipment::setName(string n) { name = n; }

string Equipment::getSerialNumber() const { return serialNumber; }
void Equipment::setSerialNumber(string sn) { serialNumber = sn; }

Equipment::~Equipment() {}