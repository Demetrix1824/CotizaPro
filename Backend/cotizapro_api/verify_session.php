<?php

header("Content-Type: application/json; charset=UTF-8");

require_once "db.php";

// OBTENER TOKEN DEL ENCABEZADO
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

// OBTENER TOKEN ORIGINAL
$token = $matches[1];

// CALCULAR HASH DEL TOKEN
$tokenHash = hash("sha256", $token);

try {

    // BUSCAR SESION ACTIVA
    $consultaSesion = $pdo->prepare(
        "SELECT usuario_id
         FROM sesiones
         WHERE token_hash = :token_hash
         AND revocado_en IS NULL
         AND expira_en > NOW()
         LIMIT 1"
    );

    $consultaSesion->execute([
        ":token_hash" => $tokenHash
    ]);

    $sesion = $consultaSesion->fetch();

    if (!$sesion) {

        http_response_code(401);

        echo json_encode([
            "ok" => false,
            "mensaje" => "La sesión ha expirado o ya no es válida."
        ]);

        exit;
    }

    // BUSCAR INFORMACION DEL USUARIO
    $consultaUsuario = $pdo->prepare(
        "SELECT id, nombre_completo, usuario, tipo_usuario
         FROM usuarios
         WHERE id = :id
         LIMIT 1"
    );

    $consultaUsuario->execute([
        ":id" => $sesion["usuario_id"]
    ]);

    $usuario = $consultaUsuario->fetch();

    if (!$usuario) {

        http_response_code(401);

        echo json_encode([
            "ok" => false,
            "mensaje" => "El usuario ya no existe."
        ]);

        exit;
    }

    // SESION VALIDA
    echo json_encode([
        "ok" => true,
        "mensaje" => "Sesión activa.",
        "id" => $usuario["id"],
        "nombre" => $usuario["nombre_completo"],
        "usuario" => $usuario["usuario"],
        "tipo_usuario" => $usuario["tipo_usuario"]
    ], JSON_UNESCAPED_UNICODE);

} catch (PDOException $e) {

    error_log("Error al validar sesión: " . $e->getMessage());

    http_response_code(500);

    echo json_encode([
        "ok" => false,
        "mensaje" => "No fue posible validar la sesión."
    ]);

}