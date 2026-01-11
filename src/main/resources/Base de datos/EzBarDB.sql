-- Crear base de datos
CREATE DATABASE IF NOT EXISTS EZBarDB;

USE EZBarDB;

-- ======================================
-- 1. Tabla PUESTOS (lookup)
-- ======================================
CREATE TABLE PUESTOS (
    puesto_id INT PRIMARY KEY AUTO_INCREMENT,
    nombre_puesto VARCHAR(50) UNIQUE NOT NULL
);

-- ======================================
-- 2. Tabla EMPLEADOS
-- ======================================
CREATE TABLE EMPLEADOS (
    empleado_id INT PRIMARY KEY AUTO_INCREMENT,
    nombre_empleado VARCHAR(50) NOT NULL,
    apellido_empleado VARCHAR(50) NOT NULL,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    dni VARCHAR(20) UNIQUE NOT NULL,
    puesto_id INT NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (puesto_id) REFERENCES PUESTOS (puesto_id)
);

-- ======================================
-- 3. Tabla MESAS
-- ======================================
CREATE TABLE MESAS (
    mesa_id INT PRIMARY KEY AUTO_INCREMENT,
    numero_mesa INT NOT NULL,
    capacidad INT NOT NULL,
    ubicacion VARCHAR(50) NOT NULL,
    estado ENUM('libre', 'ocupada', 'reservada') DEFAULT 'libre',
    pos_x DOUBLE DEFAULT 0,
    pos_y DOUBLE DEFAULT 0,
    UNIQUE KEY unique_mesa_por_zona (numero_mesa, ubicacion),
    FOREIGN KEY (ubicacion) REFERENCES ZONAS (nombre) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ======================================
-- 4. Tabla CATEGORIAS
-- ======================================
CREATE TABLE CATEGORIAS (
    categoria_id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT
);

-- ======================================
-- 5. Tabla PRODUCTOS
-- ======================================
CREATE TABLE PRODUCTOS (
    producto_id INT PRIMARY KEY AUTO_INCREMENT,
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
    FOREIGN KEY (categoria_id) REFERENCES CATEGORIAS (categoria_id)
);

-- ======================================
-- 6. Tabla PRODUCTO_INGREDIENTES (relación muchos a muchos)
-- ======================================
CREATE TABLE PRODUCTO_INGREDIENTES (
    producto_id INT NOT NULL,
    ingrediente_id INT NOT NULL,
    cantidad DECIMAL(10, 2) NOT NULL,
    unidad_medida VARCHAR(20),
    PRIMARY KEY (producto_id, ingrediente_id),
    FOREIGN KEY (producto_id) REFERENCES PRODUCTOS (producto_id),
    FOREIGN KEY (ingrediente_id) REFERENCES PRODUCTOS (producto_id)
);

-- ======================================
-- 7. Tabla PEDIDOS
-- ======================================
CREATE TABLE PEDIDOS (
    pedido_id INT PRIMARY KEY AUTO_INCREMENT,
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

-- ======================================
-- 8. Tabla DETALLE_PEDIDOS
-- ======================================
CREATE TABLE DETALLE_PEDIDOS (
    detalle_id INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad DECIMAL(10, 2) NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    total_linea DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES PEDIDOS (pedido_id),
    FOREIGN KEY (producto_id) REFERENCES PRODUCTOS (producto_id)
);

-- ======================================
-- 9. Tabla PAGOS
-- ======================================
CREATE TABLE PAGOS (
    pago_id INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT NOT NULL,
    empleado_id INT NOT NULL,
    metodo_pago ENUM('efectivo', 'tarjeta', 'transferencia') NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    propina DECIMAL(10, 2) DEFAULT 0,
    fecha_hora_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pedido_id) REFERENCES PEDIDOS (pedido_id),
    FOREIGN KEY (empleado_id) REFERENCES EMPLEADOS (empleado_id)
);

-- ======================================
-- 10. Tabla INVENTARIO
-- ======================================
CREATE TABLE INVENTARIO (
    inventario_id INT PRIMARY KEY AUTO_INCREMENT,
    producto_id INT NOT NULL,
    empleado_id INT NOT NULL,
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cantidad DECIMAL(10, 2) NOT NULL,
    tipo_movimiento ENUM('entrada', 'salida') NOT NULL,
    FOREIGN KEY (producto_id) REFERENCES PRODUCTOS (producto_id),
    FOREIGN KEY (empleado_id) REFERENCES EMPLEADOS (empleado_id)
);

-- ======================================
-- 11. Tabla ZONAS (Nueva)
-- ======================================
CREATE TABLE ZONAS (
    zona_id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

-- ======================================
-- 12. Tabla USUARIOS (Nueva)
-- ======================================
CREATE TABLE usuarios (
    usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    empleado_id INT NOT NULL,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activo TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_empleado FOREIGN KEY (empleado_id) REFERENCES empleados (empleado_id) ON UPDATE CASCADE ON DELETE RESTRICT
);