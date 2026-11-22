<?php

header("Content-Type: application/json; charset=UTF-8");

require_once "db.php";

// COMPROBAR CONEXION
try {

    $consulta = $pdo->query("SELECT 1");

    echo json_encode([
        "ok" => true,
        "mensaje" => "Conexión con cotizador_web exitosa."
    ], JSON_UNESCAPED_UNICODE);

} catch (PDOException $e) {

    http_response_code(500);

    echo json_encode([
        "ok" => false,
        "mensaje" => "Error al comprobar la base de datos."
    ]);

}