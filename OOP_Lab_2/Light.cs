using System;

namespace OopLabApp
{
    public class Light : Equipment
    {
        protected int _brightness;
        protected int _colorTemperature;

        public int Brightness 
        { 
            get { return _brightness; } 
            set 
            { 
                if (value < 0 || value > 100)
                    throw new ArgumentException("Яркость прибора должна быть в диапазоне от 0 до 100%.");
                _brightness = value; 
            } 
        }

        public int ColorTemperature 
        { 
            get { return _colorTemperature; } 
            set 
            { 
                if (value < 1000 || value > 10000)
                    throw new ArgumentException("Цветовая температура должна быть в диапазоне от 1000K до 10000K.");
                _colorTemperature = value; 
            } 
        }

        // КОНСТРУКТОР 1
        public Light() : base() 
        { 
            Name = "Дефолтный свет";
            SerialNumber = "LGT-" + new Random().Next(1000, 9999);
            Brightness = 50;
            ColorTemperature = 3200;
        }

        // КОНСТРУКТОР 2
        public Light(int id, string name, string serialNumber, int brightness, int colorTemperature) 
            : base(id, name, serialNumber)
        {
            Brightness = brightness;
            ColorTemperature = colorTemperature;
        }

        // КОНСТРУКТОР 3
        public Light(Light other) : base(other)
        {
            if (other == null) throw new ArgumentNullException(nameof(other));
            _brightness = other._brightness;
            _colorTemperature = other._colorTemperature;
        }

        
        public string EnableAutoBrightness()
        {
            int oldBrightness = Brightness;
            Brightness = 100;
            return $"[*] Авто-яркость активирована!\n[>] Яркость: {oldBrightness}% -> {Brightness}%";
        }

        public string SwitchColor()
        {
            
            this.ColorTemperature = (this.ColorTemperature == 3000) ? 6000 : 3000;
            
            return $"[Настройка] Цвет изменен на {this.ColorTemperature}K";
        }

        public override string Work()
        {
            
            this.Brightness = this.Brightness > 5 ? this.Brightness - 5 : 100;

            
            string ColorLog = SwitchColor(); 
            // string AutoBrightnessLog = EnableAutoBrightness();
            return $"========================================\n" +
                $"СВЕТ: {Name}\n" +
                $"========================================\n" +
                $"[>] Устройство начало работу...\n" +
                $"{ColorLog}\n" +
                $"[Текущая яркость]: {Brightness}%\n" +
                $"[OK] Освещение активно";
        }

        public override string GetDescription()
        {
            string lightType = (ColorTemperature <= 4000) ? "Тёплый" : "Холодный";
            return $"Тип: Свет | Название: {Name} | Серийный номер: {SerialNumber} " +
                   $"| Яркость: {Brightness}% | Цвет: {ColorTemperature}K ({lightType})";
        }
    }
}