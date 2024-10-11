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

$secretKey = "your_secret_key"; // Clave secreta para JWT
$encryptionKey = "your_32_character_encryption_key"; // Clave de cifrado de 32 caracteres
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';

if (!$authHeader) {
    http_response_code(401);
    echo json_encode(array("message" => "Token no proporcionado."));
    exit;
}

$arr = explode(" ", $authHeader);
$jwt = $arr[1] ?? '';

function encryptData($data, $encryptionKey) {
    $iv = openssl_random_pseudo_bytes(16);
    $encryptedData = openssl_encrypt($data, 'AES-256-CBC', $encryptionKey, 0, $iv);
    return base64_encode($encryptedData . ':' . base64_encode($iv));
}

if ($jwt) {
    try {
        $decoded = JWT::decode($jwt, new Key($secretKey, 'HS256'));
        $userId = $decoded->user_id;

        $dni = $_POST['dni'] ?? null;
        $nombre = $_POST['nombre'] ?? null;
        $apellido1 = $_POST['apellido1'] ?? null;
        $apellido2 = $_POST['apellido2'] ?? null;
        $telefono = $_POST['telefono'] ?? null;
        $email = $_POST['email'] ?? null;
        $contrasena = $_POST['contrasena'] ?? null;
        $puesto = $_POST['puesto'] ?? null;

        if (isset($dni, $nombre, $apellido1, $apellido2, $telefono, $email, $contrasena, $puesto)) {
            $hashedPassword = password_hash($contrasena, PASSWORD_DEFAULT);
            $encryptedDNI = encryptData($dni, $encryptionKey);

            $sql = "INSERT INTO trabajadores (dni, nombre, apellido_1, apellido_2, puesto, telefono, email, contrasena) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            $stmt = $conn->prepare($sql);

            if ($stmt === false) {
                echo json_encode(array("status" => "error", "message" => "Error en la preparación de la consulta: " . $conn->error));
                exit();
            }

            $stmt->bind_param('ssssssss', $encryptedDNI, $nombre, $apellido1, $apellido2, $puesto, $telefono, $email, $hashedPassword);

            if ($stmt->execute()) {
                echo json_encode(array("status" => "success", "message" => "Datos insertados correctamente."));
            } else {
                echo json_encode(array("status" => "error", "message" => "Error al insertar datos: " . $stmt->error));
            }

            $stmt->close();
            $conn->close();
        } else {
            echo json_encode(array("status" => "error", "message" => "Faltan datos requeridos."));
        }
    } catch (Exception $e) {
        http_response_code(401);
        echo json_encode(array("message" => "Acceso denegado.", "error" => $e->getMessage()));
    }
} else {
    http_response_code(401);
    echo json_encode(array("message" => "Acceso denegado."));
}
?>
