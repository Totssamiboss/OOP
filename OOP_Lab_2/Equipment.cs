using System;

namespace OopLabApp
{
    public abstract class Equipment
    {
        
        protected int _id;
        protected string _name;
        protected string _serialNumber;

        
        public int Id { get { return _id; } set { _id = value; } }

        public string Name 
        { 
            get { return _name; } 
            set 
            { 
                if (string.IsNullOrWhiteSpace(value))
                    throw new ArgumentException("Название устройства не может быть пустым.");
                _name = value; 
            } 
        }

        public string SerialNumber 
        { 
            get { return _serialNumber; } 
            set 
            { 
                if (string.IsNullOrWhiteSpace(value))
                    throw new ArgumentException("Серийный номер не может быть пустым.");
                _serialNumber = value; 
            } 
        }

        // КОНСТРУКТОР 1
        public Equipment() 
       { 
            Name = "Новое устройство";
            SerialNumber = "SN-DEF-" + new Random().Next(1000, 9999); 
       }

        // КОНСТРУКТОР 2
        public Equipment(int id, string name, string serialNumber)
        {
            Id = id;
            Name = name; 
            SerialNumber = serialNumber;
        }

        // КОНСТРУКТОР 3
        public Equipment(Equipment other)
        {
            if (other == null) throw new ArgumentNullException(nameof(other));
            Id = other.Id;
            Name = other.Name;
            SerialNumber = other.SerialNumber;
        }

        // Полиморфные методы
        public abstract string Work();
        public abstract string GetDescription();
    }
}