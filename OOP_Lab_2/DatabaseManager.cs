using Npgsql;
using System;
using System.Collections.Generic;

namespace OopLabApp
{
    public class DatabaseManager
    {
        private string connectionString = "Host=localhost;Port=5432;Database=oop_lab_db;Username=postgres;Password=titov16!";

        public List<Equipment> GetAllEquipment()
        {
            List<Equipment> list = new List<Equipment>();

            using (NpgsqlConnection conn = new NpgsqlConnection(connectionString))
            {
                conn.Open();

                // Читаем камеры
                string cameraQuery = @"
                    SELECT e.id, e.name, e.serial_number, c.resolution, c.charge 
                    FROM equipment e 
                    JOIN cameras c ON e.id = c.equipment_id";
                using (NpgsqlCommand cmd = new NpgsqlCommand(cameraQuery, conn))
                using (NpgsqlDataReader reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        list.Add(new Camera(
                            reader.GetInt32(0), reader.GetString(1), reader.GetString(2),
                            reader.GetString(3), reader.GetInt32(4)
                        ));
                    }
                }

                // Читаем свет
                string lightQuery = @"
                    SELECT e.id, e.name, e.serial_number, l.brightness, l.color_temperature 
                    FROM equipment e 
                    JOIN lights l ON e.id = l.equipment_id";
                using (NpgsqlCommand cmd = new NpgsqlCommand(lightQuery, conn))
                using (NpgsqlDataReader reader = cmd.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        list.Add(new Light(
                            reader.GetInt32(0), reader.GetString(1), reader.GetString(2),
                            reader.GetInt32(3), reader.GetInt32(4)
                        ));
                    }
                }
            }

            list.Sort((x, y) => x.Id.CompareTo(y.Id));
            return list;
        }

        public void AddEquipment(Equipment eq)
        {
            using (NpgsqlConnection conn = new NpgsqlConnection(connectionString))
            {
                conn.Open();
                using (var transaction = conn.BeginTransaction())
                {
                    try
                    {
                        // Вставка в базовую таблицу и получение ID
                        string baseQuery = "INSERT INTO equipment (name, serial_number, device_type) VALUES (@name, @sn, @type) RETURNING id";
                        int newId = 0;
                        using (NpgsqlCommand cmd = new NpgsqlCommand(baseQuery, conn))
                        {
                            cmd.Parameters.AddWithValue("name", (object)eq.Name ?? DBNull.Value);
                            cmd.Parameters.AddWithValue("sn", (object)eq.SerialNumber ?? DBNull.Value);
                            cmd.Parameters.AddWithValue("type", eq is Camera ? "Camera" : "Light");
                            newId = (int)cmd.ExecuteScalar();
                        }

                        // Вставка в дочернюю таблицу
                        if (eq is Camera cam)
                        {
                            string childQuery = "INSERT INTO cameras (equipment_id, resolution, charge) VALUES (@id, @res, @charge)";
                            using (NpgsqlCommand cmd = new NpgsqlCommand(childQuery, conn))
                            {
                                cmd.Parameters.AddWithValue("id", newId);
                                cmd.Parameters.AddWithValue("res", (object)cam.Resolution ?? DBNull.Value);
                                cmd.Parameters.AddWithValue("charge", cam.Charge);
                                cmd.ExecuteNonQuery();
                            }
                        }
                        else if (eq is Light light)
                        {
                            string childQuery = "INSERT INTO lights (equipment_id, brightness, color_temperature) VALUES (@id, @br, @temp)";
                            using (NpgsqlCommand cmd = new NpgsqlCommand(childQuery, conn))
                            {
                                cmd.Parameters.AddWithValue("id", newId);
                                cmd.Parameters.AddWithValue("br", light.Brightness);
                                cmd.Parameters.AddWithValue("temp", light.ColorTemperature);
                                cmd.ExecuteNonQuery();
                            }
                        }
                        transaction.Commit();
                        eq.Id = newId;
                    }
                    catch
                    {
                        transaction.Rollback();
                        throw;
                    }
                }
            }
        }

        
        public void UpdateEquipment(Equipment eq)
        {
            using (NpgsqlConnection conn = new NpgsqlConnection(connectionString))
            {
                conn.Open();
                using (var transaction = conn.BeginTransaction())
                {
                    try
                    {
                        // 1. Обновляем базовую таблицу equipment
                        string baseQuery = "UPDATE equipment SET name = @name, serial_number = @sn WHERE id = @id";
                        using (NpgsqlCommand cmd = new NpgsqlCommand(baseQuery, conn))
                        {
                            cmd.Parameters.AddWithValue("name", (object)eq.Name ?? DBNull.Value);
                            cmd.Parameters.AddWithValue("sn", (object)eq.SerialNumber ?? DBNull.Value);
                            cmd.Parameters.AddWithValue("id", eq.Id);
                            cmd.ExecuteNonQuery();
                        }

                        // 2. Обновляем соответствующую дочернюю таблицу
                        if (eq is Camera cam)
                        {
                            string childQuery = "UPDATE cameras SET resolution = @res, charge = @charge WHERE equipment_id = @id";
                            using (NpgsqlCommand cmd = new NpgsqlCommand(childQuery, conn))
                            {
                                cmd.Parameters.AddWithValue("res", (object)cam.Resolution ?? DBNull.Value);
                                cmd.Parameters.AddWithValue("charge", cam.Charge);
                                cmd.Parameters.AddWithValue("id", cam.Id);
                                cmd.ExecuteNonQuery();
                            }
                        }
                        else if (eq is Light light)
                        {
                            string childQuery = "UPDATE lights SET brightness = @br, color_temperature = @temp WHERE equipment_id = @id";
                            using (NpgsqlCommand cmd = new NpgsqlCommand(childQuery, conn))
                            {
                                cmd.Parameters.AddWithValue("br", light.Brightness);
                                cmd.Parameters.AddWithValue("temp", light.ColorTemperature);
                                cmd.Parameters.AddWithValue("id", light.Id);
                                cmd.ExecuteNonQuery();
                            }
                        }

                        transaction.Commit();
                    }
                    catch
                    {
                        transaction.Rollback();
                        throw;
                    }
                }
            }
        }

        public void DeleteEquipment(int id)
        {
            using (NpgsqlConnection conn = new NpgsqlConnection(connectionString))
            {
                conn.Open();
                string query = "DELETE FROM equipment WHERE id = @id";
                using (NpgsqlCommand cmd = new NpgsqlCommand(query, conn))
                {
                    cmd.Parameters.AddWithValue("id", id);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public void UpdateCameraCharge(int id, int newCharge)
        {
            using (NpgsqlConnection conn = new NpgsqlConnection(connectionString))
            {
                conn.Open();
                string sql = "UPDATE cameras SET charge = @charge WHERE equipment_id = @id";
                using (NpgsqlCommand cmd = new NpgsqlCommand(sql, conn))
                {
                    cmd.Parameters.AddWithValue("charge", newCharge);
                    cmd.Parameters.AddWithValue("id", id);
                    cmd.ExecuteNonQuery();
                }
            }
        }

        public void UpdateLightState(int id, int newBrightness, int newTemperature)
        {
            using (NpgsqlConnection conn = new NpgsqlConnection(connectionString))
            {
                conn.Open();
                string sql = "UPDATE lights SET brightness = @brightness, color_temperature = @temp WHERE equipment_id = @id";
                using (NpgsqlCommand cmd = new NpgsqlCommand(sql, conn))
                {
                    cmd.Parameters.AddWithValue("brightness", newBrightness);
                    cmd.Parameters.AddWithValue("temp", newTemperature);
                    cmd.Parameters.AddWithValue("id", id);
                    cmd.ExecuteNonQuery();
                }
            }
        }
    }
}