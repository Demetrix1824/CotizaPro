<?php

header("Content-Type: application/json; charset=UTF-8");

require_once "db.php";

// ACEPTAR SOLAMENTE POST
if ($_SERVER["REQUEST_METHOD"] !== "POST") {

    http_response_code(405);

    echo json_encode([
        "ok" => false,
        "mensaje" => "Metodo no permitido."
    ]);

    exit;
}

// RECIBIR DATOS JSON
$datos = json_decode(
    file_get_contents("php://input"),
    true
);

// OBTENER USUARIO Y CONTRASEÑA
$usuario = trim($datos["usuario"] ?? "");
$contrasena = $datos["contrasena"] ?? "";

// VALIDAR CAMPOS
if ($usuario === "" || $contrasena === "") {

    http_response_code(400);

    echo json_encode([
        "ok" => false,
        "mensaje" => "Completa todos los campos."
    ]);

    exit;
}

try {

    // BUSCAR USUARIO
    $consulta = $pdo->prepare(
    "SELECT
        id,
        nombre_completo,
        usuario,
        contrasena AS password_hash,
        tipo_usuario
     FROM usuarios
     WHERE usuario = :usuario
     LIMIT 1"
);

    $consulta->execute([
        ":usuario" => $usuario
    ]);

    $resultado = $consulta->fetch();

    // VERIFICAR CONTRASEÑA
    if (
    !$resultado ||
    !password_verify(
        $contrasena,
        $resultado["password_hash"]
    )
) {
    http_response_code(401);

    echo json_encode([
        "ok" => false,
        "mensaje" => "Usuario o contraseña incorrectos."
    ]);

    exit;
}

    // GENERAR TOKEN SEGURO
$token = bin2hex(random_bytes(32));

// GENERAR HASH DEL TOKEN
$tokenHash = hash("sha256", $token);

try {

    // GUARDAR SESION EN MYSQL
    $insertarSesion = $pdo->prepare(
        "INSERT INTO sesiones
            (usuario_id, token_hash, expira_en)
         VALUES
            (:usuario_id, :token_hash,
             DATE_ADD(NOW(), INTERVAL 30 DAY))"
    );

    $insertarSesion->execute([
        ":usuario_id" => $resultado["id"],
        ":token_hash" => $tokenHash
    ]);

    // INICIO DE SESION CORRECTO
    echo json_encode([

        "ok" => true,

        "mensaje" => "Inicio de sesión exitoso.",

        "id" => $resultado["id"],

        "nombre" => $resultado["nombre_completo"],

        "usuario" => $resultado["usuario"],

        "tipo_usuario" => $resultado["tipo_usuario"],

        // Token original para Android
        "token" => $token

    ], JSON_UNESCAPED_UNICODE);

} catch (PDOException $e) {

    error_log("Error al crear sesión: " . $e->getMessage());

    http_response_code(500);

    echo json_encode([
        "ok" => false,
        "mensaje" => "No fue posible crear la sesión."
    ]);

}

} catch (PDOException $e) {

    error_log($e->getMessage());

    http_response_code(500);

    echo json_encode([
        "ok" => false,
        "mensaje" => "Error al consultar los usuarios."
    ]);

}