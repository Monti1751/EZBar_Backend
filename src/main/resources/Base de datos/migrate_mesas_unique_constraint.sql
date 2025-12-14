-- Migration: Allow same table numbers in different zones
-- Date: 2025-12-14
-- Description: Changes UNIQUE constraint on numero_mesa to composite constraint (numero_mesa, ubicacion)
--              This allows each zone to have independent table numbering

USE EZBarDB;

-- Drop the existing UNIQUE constraint on numero_mesa
ALTER TABLE MESAS DROP INDEX numero_mesa;

-- Add composite UNIQUE constraint on (numero_mesa, ubicacion)
ALTER TABLE MESAS
ADD UNIQUE KEY unique_mesa_por_zona (numero_mesa, ubicacion);