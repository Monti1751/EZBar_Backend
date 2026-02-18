SET FOREIGN_KEY_CHECKS = 0;

-- Corregir relación detalle_pedidos -> productos
ALTER TABLE detalle_pedidos
DROP FOREIGN KEY IF EXISTS detalle_pedidos_ibfk_2;

ALTER TABLE detalle_pedidos
ADD CONSTRAINT detalle_pedidos_ibfk_2 FOREIGN KEY (producto_id) REFERENCES productos (producto_id) ON DELETE CASCADE;

-- Corregir relación productos -> categorias
ALTER TABLE productos DROP FOREIGN KEY IF EXISTS productos_ibfk_1;

ALTER TABLE productos
ADD CONSTRAINT productos_ibfk_1 FOREIGN KEY (categoria_id) REFERENCES categorias (categoria_id) ON DELETE CASCADE;

SET FOREIGN_KEY_CHECKS = 1;

-- Verificación
SELECT
    CONSTRAINT_NAME,
    DELETE_RULE,
    TABLE_NAME
FROM information_schema.REFERENTIAL_CONSTRAINTS
WHERE
    CONSTRAINT_SCHEMA = 'ezbardb';