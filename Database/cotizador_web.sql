-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-11-2026 a las 03:37:02
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `cotizador_web`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `correo` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `nombre`, `correo`, `telefono`) VALUES
(1, 'edgar', 'edgar.garcia@gmail.com', '9381278492'),
(2, 'bety', 'beatriz@gmail.com', '9381424495'),
(4, 'jannia shiray', 'ejemplo@gmail.com', '9381130148'),
(5, 'maria castro', 'maria@gmail.com', '93812300741'),
(6, 'yerick', 'yerick@gmail.com', '123'),
(7, 'Yazmin', 'Yazmin@gmail.com', '938456145');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cotizaciones`
--

CREATE TABLE `cotizaciones` (
  `id` int(11) NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `fecha` datetime DEFAULT current_timestamp(),
  `total` decimal(10,2) NOT NULL,
  `descripcion` text NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `medidas` varchar(100) DEFAULT NULL,
  `porcentaje` decimal(5,2) DEFAULT NULL,
  `precio_venta` decimal(10,2) DEFAULT NULL,
  `cantidad` int(11) DEFAULT NULL,
  `usuario_id` int(11) DEFAULT NULL,
  `m2` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `cotizaciones`
--

INSERT INTO `cotizaciones` (`id`, `cliente_id`, `fecha`, `total`, `descripcion`, `monto`, `medidas`, `porcentaje`, `precio_venta`, `cantidad`, `usuario_id`, `m2`) VALUES
(5, 1, '2025-06-13 09:39:29', 0.00, 'ventana', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(6, 1, '2025-06-13 09:42:44', 0.00, 'ventana', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(7, 1, '2025-06-13 09:44:58', 0.00, 'ventana', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(8, 1, '2025-06-13 09:56:41', 0.00, 'ventana', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(9, 1, '2025-06-13 10:11:34', 0.00, 'ventana', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(10, 1, '2025-06-13 10:19:23', 0.00, 'ventana', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(11, 1, '2025-06-13 10:20:03', 0.00, 'puerta', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(12, 1, '2025-06-13 10:31:51', 0.00, 'ventana', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(13, 1, '2025-06-13 10:52:50', 0.00, 'ventana', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(14, 1, '2025-06-13 11:26:02', 0.00, 'espejo', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(15, 1, '2025-06-13 12:01:04', 0.00, 'espejo', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(16, 1, '2025-06-13 13:05:22', 0.00, 'pelicula', 320.00, '1.60 x1.90', 60.00, 200.00, 1, NULL, NULL),
(17, 1, '2025-06-13 16:26:23', 0.00, 'barandal', 320.00, '1.60 x1.90', 60.00, 200.00, 1, 7, NULL),
(23, 1, '2025-06-24 22:38:27', 0.00, 'puerta doble corrediza', 9600.00, NULL, NULL, NULL, NULL, 6, 2.88),
(30, 1, '2025-06-24 16:19:56', 0.00, 'cancel de baÃ±o', 19800.00, NULL, NULL, NULL, NULL, 6, 14),
(34, 1, '2025-07-02 10:22:53', 0.00, 'SUMINISTRO E INSTALACION DE VENTANA DE PROYECCION CON ALUMINIO DE 2\" EN COLOR NEGRO CON MEDIDAS DE 38.5 DE ANCHO POR 1.285 DE ALTO, HERRAJE DE ACERO INOXIDABLE, TODO PARA SU PERFECTA INSTALACIÃ“N ', 2453.78, NULL, NULL, NULL, NULL, 8, 49),
(36, 4, '2026-08-11 13:12:29', 0.00, 'aluminipo', 1113.60, NULL, NULL, NULL, NULL, 11, 20),
(37, 5, '2026-08-12 11:58:45', 0.00, 'Tablaroca', 2784.00, NULL, NULL, NULL, NULL, 13, 50),
(38, 6, '2026-08-12 12:10:02', 0.00, 'barra de acero', 1500.00, NULL, NULL, NULL, NULL, 13, 20),
(39, 7, '2026-08-12 12:42:31', 0.00, 'tabla roca ', 1113.60, NULL, NULL, NULL, NULL, 10, 68),
(40, 5, '2026-08-12 12:50:48', 0.00, 'alumino', 4751.36, NULL, NULL, NULL, NULL, 10, 70),
(41, 1, '2026-08-12 12:57:21', 0.00, 'Tablaroca', 928.00, NULL, NULL, NULL, NULL, 10, 469),
(42, 5, '2026-08-17 12:36:30', 0.00, 'aluminio con un cerco A\r\n', 2227.20, NULL, NULL, NULL, NULL, 11, 6),
(43, 2, '2026-08-17 12:57:38', 0.00, 'puerta', 8658.24, NULL, NULL, NULL, NULL, 11, 45),
(44, 6, '2026-08-19 10:46:20', 0.00, '1 puerta de madera', 928.00, NULL, NULL, NULL, NULL, 11, 1),
(45, 7, '2026-08-19 10:48:59', 0.00, 'una ventaana', 2383.10, NULL, NULL, NULL, NULL, 11, 80),
(46, 2, '2026-08-19 10:52:15', 0.00, 'escritorio gamers', 9280.00, NULL, NULL, NULL, NULL, 11, 4),
(48, 2, '2026-08-19 11:07:32', 0.00, 'tubo de PVC', 1856.00, NULL, NULL, NULL, NULL, 11, 25),
(50, 4, '2026-08-19 12:16:23', 0.00, 'PUERTA ', 1419.84, NULL, NULL, NULL, NULL, 11, 89),
(51, 1, '2026-08-19 13:05:31', 0.00, 'Ventana', 848.19, NULL, NULL, NULL, NULL, 11, 45),
(52, 1, '2026-08-19 13:22:34', 0.00, 'una ventana ', 1772.48, NULL, NULL, NULL, NULL, 11, 45);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cotizacion_detalles`
--

CREATE TABLE `cotizacion_detalles` (
  `id` int(11) NOT NULL,
  `cotizacion_id` int(11) NOT NULL,
  `producto_id` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `precio` decimal(10,2) NOT NULL,
  `medidas` varchar(100) DEFAULT NULL,
  `porcentaje` decimal(5,2) DEFAULT NULL,
  `precio_venta` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `nombre`, `descripcion`, `precio`, `medidas`, `porcentaje`, `precio_venta`) VALUES
(4, 'IMAN', 'IMAN DE USO INDUSTRIAL', 50.00, '1 CMM X 1 CMM', 80.00, 90.00),
(6, 'tabla de madera', 'tabla de madera de roble', 600.00, '1.80m*80m', 15.00, 690.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sesiones`
--

CREATE TABLE `sesiones` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `token_hash` char(64) NOT NULL,
  `creado_en` datetime NOT NULL DEFAULT current_timestamp(),
  `expira_en` datetime NOT NULL,
  `revocado_en` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `sesiones`
--

INSERT INTO `sesiones` (`id`, `usuario_id`, `token_hash`, `creado_en`, `expira_en`, `revocado_en`) VALUES
(3, 11, 'ee05cee2e4281af03f598114afc82be69ef1c12cd6beaf70fc74a911864bc593', '2026-11-20 23:52:25', '2026-12-20 23:52:25', '2026-11-20 23:52:27'),
(4, 11, '8d8385aeb30df271a0a5a5fa41d000ded17fcf44cba1c88a2b218ea41fc7794e', '2026-11-20 23:52:38', '2026-12-20 23:52:38', '2026-11-20 23:52:40'),
(5, 11, '38a9a805e79c746777d0cadf5a134d3027423259854e326bce10a2aaa9270ab6', '2026-11-21 00:15:30', '2026-12-21 00:15:30', '2026-11-21 00:15:48'),
(6, 11, '765c369b05d6f3e0191f2d840bf0a1ccecc2faf3ce6b9531dd216c0a8b4c3793', '2026-11-21 00:16:31', '2026-12-21 00:16:31', '2026-11-21 00:16:37'),
(7, 11, 'efb70f4d56f64072911106b72bff51c9f9b5a520b09a9f6a31fd99fe4e5ef5ae', '2026-11-21 19:54:12', '2026-12-21 19:54:12', '2026-11-21 19:54:14'),
(8, 11, 'b72f6f0271aca6ea4af73c008e7dc4d9630d21d5d0637092e64a96825c730fea', '2026-11-21 19:56:00', '2026-12-21 19:56:00', '2026-11-21 19:56:33');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `nombre_completo` varchar(100) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `tipo_usuario` enum('admin','empleado') NOT NULL DEFAULT 'empleado'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `nombre_completo`, `usuario`, `contrasena`, `tipo_usuario`) VALUES
(6, 'Iris del Carmen Garcia Rodriguez', 'iris', '$2y$10$qEORSoJ.usWTDbkzIhviBuJlcOX/s4oeSC4u22fO8sjSQZdv8wDqy', ''),
(7, 'Consuelo Cruz may', 'consuelo', '$2y$10$KQcActAMVZEP6cmsMhzEOeYFH1JA7tvddFbHl2hqCdm39rhIAzgD6', ''),
(8, 'yuri potenciano', 'yuri', '$2y$10$to43fQKmXC8NbgX.MVklnuXYxvFXu5UuQQzyDJToDyws8FLMCJ68K', ''),
(9, 'mariloli hernandez', 'mariloli', '$2y$10$rFuYxebBX2LfsQwKU8vcf.fKaC34Jrp1boSjFZUci9pXQv.a0IDx.', ''),
(10, 'Emiliano', 'Emi', '$2y$10$hDH/L4eR.tV0iYSmMvWy0eCG1NWtUDkq02IAndzCJBIQ.a66G21S2', 'admin'),
(11, 'jannia shiray', 'jannia', '$2y$10$El7T2vusmyAgfaJvi.43MecL6rw/lv1WjMmVC5MW/Bc9M75/hqibW', ''),
(12, 'pancho francisco chucho pancracio', 'pepe', '$2y$10$/arE4tfZkV5tD7Imj1Zr8eEnr88RXiI3RO8V8YU9JUxGUSXKKW0im', 'admin'),
(13, 'maria', 'maria', '$2y$10$eE/1vBQNVVehmZEsJN/P6.5239zouz7WVOf6tJBlSyQiRrhnkIafS', ''),
(14, 'Jorge', 'Demem', '$2y$10$Yv/COoE0JYhBMMty.YJ3MOAnjHMAa3s8URAOc5hAX1m9bJMS9ZgOm', 'admin'),
(15, 'Murillo', 'Murillo', '$2y$10$AJ/GpExDVYfcvFFWyXJJwObckHiheYZ5Md9lC0lCyEjVo61Eak/K6', 'admin');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `cotizaciones`
--
ALTER TABLE `cotizaciones`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cliente_id` (`cliente_id`),
  ADD KEY `usuario_id` (`usuario_id`);

--
-- Indices de la tabla `cotizacion_detalles`
--
ALTER TABLE `cotizacion_detalles`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cotizacion_id` (`cotizacion_id`),
  ADD KEY `producto_id` (`producto_id`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `sesiones`
--
ALTER TABLE `sesiones`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_token_hash` (`token_hash`),
  ADD KEY `idx_usuario_id` (`usuario_id`),
  ADD KEY `idx_expira_en` (`expira_en`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `usuario` (`usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `cotizaciones`
--
ALTER TABLE `cotizaciones`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;

--
-- AUTO_INCREMENT de la tabla `cotizacion_detalles`
--
ALTER TABLE `cotizacion_detalles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `sesiones`
--
ALTER TABLE `sesiones`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cotizaciones`
--
ALTER TABLE `cotizaciones`
  ADD CONSTRAINT `cotizaciones_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `cotizaciones_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `cotizacion_detalles`
--
ALTER TABLE `cotizacion_detalles`
  ADD CONSTRAINT `cotizacion_detalles_ibfk_1` FOREIGN KEY (`cotizacion_id`) REFERENCES `cotizaciones` (`id`),
  ADD CONSTRAINT `cotizacion_detalles_ibfk_2` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
