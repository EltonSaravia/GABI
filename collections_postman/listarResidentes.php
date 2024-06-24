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

$logFile = __DIR__ . '/api.log';

$secretKey = "your_secret_key"; // Clave secreta para JWT
$encryptionKey = "your_32_character_encryption_key"; // Clave de cifrado de 32 caracteres
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';

file_put_contents($logFile, "Inicio del script listarResidentesAuxiliares.php\n", FILE_APPEND);

if (!$authHeader) {
    http_response_code(401);
    file_put_contents($logFile, "Token no proporcionado.\n", FILE_APPEND);
    echo json_encode(array("message" => "Token no proporcionado."));
    exit;
}

$arr = explode(" ", $authHeader);
$jwt = $arr[1] ?? '';

file_put_contents($logFile, "JWT recibido\n", FILE_APPEND);

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

        file_put_contents($logFile, "JWT decodificado correctamente\n", FILE_APPEND);

        $sql = "SELECT id, dni, nombre, telefono, email, apellidos, fecha_nacimiento, ar, nss, numero_cuenta_bancaria, observaciones, medicamentos, fecha_ingreso, activo, empadronamiento, edad, mes_cumple, habitacion_id, estado, foto, tlfn_familiar_1, tlfn_familiar_2 FROM residentes";
        $result = $conn->query($sql);

        if ($result === false) {
            file_put_contents($logFile, "Error en la consulta SQL: " . $conn->error . "\n", FILE_APPEND);
            echo json_encode(array("status" => "error", "message" => "Error en la consulta: " . $conn->error));
            exit();
        }

        file_put_contents($logFile, "Consulta SQL ejecutada correctamente\n", FILE_APPEND);

        if ($result->num_rows > 0) {
            $residentes = array();
            while ($row = $result->fetch_assoc()) {
                file_put_contents($logFile, "Procesando fila con ID: " . $row['id'] . "\n", FILE_APPEND);

                $row['dni'] = decrypt($row['dni'], $encryptionKey);
                $row['ar'] = !is_null($row['ar']) ? decrypt($row['ar'], $encryptionKey) : null;
                $row['nss'] = !is_null($row['nss']) ? decrypt($row['nss'], $encryptionKey) : null;
                $row['numero_cuenta_bancaria'] = !is_null($row['numero_cuenta_bancaria']) ? decrypt($row['numero_cuenta_bancaria'], $encryptionKey) : null;
                $row['empadronamiento'] = !is_null($row['empadronamiento']) ? decrypt($row['empadronamiento'], $encryptionKey) : null;

                // Manejar valores nulos para los nuevos campos
                $row['tlfn_familiar_1'] = !is_null($row['tlfn_familiar_1']) ? $row['tlfn_familiar_1'] : "+34";
                $row['tlfn_familiar_2'] = !is_null($row['tlfn_familiar_2']) ? $row['tlfn_familiar_2'] : "+34";

                $residentes[] = $row;
            }
            file_put_contents($logFile, "Residentes procesados correctamente\n", FILE_APPEND);
            echo json_encode(array("status" => "success", "data" => $residentes));
        } else {
            file_put_contents($logFile, "No se encontraron residentes.\n", FILE_APPEND);
            echo json_encode(array("status" => "error", "message" => "No se encontraron residentes."));
        }

        $conn->close();
    } catch (Exception $e) {
        http_response_code(401);
        file_put_contents($logFile, "Error al decodificar JWT: " . $e->getMessage() . "\n", FILE_APPEND);
        echo json_encode(array("message" => "Acceso denegado.", "error" => $e->getMessage()));
    }
} else {
    http_response_code(401);
    file_put_contents($logFile, "Token no proporcionado.\n", FILE_APPEND);
    echo json_encode(array("message" => "Acceso denegado."));
}
?>
