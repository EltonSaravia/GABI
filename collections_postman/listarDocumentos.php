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

function encrypt($string, $key) {
    $iv = openssl_random_pseudo_bytes(16);
    $encryptedData = openssl_encrypt($string, 'AES-256-CBC', $key, 0, $iv);
    return base64_encode($encryptedData . '::' . $iv);
}

function decrypt($string, $key) {
    list($encryptedData, $iv) = explode('::', base64_decode($string), 2);
    return openssl_decrypt($encryptedData, 'AES-256-CBC', $key, 0, $iv);
}

if ($jwt) {
    try {
        $decoded = JWT::decode($jwt, new Key($secretKey, 'HS256'));
        $userId = $decoded->user_id;

        $dni = $_POST['dni'] ?? null;

        if (!$dni) {
            echo json_encode(array("status" => "error", "message" => "DNI no proporcionado."));
            exit;
        }

        // Obtener todos los DNIs de la base de datos
        $sql = "SELECT id, dni FROM residentes";
        $result = $conn->query($sql);

        if ($result === false) {
            echo json_encode(array("status" => "error", "message" => "Error en la consulta: " . $conn->error));
            exit();
        }

        $hashedDNI = null;

        while ($row = $result->fetch_assoc()) {
            $decryptedDNI = decrypt($row['dni'], $encryptionKey);

            if ($decryptedDNI === $dni) {
                $hashedDNI = $row['dni'];
                break;
            }
        }

        if (is_null($hashedDNI)) {
            echo json_encode(array("status" => "error", "message" => "DNI no encontrado en la base de datos."));
            exit();
        }

        $sql = "SELECT id, titulo, descripcion, nombre_archivo, tipo_archivo, fecha_subida FROM documentos WHERE dni = ?";
        $stmt = $conn->prepare($sql);
        if ($stmt === false) {
            echo json_encode(array("status" => "error", "message" => "Error en la preparación de la consulta: " . $conn->error));
            exit();
        }

        $stmt->bind_param('s', $hashedDNI);
        $stmt->execute();
        $result = $stmt->get_result();

        $documentos = array();
        while ($row = $result->fetch_assoc()) {
            $documentos[] = $row;
        }

        if (empty($documentos)) {
            echo json_encode(array("status" => "success", "data" => []));
        } else {
            echo json_encode(array("status" => "success", "data" => $documentos));
        }

        $stmt->close();
        $conn->close();
    } catch (Exception $e) {
        http_response_code(401);
        echo json_encode(array("message" => "Acceso denegado.", "error" => $e->getMessage()));
    }
} else {
    http_response_code(401);
    echo json_encode(array("message" => "Acceso denegado."));
}
?>
