using System;
using System.Collections.Generic;
using System.Data;
using System.Drawing;
using System.Windows.Forms;

namespace OopLabApp
{
    public partial class Form1 : Form
    {
        private DatabaseManager dbManager = new DatabaseManager();
        private List<Equipment> equipmentList = new List<Equipment>();

        private DataGridView grid;
        private TextBox txtName;
        private TextBox txtSerial;
        private ComboBox cmbType;
        
        private Label lblParam1;
        private TextBox txtParam1;
        private Label lblParam2;
        private TextBox txtParam2;

        
        private Label lblAction;
        private ComboBox cmbAction;

        private Button btnAdd;
        private Button btnEdit; 
        private Button btnDelete;
        private Button btnWork; 
        private Button btnRefresh; 
        private Button btnClone; 

        public Form1()
        {
            this.Text = "Управление студийным оборудованием (ООП Лаб 2)";
            this.Size = new Size(900, 580); 
            this.MinimumSize = new Size(900, 580);
            this.StartPosition = FormStartPosition.CenterScreen;

            InitializeComponents();
        }

        private void InitializeComponents()
        {
            // 1. Таблица данных
            grid = new DataGridView();
            grid.Location = new Point(20, 20);
            grid.Size = new Size(570, 490); 
            grid.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            grid.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            grid.MultiSelect = false;
            grid.ReadOnly = true;
            grid.Anchor = AnchorStyles.Top | AnchorStyles.Bottom | AnchorStyles.Left | AnchorStyles.Right;
            grid.CellClick += Grid_CellClick; 
            this.Controls.Add(grid);

            // 2. Панель управления справа
            int startX = 610;

            Label lblName = new Label() { Text = "Название устройства:", Location = new Point(startX, 20), Size = new Size(150, 20) };
            txtName = new TextBox() { Location = new Point(startX, 40), Size = new Size(250, 20) };
            
            Label lblSerial = new Label() { Text = "Серийный номер:", Location = new Point(startX, 70), Size = new Size(150, 20) };
            txtSerial = new TextBox() { Location = new Point(startX, 90), Size = new Size(250, 20) };

            Label lblType = new Label() { Text = "Тип оборудования:", Location = new Point(startX, 120), Size = new Size(150, 20) };
            cmbType = new ComboBox() { Location = new Point(startX, 140), Size = new Size(250, 20), DropDownStyle = ComboBoxStyle.DropDownList };
            cmbType.Items.AddRange(new string[] { "Камера", "Свет" });
            cmbType.SelectedIndexChanged += CmbType_SelectedIndexChanged; 

            lblParam1 = new Label() { Text = "Разрешение (н-р: 4K):", Location = new Point(startX, 180), Size = new Size(250, 20) };
            txtParam1 = new TextBox() { Location = new Point(startX, 200), Size = new Size(250, 20) };

            lblParam2 = new Label() { Text = "Заряд батареи (%):", Location = new Point(startX, 230), Size = new Size(250, 20) };
            txtParam2 = new TextBox() { Location = new Point(startX, 250), Size = new Size(250, 20) };

            // Кнопки управления данными
            btnAdd = new Button() { Text = "Добавить в БД", Location = new Point(startX, 285), Size = new Size(250, 28), BackColor = Color.LightGreen };
            btnAdd.Click += BtnAdd_Click;

            btnEdit = new Button() { Text = "✏️ Изменить выбранное", Location = new Point(startX, 318), Size = new Size(250, 28), BackColor = Color.LightSalmon };
            btnEdit.Click += BtnEdit_Click;

            btnDelete = new Button() { Text = "Удалить выбранное", Location = new Point(startX, 351), Size = new Size(250, 28), BackColor = Color.LightCoral };
            btnDelete.Click += BtnDelete_Click;

            // Выпадающий список выбора функции класса 
            lblAction = new Label() { Text = "Выберите метод класса:", Location = new Point(startX, 390), Size = new Size(250, 20), Font = new Font(this.Font, FontStyle.Underline) };
            cmbAction = new ComboBox() { Location = new Point(startX, 410), Size = new Size(250, 20), DropDownStyle = ComboBoxStyle.DropDownList };

            // Кнопка вызова выбранного метода
            btnWork = new Button() { Text = "🚀 Вызвать выбранный метод", Location = new Point(startX, 440), Size = new Size(250, 35), Font = new Font(this.Font, FontStyle.Bold), BackColor = Color.LightYellow };
            btnWork.Click += BtnWork_Click;

            // Кнопки клонирования и обновления
            btnClone = new Button() { Text = "👯 Клонировать выбранное", Location = new Point(startX, 485), Size = new Size(250, 25), BackColor = Color.LightGoldenrodYellow };
            btnClone.Click += BtnClone_Click;

            btnRefresh = new Button() { Text = "🔄 Обновить данные из БД", Location = new Point(startX, 515), Size = new Size(250, 25), BackColor = Color.LightSkyBlue };
            btnRefresh.Click += BtnRefresh_Click;

            this.Controls.AddRange(new Control[] { 
                lblName, txtName, lblSerial, txtSerial, lblType, cmbType, 
                lblParam1, txtParam1, lblParam2, txtParam2, 
                lblAction, cmbAction, // Добавили на форму
                btnAdd, btnEdit, btnDelete, btnWork, btnClone, btnRefresh 
            });

            foreach (Control ctrl in this.Controls)
            {
                if (ctrl != grid) ctrl.Anchor = AnchorStyles.Top | AnchorStyles.Right;
            }

            cmbType.SelectedIndex = 0; 
            this.Load += Form1_Load;
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            RefreshGrid();
        }

        private void RefreshGrid()
        {
            equipmentList = dbManager.GetAllEquipment();

            DataTable dt = new DataTable();
            dt.Columns.Add("id", typeof(int));
            dt.Columns.Add("name", typeof(string));
            dt.Columns.Add("serial_number", typeof(string));
            dt.Columns.Add("type", typeof(string));
            dt.Columns.Add("resolution", typeof(string));
            dt.Columns.Add("charge", typeof(string));
            dt.Columns.Add("brightness", typeof(string));
            dt.Columns.Add("color_temperature", typeof(string));

            foreach (var eq in equipmentList)
            {
                DataRow row = dt.NewRow();
                row["id"] = eq.Id;
                row["name"] = eq.Name;
                row["serial_number"] = eq.SerialNumber;

                if (eq is Camera cam)
                {
                    row["type"] = "Камера";
                    row["resolution"] = cam.Resolution;
                    row["charge"] = cam.Charge + "%";
                    row["brightness"] = "—";
                    row["color_temperature"] = "—";
                }
                else if (eq is Light light)
                {
                    row["type"] = "Свет";
                    row["resolution"] = "—";
                    row["charge"] = "—";
                    row["brightness"] = light.Brightness + "%";
                    row["color_temperature"] = light.ColorTemperature + "K";
                }

                dt.Rows.Add(row);
            }

            grid.DataSource = dt;
            
            if (grid.Columns.Count > 0)
            {
                grid.Columns["id"].HeaderText = "ID";
                grid.Columns["name"].HeaderText = "Название";
                grid.Columns["serial_number"].HeaderText = "Серийный номер";
                grid.Columns["type"].HeaderText = "Тип прибора";
                grid.Columns["resolution"].HeaderText = "Разрешение";
                grid.Columns["charge"].HeaderText = "Заряд";
                grid.Columns["brightness"].HeaderText = "Яркость";
                grid.Columns["color_temperature"].HeaderText = "Цв. температура";
            }
        }

        
        private void Grid_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (grid.CurrentRow == null || e.RowIndex < 0) return;

            try
            {
                int id = Convert.ToInt32(grid.CurrentRow.Cells["id"].Value);
                Equipment selected = equipmentList.Find(eq => eq.Id == id);

                if (selected == null) return;

                txtName.Text = selected.Name;
                txtSerial.Text = selected.SerialNumber;

                // Очищаем комбобокс перед добавлением новых функций
                cmbAction.Items.Clear();

                if (selected is Camera cam)
                {
                    cmbType.SelectedIndex = 0; 
                    txtParam1.Text = cam.Resolution;
                    txtParam2.Text = cam.Charge.ToString();

                    
                    cmbAction.Items.AddRange(new string[] { "TakePhoto()", "RecordVideo()", "Recharge()" });
                }
                else if (selected is Light light)
                {
                    cmbType.SelectedIndex = 1;
                    txtParam1.Text = light.Brightness.ToString();
                    txtParam2.Text = light.ColorTemperature.ToString();

                    
                    cmbAction.Items.AddRange(new string[] { "EnableAutoBrightness()", "SwitchColor()" });
                }

                // Автоматически выбираем первый метод из списка
                if (cmbAction.Items.Count > 0) cmbAction.SelectedIndex = 0;
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ошибка выбора строки: {ex.Message}");
            }
        }

        
        private void BtnWork_Click(object sender, EventArgs e)
        {
            if (grid.CurrentRow == null)
            {
                MessageBox.Show("Сначала выберите устройство в таблице!", "Внимание");
                return;
            }
            if (cmbAction.SelectedItem == null)
            {
                MessageBox.Show("Выберите метод для выполнения!", "Внимание");
                return;
            }

            try
            {
                int id = Convert.ToInt32(grid.CurrentRow.Cells["id"].Value);
                Equipment currentDevice = equipmentList.Find(eq => eq.Id == id);

                if (currentDevice == null) return;

                string selectedMethod = cmbAction.SelectedItem.ToString();
                string resultMessage = ""; // Сюда запишем ответ из твоего метода

                // Проверяем тип устройства и вызываем ОРИГИНАЛЬНЫЙ метод
                if (currentDevice is Camera cam)
                {
                    switch (selectedMethod)
                    {
                        case "TakePhoto()":
                            resultMessage = cam.TakePhoto(); // Вызов твоего метода
                            dbManager.UpdateCameraCharge(cam.Id, cam.Charge); // Сохраняем новый заряд в БД
                            break;

                        case "RecordVideo()":
                            resultMessage = cam.RecordVideo(); // Вызов твоего метода
                        
                            break;

                        case "Recharge()":
                            resultMessage = cam.Recharge(); // Вызов твоего метода
                            dbManager.UpdateCameraCharge(cam.Id, cam.Charge); // Сохраняем 100% заряд в БД
                            break;
                    }
                }
                else if (currentDevice is Light light)
                {
                    switch (selectedMethod)
                    {
                        case "EnableAutoBrightness()":
                            resultMessage = light.EnableAutoBrightness(); // Вызов твоего метода
                            dbManager.UpdateLightState(light.Id, light.Brightness, light.ColorTemperature); // Сохраняем яркость 100 в БД
                            break;

                        case "SwitchColor()":
                            resultMessage = light.SwitchColor(); // Вызов твоего метода
                            dbManager.UpdateLightState(light.Id, light.Brightness, light.ColorTemperature); // Сохраняем новый цвет в БД
                            break;
                    }
                }

                // Показываем в MessageBox именно то, что вернул класс
                if (!string.IsNullOrEmpty(resultMessage))
                {
                    MessageBox.Show(resultMessage, "Результат работы метода класса", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }

                
                RefreshGrid();
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ошибка при вызове метода: {ex.Message}", "Ошибка");
            }
        }

        private void BtnEdit_Click(object sender, EventArgs e)
        {
            if (grid.CurrentRow == null)
            {
                MessageBox.Show("Сначала выберите устройство в таблице для изменения!", "Предупреждение");
                return;
            }

            try
            {
                int id = Convert.ToInt32(grid.CurrentRow.Cells["id"].Value);
                Equipment selected = equipmentList.Find(eq => eq.Id == id);

                if (selected == null) return;

                selected.Name = txtName.Text.Trim();
                selected.SerialNumber = txtSerial.Text.Trim();

                if (selected is Camera cam)
                {
                    cam.Resolution = txtParam1.Text.Trim();
                    cam.Charge = Convert.ToInt32(txtParam2.Text);
                }
                else if (selected is Light light)
                {
                    light.Brightness = Convert.ToInt32(txtParam1.Text);
                    light.ColorTemperature = Convert.ToInt32(txtParam2.Text);
                }

                dbManager.UpdateEquipment(selected);
                RefreshGrid();
                MessageBox.Show("Данные успешно сохранены в базе данных!", "Успех");
            }
            catch (FormatException)
            {
                MessageBox.Show("Числовые поля (заряд, яркость, температура) должны содержать только цифры!", "Ошибка ввода");
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ошибка сохранения изменений: {ex.Message}", "Ошибка");
            }
        }

        private void CmbType_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (cmbType.SelectedItem.ToString() == "Камера")
            {
                lblParam1.Text = "Разрешение (н-р: 4K):";
                txtParam1.Text = "HD";
                lblParam2.Text = "Заряд батареи (%):";
                txtParam2.Text = "100";
            }
            else
            {
                lblParam1.Text = "Яркость прибора (%):";
                txtParam1.Text = "50";
                lblParam2.Text = "Цветовая температура (K):";
                txtParam2.Text = "3000";
            }
        }

        private void BtnAdd_Click(object sender, EventArgs e)
        {
            try
            {
                string name = txtName.Text.Trim();
                string serial = txtSerial.Text.Trim();
                string type = cmbType.SelectedItem.ToString();

                bool useDefault = string.IsNullOrEmpty(name) && string.IsNullOrEmpty(serial);

                if (type == "Камера")
                {
                    Camera newCam;
                    if (useDefault) { newCam = new Camera(); }
                    else
                    {
                        string res = txtParam1.Text;
                        int charge = Convert.ToInt32(txtParam2.Text);
                        newCam = new Camera(0, name, serial, res, charge); 
                    }
                    dbManager.AddEquipment(newCam);
                }
                else 
                {
                    Light newLight;
                    if (useDefault) { newLight = new Light(); }
                    else
                    {
                        int bright = Convert.ToInt32(txtParam1.Text);
                        int temp = Convert.ToInt32(txtParam2.Text);
                        newLight = new Light(0, name, serial, bright, temp); 
                    }
                    dbManager.AddEquipment(newLight);
                }

                RefreshGrid();
                MessageBox.Show("Устройство успешно добавлено!", "Успех");
            }
            catch (Exception ex) 
            { 
                MessageBox.Show($"Ошибка добавления: {ex.Message}", "Ошибка"); 
            }
        }

        private void BtnClone_Click(object sender, EventArgs e)
        {
            if (grid.CurrentRow == null)
            {
                MessageBox.Show("Выберите прибор из таблицы для клонирования!");
                return;
            }

            try
            {
                int id = Convert.ToInt32(grid.CurrentRow.Cells["id"].Value);
                Equipment selected = equipmentList.Find(eq => eq.Id == id);

                if (selected == null) return;

                if (selected is Camera cam)
                {
                    Camera clonedCam = new Camera(cam); 
                    clonedCam.Name += " (Копия)"; 
                    dbManager.AddEquipment(clonedCam);
                }
                else if (selected is Light light)
                {
                    Light clonedLight = new Light(light);
                    clonedLight.Name += " (Копия)";
                    dbManager.AddEquipment(clonedLight);
                }

                RefreshGrid();
                MessageBox.Show("Объект успешно скопирован через конструктор копирования и занесен в БД!", "Конструктор копирования");
            }
            catch (Exception ex) 
            { 
                MessageBox.Show($"Ошибка клонирования: {ex.Message}", "Ошибка"); 
            }
        }

        private void BtnDelete_Click(object sender, EventArgs e)
        {
            if (grid.CurrentRow == null) return;
            
            try
            {
                int id = Convert.ToInt32(grid.CurrentRow.Cells["id"].Value);
                dbManager.DeleteEquipment(id);
                RefreshGrid();
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ошибка при удалении: {ex.Message}", "Ошибка");
            }
        }

        private void BtnRefresh_Click(object sender, EventArgs e)
        {
            try
            {
                RefreshGrid();
                MessageBox.Show("Данные успешно синхронизированы с PostgreSQL!", "Обновление");
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Ошибка обновления: {ex.Message}", "Ошибка");
            }
        }
    }
}