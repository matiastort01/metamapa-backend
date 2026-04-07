USE servicio_agregador;

-- -- 0. Desasociar hechos de las categorías
-- SET SQL_SAFE_UPDATES = 0;
-- UPDATE hecho SET categoria_id = NULL;
-- SET SQL_SAFE_UPDATES = 1;

-- -- 1. VACIAR TABLAS
-- SET SQL_SAFE_UPDATES = 0;
-- DELETE FROM categoria_sinonimo;
-- DELETE FROM categoria;
-- SET SQL_SAFE_UPDATES = 1;

-- 2. INSERTAR CATEGORÍAS "PADRE"
INSERT INTO categoria (id, nombre) VALUES
(1, 'Granizo / Tormenta de granizo'),
(2, 'Inundación / Desborde'),
(3, 'Sequía / Escasez de agua'),
(4, 'Frío / Helada'),
(5, 'Ola de calor / Temperaturas extremas'),
(6, 'Viento / Tormenta de viento / Tornado'),
(7, 'Actividad volcánica / Ceniza'),
(8, 'Sismo / Terremoto'),
(9, 'Incendio / Fuego forestal'),
(10, 'Derrumbe / Alud / Deslizamiento'),
(11, 'Caída de aeronave'),
(12, 'Accidente con maquinaria industrial'),
(13, 'Accidente en paso a nivel');

-- 3. INSERTAR SINÓNIMOS
INSERT INTO categoria_sinonimo (categoria_id, sinonimo) VALUES
-- 1. Granizo / Tormenta de granizo
(1, 'Precipitación de granizo'),
(1, 'Granizo de gran tamaño'),
(1, 'Fenómeno meteorológico con granizo'),
(1, 'Lluvia de hielo'),
(1, 'Tormenta de granizo'),
(1, 'Tormenta con piedras de granizo'),
(1, 'Granizada destructiva'),
(1, 'Copiosa caída de nieve'),

-- 2. Inundación / Desborde
(2, 'Inundación por lluvias intensas'),
(2, 'Anegamiento masivo'),
(2, 'Desborde de arroyo'),
(2, 'Desborde de río'),
(2, 'Inundación en zona urbana'),
(2, 'Crecida histórica'),
(2, 'Crecida de aguas subterráneas'),
(2, 'Inundación repentina'),
(2, 'Lluvia torrencial'),

-- 3. Sequía / Escasez de agua
(3, 'Crisis hídrica'),
(3, 'Emergencia por sequía'),
(3, 'Sequía prolongada'),
(3, 'Sequía extrema'),
(3, 'Sequía con pérdidas agrícolas'),
(3, 'Déficit hídrico'),
(3, 'Escasez de agua'),

-- 4. Frío / Helada
(4, 'Helada fuera de temporada'),
(4, 'Helada severa'),
(4, 'Helada destructiva'),
(4, 'Frío extremo'),
(4, 'Temperaturas bajo cero'),
(4, 'Nevada fuera de temporada'),
(4, 'Nevazón'),
(4, 'Nevada extrema'),
(4, 'Nevada histórica'),
(4, 'Precipitación de nieve'),
(4, 'Tormenta de nieve'),
(4, 'Ventisca con nieve'),
(4, 'Congelamiento'),
(4, 'Fenómeno de frío intenso'),
(4, 'Frente tormentoso'),

-- 5. Ola de calor / Temperaturas extremas
(5, 'Calor extremo'),
(5, 'Fenómeno de calor intenso'),
(5, 'Emergencia por altas temperaturas'),
(5, 'Temperaturas sofocantes'),
(5, 'Récord histórico de calor'),
(5, 'Temperaturas récord'),
(5, 'Ola de calor extremo'),

-- 6. Viento / Tormenta de viento / Tornado
(6, 'Vientos huracanados'),
(6, 'Temporal de viento'),
(6, 'Tormenta de viento'),
(6, 'Vientos con fuerza ciclónica'),
(6, 'Tormenta con fuertes vientos'),
(6, 'Embudo de viento'),
(6, 'Ráfagas de más de 100 km/h'),
(6, 'Torbellino'),
(6, 'Manga de viento'),
(6, 'Vendaval'),
(6, 'Temporal'),
(6, 'Fenómeno de viento rotativo'),
(6, 'Tormenta severa'),
(6, 'Tormenta eléctrica'),
(6, 'Tormenta tropical'),
(6, 'Tornado'),
(6, 'Ráfagas destructivas'),
(6, 'Remolino de aire'),

-- 7. Actividad volcánica / Ceniza
(7, 'Caída de ceniza'),
(7, 'Emisión volcánica'),
(7, 'Polvo volcánico en suspensión'),
(7, 'Contaminación por ceniza volcánica'),
(7, 'Lluvia de ceniza volcánica'),
(7, 'Precipitación de material volcánico'),
(7, 'Nube de ceniza'),

-- 8. Sismo / Terremoto
(8, 'Movimiento telúrico'),
(8, 'Sismo con epicentro local'),
(8, 'Sismo de gran magnitud'),
(8, 'Temblor'),
(8, 'Evento sísmico'),
(8, 'Réplica sísmica'),
(8, 'Terremoto destructivo'),

-- 9. Incendio / Fuego forestal
(9, 'Fuego en área protegida'),
(9, 'Incendio en reserva natural'),
(9, 'Incendio forestal'),
(9, 'Fuego arrasador en zona boscosa'),
(9, 'Quema de pastizales'),
(9, 'Quema descontrolada'),
(9, 'Fuego en bosque nativo'),
(9, 'Incendio en zona de monte'),

-- 10. Derrumbe / Alud / Deslizamiento
(10, 'Derrumbe en obra en construcción'),
(10, 'Derrumbe de cerro'),
(10, 'Desprendimiento de ladera'),
(10, 'Avalancha de lodo'),
(10, 'Alud'),
(10, 'Corrimiento de tierra'),
(10, 'Deslizamiento de tierra'),
(10, 'Aluvión de tierra y rocas'),

-- 11. Caída de aeronave
(11, 'Caída de aeronave'),
(11, 'Accidente aéreo'),
(11, 'Siniestro de avión'),
(11, 'Caída de avión'),

-- 12. Accidente con maquinaria industrial
(12, 'Accidente con maquinaria industrial'),
(12, 'Accidente industrial'),
(12, 'Incidente con maquinaria'),
(12, 'Lesión en planta industrial'),

-- 13. Accidente en paso a nivel
(13, 'Accidente en paso a nivel'),
(13, 'Accidente ferroviario'),
(13, 'Colisión en cruce de tren'),
(13, 'Choque en paso a nivel');
