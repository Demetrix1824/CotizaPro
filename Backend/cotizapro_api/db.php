<?php

// DATOS DE CONEXION
$host = "localhost";
$puerto = 3306;
$baseDatos = "cotizador_web";
$usuario = "root";
$contrasena = "";

// CONEXION PDO
try {

    $pdo = new PDO(
        "mysql:host=$host;port=$puerto;dbname=$baseDatos;charset=utf8mb4",
        $usuario,
        $contrasena,
        [
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_EMULATE_PREPARES => false
        ]
    );

} catch (PDOException $e) {

    error_log($e->getMessage());

    http_response_code(500);

    header("Content-Type: application/json; charset=UTF-8");

    echo json_encode([
        "ok" => false,
        "mensaje" => "No fue posible conectar con la base de datos."
    ]);

    exit;
}