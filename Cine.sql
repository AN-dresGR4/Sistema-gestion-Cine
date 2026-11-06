CREATE DATABASE IF NOT EXISTS cine_db;
USE cine_db;

CREATE TABLE peliculas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    duracion INT NOT NULL,
    clasificacion VARCHAR(10) NOT NULL
);

CREATE TABLE salas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    capacidad INT NOT NULL
);

CREATE TABLE funciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pelicula INT NOT NULL,
    id_sala INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_pelicula) REFERENCES peliculas(id) ON DELETE CASCADE,
    FOREIGN KEY (id_sala) REFERENCES salas(id) ON DELETE CASCADE
);

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    documento VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL
);

CREATE TABLE reservas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_funcion INT NOT NULL,
    id_cliente INT NOT NULL,
    asiento VARCHAR(10) NOT NULL,
    fecha_reserva DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'Confirmada',
    FOREIGN KEY (id_funcion) REFERENCES funciones(id) ON DELETE CASCADE,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id) ON DELETE CASCADE,
    UNIQUE(id_funcion, asiento)
);

INSERT INTO peliculas (titulo, genero, duracion, clasificacion) VALUES 
('Inception', 'Ciencia Ficción', 148, '+12'),
('El Padrino', 'Drama', 175, '+18');

INSERT INTO salas (nombre, capacidad) VALUES 
('Sala 1 - IMAX', 100),
('Sala 2 - 3D', 80);

INSERT INTO funciones (id_pelicula, id_sala, fecha_hora, precio) VALUES 
(1, 1, '2026-11-10 18:00:00', 15000.00),
(2, 2, '2026-11-10 21:00:00', 12000.00);

INSERT INTO clientes (documento, nombre, correo) VALUES 
('10101010', 'Andres Gonzalez', 'andres@example.com');
