using System;

namespace OopLabApp
{
    public class Camera : Equipment
    {
        protected string _resolution;
        protected int _charge;

        public string Resolution 
        { 
            get { return _resolution; } 
            set 
            { 
                if (string.IsNullOrWhiteSpace(value))
                    throw new ArgumentException("Разрешение камеры должно быть указано.");
                _resolution = value; 
            } 
        }

        public int Charge 
        { 
            get { return _charge; } 
            set 
            { 
                if (value < 0 || value > 100)
                    throw new ArgumentException("Заряд батареи должен быть в диапазоне от 0 до 100%.");
                _charge = value; 
            } 
        }

        // КОНСТРУКТОР 1
        public Camera() : base() 
        { 
            Name = "Дефолтная камера";
            SerialNumber = "CAM-" + new Random().Next(1000, 9999);
            Resolution = "HD";
            Charge = 100;
        }

        // КОНСТРУКТОР 2
        public Camera(int id, string name, string serialNumber, string resolution, int charge) 
            : base(id, name, serialNumber)
        {
            Resolution = resolution;
            Charge = charge;
        }

        // КОНСТРУКТОР 3
        public Camera(Camera other) : base(other)
        {
            if (other == null) throw new ArgumentNullException(nameof(other));
            _resolution = other._resolution;
            _charge = other._charge;
        }

        
        public string TakePhoto()
        {
            if (Charge > 0)
            {
                Charge--; // Уменьшаем заряд
                return $"[*] Фото успешно сделано!\n[^] Заряд уменьшен на 1% (осталось: {Charge}%)";
            }
            else
            {
                return "[X] ОШИБКА: Батарея разряжена! Требуется подзарядка.";
            }
        }

        public string RecordVideo()
        {
            return $"[#] Начата запись видео в разрешении {Resolution}";
        }

        public string Recharge()
        {
            int oldCharge = Charge;
            Charge = 100;
            return $"[+] Подзарядка завершена!\n[>] Было: {oldCharge}% -> Стало: {Charge}%";
        }

        
        public override string Work()
        {
            string photoLog = TakePhoto();
            // string RecordLog = RecordVideo();
            // string RechargeLog = Recharge();
            return $"========================================\n" +
                   $"КАМЕРА: {Name}\n" +
                   $"========================================\n" +
                   $"[>] Устройство начало работу...\n" +
                   $"{photoLog}\n" +
                   $"[OK] Камера готова к съемке";
        }

        public override string GetDescription()
        {
            return $"Тип: Камера | Название: {Name} | Серийный номер: {SerialNumber} " +
                   $"| Разрешение: {Resolution} | Заряд: {Charge}%";
        }
    }
}