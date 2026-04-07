-- Tabla: categoria_sinonimo
-- Columns: categoria_id bigint, sinonimo varchar(255)

INSERT INTO servicio_agregador.categoria_sinonimo (categoria_id, sinonimo) VALUES
-- 1. Caída de aeronave
(1, 'Accidente aéreo'),
(1, 'Siniestro de avión'),
(1, 'Caída de avión'),

-- 2. Accidente con maquinaria industrial
(2, 'Accidente industrial'),
(2, 'Incidente con maquinaria'),
(2, 'Lesión en planta industrial'),

-- 3. Accidente en paso a nivel
(3, 'Accidente ferroviario'),
(3, 'Colisión en cruce de tren'),
(3, 'Choque en paso a nivel'),

-- 4. Derrumbe en obra en construcción
(4, 'Colapso en construcción'),
(4, 'Derrumbe de edificio'),
(4, 'Accidente en obra'),

-- 5. Precipitación de granizo
(5, 'Granizo'),
(5, 'Tormenta de granizo'),
(5, 'Caída de piedra de hielo'),

-- 6. Vandalismo
(6, 'Destrucción de propiedad'),
(6, 'Daños materiales'),
(6, 'Sabotaje urbano'),

-- 7. Violencia de género
(7, 'Maltrato a mujeres'),
(7, 'Violencia doméstica'),
(7, 'Abuso de género'),

-- 8. Homicidio
(8, 'Asesinato'),
(8, 'Muerte violenta'),
(8, 'Crimen'),

-- 9. Hurto
(9, 'Robo menor'),
(9, 'Sustracción'),
(9, 'Robo sin violencia'),

-- 10. Allanamiento
(10, 'Entrada forzada'),
(10, 'Intrusión'),
(10, 'Violación de propiedad'),

-- 11. Robo
(11, 'Hurto con violencia'),
(11, 'Atraco'),
(11, 'Robo con fuerza'),

-- 12. Corrupción
(12, 'Soborno'),
(12, 'Malversación'),
(12, 'Cohecho'),

-- 13. Amenaza
(13, 'Intimidación'),
(13, 'Advertencia de daño'),
(13, 'Coacción'),

-- 14. Delito informático
(14, 'Cibercrimen'),
(14, 'Hackeo'),
(14, 'Fraude digital'),

-- 15. Accidente de tránsito
(15, 'Choque vial'),
(15, 'Colisión de vehículos'),
(15, 'Accidente en carretera'),

-- 16. Venta de drogas
(16, 'Tráfico de drogas'),
(16, 'Distribución de estupefacientes'),
(16, 'Comercio de drogas'),

-- 17. Estafa
(17, 'Fraude'),
(17, 'Engaño económico'),
(17, 'Timar a alguien');
