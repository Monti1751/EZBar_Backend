/* =========================
1. Crear base de datos
========================= */
CREATE DATABASE IF NOT EXISTS EZBarDB;

USE EZBarDB;

/* =========================
2. PUESTOS
========================= */
CREATE TABLE IF NOT EXISTS PUESTOS (
    puesto_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_puesto VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO
    PUESTOS (nombre_puesto)
VALUES ('Admin'),
    ('Mesero'),
    ('Cocinero'),
    ('Caja');

/* =========================
3. EMPLEADOS
========================= */
CREATE TABLE IF NOT EXISTS EMPLEADOS (
    empleado_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_empleado VARCHAR(50) NOT NULL,
    apellido_empleado VARCHAR(50) NOT NULL,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    dni VARCHAR(20) NOT NULL UNIQUE,
    puesto_id INT NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (puesto_id) REFERENCES PUESTOS (puesto_id)
);

INSERT INTO
    EMPLEADOS (
        nombre_empleado,
        apellido_empleado,
        nombre_usuario,
        dni,
        puesto_id,
        password_hash
    )
VALUES (
        'Juan',
        'García',
        'juan_mesero',
        '12345678A',
        2,
        '$2a$10$abc123'
    ),
    (
        'María',
        'López',
        'maria_cocinera',
        '87654321B',
        3,
        '$2a$10$xyz789'
    ),
    (
        'Pedro',
        'Martínez',
        'pedro_caja',
        '11111111C',
        4,
        '$2a$10$def456'
    );

/* =========================
4. ZONAS
========================= */
CREATE TABLE IF NOT EXISTS ZONAS (
    zona_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO
    ZONAS (nombre)
VALUES ('Terraza'),
    ('Interior'),
    ('Barra');

/* =========================
5. MESAS
========================= */
CREATE TABLE IF NOT EXISTS MESAS (
    mesa_id INT AUTO_INCREMENT PRIMARY KEY,
    numero_mesa INT NOT NULL,
    capacidad INT NOT NULL,
    ubicacion VARCHAR(50) NOT NULL,
    estado ENUM(
        'libre',
        'ocupada',
        'reservada'
    ) DEFAULT 'libre',
    pos_x DOUBLE DEFAULT 0,
    pos_y DOUBLE DEFAULT 0,
    UNIQUE (numero_mesa, ubicacion),
    FOREIGN KEY (ubicacion) REFERENCES ZONAS (nombre) ON UPDATE CASCADE ON DELETE CASCADE
);

/* =========================
6. CATEGORIAS
========================= */
CREATE TABLE IF NOT EXISTS CATEGORIAS (
    categoria_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT
);

/* =========================
7. PRODUCTOS (CON IMAGEN)
========================= */
CREATE TABLE IF NOT EXISTS PRODUCTOS (
    producto_id INT AUTO_INCREMENT PRIMARY KEY,
    categoria_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    costo DECIMAL(10, 2),
    iva_porcentaje DECIMAL(5, 2) DEFAULT 0,
    stock_actual INT DEFAULT 0,
    stock_minimo INT DEFAULT 0,
    unidad_medida VARCHAR(20),
    es_ingrediente BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    url_imagen VARCHAR(255),
    imagen LONGBLOB,
    FOREIGN KEY (categoria_id) REFERENCES CATEGORIAS (categoria_id)
);

/* =========================
8. PRODUCTO_INGREDIENTES
========================= */
CREATE TABLE IF NOT EXISTS PRODUCTO_INGREDIENTES (
    producto_id INT NOT NULL,
    ingrediente_id INT NOT NULL,
    cantidad DECIMAL(10, 2) NOT NULL,
    unidad_medida VARCHAR(20),
    PRIMARY KEY (producto_id, ingrediente_id),
    FOREIGN KEY (producto_id) REFERENCES PRODUCTOS (producto_id),
    FOREIGN KEY (ingrediente_id) REFERENCES PRODUCTOS (producto_id)
);

/* =========================
9. PEDIDOS
========================= */
CREATE TABLE IF NOT EXISTS PEDIDOS (
    pedido_id INT AUTO_INCREMENT PRIMARY KEY,
    mesa_id INT NOT NULL,
    empleado_id INT NOT NULL,
    estado ENUM(
        'pendiente',
        'en_preparacion',
        'listo',
        'entregado',
        'cancelado',
        'pagado'
    ) DEFAULT 'pendiente',
    numero_comensales INT DEFAULT 1,
    observaciones TEXT,
    total_pedido DECIMAL(10, 2) DEFAULT 0,
    fecha_hora_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mesa_id) REFERENCES MESAS (mesa_id),
    FOREIGN KEY (empleado_id) REFERENCES EMPLEADOS (empleado_id)
);

/* =========================
10. DETALLE_PEDIDOS
========================= */
CREATE TABLE IF NOT EXISTS DETALLE_PEDIDOS (
    detalle_id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad DECIMAL(10, 2) DEFAULT 1,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    total_linea DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES PEDIDOS (pedido_id),
    FOREIGN KEY (producto_id) REFERENCES PRODUCTOS (producto_id)
);

/* =========================
11. PAGOS
========================= */
CREATE TABLE IF NOT EXISTS PAGOS (
    pago_id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    empleado_id INT NOT NULL,
    metodo_pago ENUM(
        'efectivo',
        'tarjeta',
        'transferencia'
    ) NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    propina DECIMAL(10, 2) DEFAULT 0,
    fecha_hora_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pedido_id) REFERENCES PEDIDOS (pedido_id),
    FOREIGN KEY (empleado_id) REFERENCES EMPLEADOS (empleado_id)
);

/* =========================
12. INVENTARIO
========================= */
CREATE TABLE IF NOT EXISTS INVENTARIO (
    inventario_id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT NOT NULL,
    empleado_id INT NOT NULL,
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cantidad DECIMAL(10, 2) NOT NULL,
    tipo_movimiento ENUM('entrada', 'salida') NOT NULL,
    FOREIGN KEY (producto_id) REFERENCES PRODUCTOS (producto_id),
    FOREIGN KEY (empleado_id) REFERENCES EMPLEADOS (empleado_id)
);

/* =========================
13. USUARIOS
========================= */
CREATE TABLE IF NOT EXISTS USUARIOS (
    usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    empleado_id INT NOT NULL,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (empleado_id) REFERENCES EMPLEADOS (empleado_id)
);