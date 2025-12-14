USE EZBarDB;

-- 1. Insertar Puestos
INSERT IGNORE INTO PUESTOS (nombre_puesto)
VALUES
    ('Administrador'),
    ('Camarero'),
    ('Cocinero');

-- 2. Insertar Empleados
-- Password hash placeholder (e.g., '1234')
INSERT IGNORE INTO EMPLEADOS (
    nombre_empleado,
    apellido_empleado,
    nombre_usuario,
    dni,
    puesto_id,
    password_hash
)
VALUES
    (
        'Juan',
        'Perez',
        'admin',
        '12345678A',
        1,
        '$2a$10$XXXXXXXXXXXXXXXXXXXXXX'
    );

-- 3. Insertar Zonas (Requerido para Mesas)
INSERT IGNORE INTO ZONAS (nombre)
VALUES
    ('Terraza'),
    ('Interior');

-- 4. Insertar Mesas
INSERT IGNORE INTO MESAS (
    numero_mesa,
    capacidad,
    ubicacion,
    estado,
    pos_x,
    pos_y
)
VALUES
    (1, 4, 'Terraza', 'libre', 0, 0),
    (2, 4, 'Terraza', 'ocupada', 10, 0),
    (3, 2, 'Interior', 'libre', 0, 0),
    (4, 6, 'Interior', 'reservada', 10, 0);

-- 5. Insertar Categorias
INSERT IGNORE INTO CATEGORIAS (nombre, descripcion)
VALUES
    ('Bebidas', 'Refrescos, cervezas y vinos'),
    ('Tapas', 'Entrantes y raciones');

-- 6. Insertar Productos
INSERT IGNORE INTO PRODUCTOS (
    categoria_id,
    nombre,
    descripcion,
    precio,
    stock_actual
)
VALUES
    (1, 'Coca Cola', 'Refresco de cola', 2.50, 100),
    (1, 'Cerveza', 'Caña de barril', 1.80, 200),
    (
        2,
        'Patatas Bravas',
        'Ración con salsa picante',
        4.50,
        50
    ),
    (2, 'Croquetas', 'Ración de 6 unidades', 6.00, 40);