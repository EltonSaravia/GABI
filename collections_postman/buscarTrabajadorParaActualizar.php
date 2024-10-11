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

error_log("Authorization Header: " . $authHeader);

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

function decryptData($data, $encryptionKey) {
    $parts = explode(':', base64_decode($data));
    if (count($parts) === 2) {
        $encryptedData = $parts[0];
        $iv = base64_decode($parts[1]);
        return openssl_decrypt($encryptedData, 'AES-256-CBC', $encryptionKey, 0, $iv);
    }
    return false;
}

if ($jwt) {
    try {
        $decoded = JWT::decode($jwt, new Key($secretKey, 'HS256'));
        $userId = $decoded->user_id;

        error_log("Decoded user ID: " . $userId);

        $dni = $_POST['dni'] ?? null;
        error_log("Received DNI: " . $dni);

        // Consulta para obtener todos los trabajadores activos excepto la contraseña
        $sql = "SELECT id, dni, nombre, apellido_1, apellido_2, puesto, telefono, email FROM trabajadores WHERE trabajador_activo = TRUE";
        $result = $conn->query($sql);

        if ($result->num_rows > 0) {
            $trabajadores = array();
            while($row = $result->fetch_assoc()) {
                $row['dni'] = decryptData($row['dni'], $encryptionKey);
                $trabajadores[] = $row;
            }

            // Si se proporciona un DNI, buscar en la lista de trabajadores
            if ($dni) {
                $trabajadorEncontrado = null;
                foreach ($trabajadores as $trabajador) {
                    if ($trabajador['dni'] === $dni) {
                        $trabajadorEncontrado = $trabajador;
                        break;
                    }
                }

                if ($trabajadorEncontrado) {
                    echo json_encode(array("status" => "success", "data" => $trabajadorEncontrado));
                } else {
                    echo json_encode(array("status" => "error", "message" => "Trabajador no encontrado."));
                }
            } else {
                echo json_encode(array("status" => "error", "message" => "DNI no proporcionado."));
            }
        } else {
            echo json_encode(array("status" => "error", "message" => "No se encontraron trabajadores."));
        }
    } catch (Exception $e) {
        http_response_code(401);
        echo json_encode(array(
            "message" => "Acceso denegado.",
            "error" => $e->getMessage()
        ));
    }
} else {
    http_response_code(401);
    echo json_encode(array("message" => "Acceso denegado."));
}
?>
