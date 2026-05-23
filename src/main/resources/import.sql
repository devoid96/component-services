-- ==========================================================
-- DATOS DE PRUEBA - CPU
-- ==========================================================
INSERT INTO cpus (id, brand, model, price, socket, cores, threads, tdp) VALUES (1, 'AMD', 'Ryzen 7 7800X3D', 1899000.00, 'AM5', 8, 16, 120);
INSERT INTO cpus (id, brand, model, price, socket, cores, threads, tdp) VALUES (2, 'Intel', 'Core i7-14700K', 2299000.00, 'LGA1700', 20, 28, 125);
INSERT INTO cpus (id, brand, model, price, socket, cores, threads, tdp) VALUES (3, 'AMD', 'Ryzen 5 7600', 1099000.00, 'AM5', 6, 12, 65);

-- ==========================================================
-- DATOS DE PRUEBA - GPU
-- ==========================================================
INSERT INTO gpus (id, brand, model, price, vram, tdp, pcie_version) VALUES (4, 'NVIDIA', 'RTX 4070 Ti', 4299000.00, 12, 285, 'PCIe 4.0');
INSERT INTO gpus (id, brand, model, price, vram, tdp, pcie_version) VALUES (5, 'AMD', 'RX 7800 XT', 3199000.00, 16, 263, 'PCIe 4.0');
INSERT INTO gpus (id, brand, model, price, vram, tdp, pcie_version) VALUES (6, 'NVIDIA', 'RTX 4060', 1499000.00, 8, 115, 'PCIe 4.0');

-- ==========================================================
-- DATOS DE PRUEBA - RAM
-- ==========================================================
INSERT INTO rams (id, brand, model, price, type, capacity, speed) VALUES (7, 'Corsair', 'Vengeance', 399900.00, 'DDR5', 32, 6000);
INSERT INTO rams (id, brand, model, price, type, capacity, speed) VALUES (8, 'Kingston', 'Fury Beast', 199900.00, 'DDR4', 16, 3200);
INSERT INTO rams (id, brand, model, price, type, capacity, speed) VALUES (9, 'G.Skill', 'Trident Z5', 459900.00, 'DDR5', 32, 6400);

-- ==========================================================
-- DATOS DE PRUEBA - MOTHERBOARD
-- ==========================================================
INSERT INTO motherboards (id, brand, model, price, socket, ram_type, max_ram, pcie_version) VALUES (10, 'ASUS', 'ROG Strix B650', 1299000.00, 'AM5', 'DDR5', 128, 'PCIe 5.0');
INSERT INTO motherboards (id, brand, model, price, socket, ram_type, max_ram, pcie_version) VALUES (11, 'MSI', 'Z790 Tomahawk', 1599000.00, 'LGA1700', 'DDR5', 192, 'PCIe 5.0');
INSERT INTO motherboards (id, brand, model, price, socket, ram_type, max_ram, pcie_version) VALUES (12, 'Gigabyte', 'B760M DS3H', 849900.00, 'LGA1700', 'DDR4', 128, 'PCIe 4.0');

-- ==========================================================
-- DATOS DE PRUEBA - PSU
-- ==========================================================
INSERT INTO psus (id, brand, model, price, wattage, efficiency) VALUES (13, 'Corsair', 'RM850x', 699900.00, 850, '80+ Gold');
INSERT INTO psus (id, brand, model, price, wattage, efficiency) VALUES (14, 'EVGA', 'SuperNOVA 750', 549900.00, 750, '80+ Gold');
INSERT INTO psus (id, brand, model, price, wattage, efficiency) VALUES (15, 'Seasonic', 'Focus GX-1000', 799900.00, 1000, '80+ Gold');

-- La herencia TABLE_PER_CLASS usa una secuencia compartida para generar ids.
-- Se deja la secuencia despues del ultimo id insertado para que los POST creen ids nuevos.
SELECT setval('components_seq', 15, true);
