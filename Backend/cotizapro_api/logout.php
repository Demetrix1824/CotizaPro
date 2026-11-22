<?php

header("Content-Type: application/json; charset=UTF-8");

require_once "db.php";

// OBTENER ENCABEZADO DE AUTORIZACION
$headers = function_exists("getallheaders")
    ? getallheaders()
    : [];

$authorization =
    $_SERVER["HTTP_AUTHORIZATION"]
    ?? $_SERVER["REDIRECT_HTTP_AUTHORIZATION"]
    ?? $headers["Authorization"]
    ?? $headers["authorization"]
    ?? "";

if (!preg_match("/Bearer\s+([a-f0-9]{64})/i", $authorization, $matches)) {

    http_response_code(401);

    echo json_encode([
        "ok" => false,
        "mensaje" => "Token no proporcionado."
    ]);

    exit;
}

// OBTENER Y PROTEGER TOKEN
$token = $matches[1];

$tokenHash = hash("sha256", $token);

try {

    // REVOCAR SESION
    $consulta = $pdo->prepare(
        "UPDATE sesiones
         SET revocado_en = NOW()
         WHERE token_hash = :token_hash
         AND revocado_en IS NULL"
    );

    $consulta->execute([
        ":token_hash" => $tokenHash
    ]);

    echo json_encode([
        "ok" => true,
        "mensaje" => "Sesión cerrada correctamente."
    ]);

} catch (PDOException $e) {

    error_log("Error al cerrar sesión: " . $e->getMessage());

    http_response_code(500);

    echo json_encode([
        "ok" => false,
        "mensaje" => "No fue posible cerrar la sesión."
    ]);

}