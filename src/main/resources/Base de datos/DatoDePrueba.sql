-- Datos de prueba para PUESTOS
INSERT INTO
    PUESTOS (nombre_puesto)
VALUES ('Ausuariosdmin'),
    ('Mesero'),
    ('Cocinero'),
    ('Caja');
-- Datos de prueba para EMPLEADOS
INSERT INTO
    EMPLEADOS (
        nombre_empleado,
        apellido_empleado,
        nombre_usuario,
        dni,
        puesto_id,
        password_hash,
        activo
    )
VALUES (
        'Juan',
        'García',
        'juan_mesero',
        '12345678A',
        2,
        '$2a$10$abc123...',
        TRUE
    ),
    (
        'María',
        'López',
        'maria_cocinera',
        '87654321B',
        3,
        '$2a$10$xyz789...',
        TRUE
    ),
    (
        'Pedro',
        'Martínez',
        'pedro_caja',
        '11111111C',
        4,
        '$2a$10$def456...',
        TRUE
    );
-- Datos de prueba para ZONAS
INSERT INTO
    ZONAS (nombre)
VALUES ('Terraza'),
    ('Interior'),
    ('Barra');
-- Datos de prueba para MESAS - TERRAZA
INSERT INTO
    MESAS (
        numero_mesa,
        capacidad,
        ubicacion,
        estado,
        pos_x,
        pos_y
    )
VALUES (
        1,
        4,
        'Terraza',
        'libre',
        10.0,
        10.0
    ),
    (
        2,
        4,
        'Terraza',
        'libre',
        20.0,
        10.0
    ),
    (
        3,
        6,
        'Terraza',
        'libre',
        30.0,
        10.0
    ),
    (
        4,
        2,
        'Terraza',
        'libre',
        10.0,
        20.0
    ),
    (
        5,
        4,
        'Terraza',
        'libre',
        20.0,
        20.0
    );
-- Datos de prueba para MESAS -
INTERIOR
INSERT INTO
    MESAS (
        numero_mesa,
        capacidad,
        ubicacion,
        estado,
        pos_x,
        pos_y
    )
VALUES (
        1,
        4,
        'Interior',
        'libre',
        10.0,
        10.0
    ),
    (
        2,
        6,
        'Interior',
        'libre',
        20.0,
        10.0
    ),
    (
        3,
        2,
        'Interior',
        'libre',
        30.0,
        10.0
    ),
    (
        4,
        4,
        'Interior',
        'libre',
        10.0,
        20.0
    );
-- Datos de prueba para MESAS - BARRA
INSERT INTO
    MESAS (
        numero_mesa,
        capacidad,
        ubicacion,
        estado,
        pos_x,
        pos_y
    )
VALUES (
        1,
        1,
        'Barra',
        'libre',
        5.0,
        5.0
    ),
    (
        2,
        1,
        'Barra',
        'libre',
        10.0,
        5.0
    ),
    (
        3,
        1,
        'Barra',
        'libre',
        15.0,
        5.0
    ),
    (
        4,
        2,
        'Barra',
        'libre',
        20.0,
        5.0
    );
-- Datos de prueba para CATEGORIAS
INSERT INTO
    CATEGORIAS (nombre, descripcion)
VALUES (
        'Entrantes',
        'Platos para empezar'
    ),
    (
        'Platos Principales',
        'Platos principales del menú'
    ),
    ('Postres', 'Postres variados'),
    (
        'Bebidas',
        'Bebidas frías y calientes'
    ),
    (
        'Alcohólicas',
        'Bebidas alcohólicas'
    ),
    (
        'Ingredientes',
        'Ingredientes para cocina'
    );