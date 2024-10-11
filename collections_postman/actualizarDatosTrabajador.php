<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

require __DIR__ . '/../vendor/autoload.php';

use \Firebase\JWT\JWT;
use \Firebase\JWT\Key;

include 'db.php';

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Headers: Authorization, Content-Type");

$secretKey = "your_secret_key"; // Asegúrate de que esta clave sea la misma utilizada para generar el token
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';

error_log("Authorization Header: " . $authHeader); // Log de la cabecera de autorización

if (!$authHeader) {
    http_response_code(401);
    echo json_encode(array("status" => "error", "message" => "Token no proporcionado."));
    exit;
}

$arr = explode(" ", $authHeader);
$jwt = $arr[1] ?? '';

if ($jwt) {
    try {
        $decoded = JWT::decode($jwt, new Key($secretKey, 'HS256'));
        error_log("Decoded JWT: " . json_encode($decoded)); // Log del JWT decodificado
        $userId = $decoded->user_id;

        // Log de todos los datos recibidos en $_POST
        error_log("POST Data: " . json_encode($_POST));

        $id = $_POST['id'] ?? null;
        $dni = $_POST['dni'] ?? null;
        $nombre = $_POST['nombre'] ?? null;
        $apellido1 = $_POST['apellido1'] ?? null;
        $apellido2 = $_POST['apellido2'] ?? null;
        $telefono = $_POST['telefono'] ?? null;
        $email = $_POST['email'] ?? null;
        $contrasena = $_POST['contrasena'] ?? null;
        $puesto = $_POST['puesto'] ?? null;

        // Agregar logs para verificar los valores recibidos
        error_log("id: " . $id);
        error_log("dni: " . $dni);
        error_log("nombre: " . $nombre);
        error_log("apellido1: " . $apellido1);
        error_log("apellido2: " . $apellido2);
        error_log("telefono: " . $telefono);
        error_log("email: " . $email);
        error_log("contrasena: " . $contrasena);
        error_log("puesto: " . $puesto);

        if (isset($id, $dni, $nombre, $apellido1, $apellido2, $telefono, $email, $contrasena, $puesto)) {
            // Hash de la contraseña
            $hashedPassword = password_hash($contrasena, PASSWORD_DEFAULT);
            // Hash del DNI
            $hashedDNI = hash('sha256', $dni);

            $sql = "UPDATE trabajadores SET dni=?, nombre=?, apellido_1=?, apellido_2=?, puesto=?, telefono=?, email=?, contrasena=? WHERE id=?";
            $stmt = $conn->prepare($sql);

            if ($stmt === false) {
                echo json_encode(array("status" => "error", "message" => "Error en la preparación de la consulta: " . $conn->error));
                exit();
            }

            $stmt->bind_param('ssssssssi', $hashedDNI, $nombre, $apellido1, $apellido2, $puesto, $telefono, $email, $hashedPassword, $id);

            if ($stmt->execute()) {
                echo json_encode(array("status" => "success", "message" => "Datos actualizados correctamente."));
            } else {
                echo json_encode(array("status" => "error", "message" => "Error al actualizar datos: " . $stmt->error));
            }

            $stmt->close();
            $conn->close();
        } else {
            echo json_encode(array("status" => "error", "message" => "Faltan datos requeridos."));
        }
    } catch (Exception $e) {
        http_response_code(401);
        error_log("Error: " . $e->getMessage()); // Log del error
        echo json_encode(array(
            "status" => "error",
            "message" => "Acceso denegado.",
            "error" => $e->getMessage()
        ));
    }
} else {
    http_response_code(401);
    echo json_encode(array("status" => "error", "message" => "Acceso denegado."));
}
?>
