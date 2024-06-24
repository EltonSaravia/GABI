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

file_put_contents($logFile, "Inicio del script subirDocumento.php\n", FILE_APPEND);

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

        $dni = $_POST['dni'] ?? null;
        $titulo = $_POST['titulo'] ?? null;
        $descripcion = $_POST['descripcion'] ?? null;
        $nombreArchivo = $_POST['nombreArchivo'] ?? "documento_subido";
        $documento = $_POST['documento'] ?? null;  // Cambiado para recibir el archivo codificado en base64

        error_log("DNI: $dni");
        error_log("Título: $titulo");
        error_log("Descripción: $descripcion");
        error_log("Nombre del archivo: $nombreArchivo");
        error_log("Documento: " . substr($documento, 0, 30) . "...");

        if (isset($dni, $titulo, $descripcion, $nombreArchivo, $documento)) {
            // Obtener todos los DNIs de la base de datos
            $sql = "SELECT id, dni FROM residentes";
            $result = $conn->query($sql);

            if ($result === false) {
                file_put_contents($logFile, "Error en la consulta SQL: " . $conn->error . "\n", FILE_APPEND);
                echo json_encode(array("status" => "error", "message" => "Error en la consulta: " . $conn->error));
                exit();
            }

            file_put_contents($logFile, "Consulta SQL ejecutada correctamente\n", FILE_APPEND);

            $hashedDNI = null;

            while ($row = $result->fetch_assoc()) {
                $decryptedDNI = decrypt($row['dni'], $encryptionKey);

                if ($decryptedDNI === $dni) {
                    file_put_contents($logFile, "Residente encontrado con el DNI desencriptado: " . $decryptedDNI . "\n", FILE_APPEND);
                    $hashedDNI = $row['dni'];
                    break;
                }
            }

            if (is_null($hashedDNI)) {
                echo json_encode(array("status" => "error", "message" => "DNI no encontrado en la base de datos."));
                exit();
            }

            // Procesar el archivo
            $tipoArchivo = "application/octet-stream"; // Ajustar según el tipo de archivo real
            $contenidoArchivo = base64_decode($documento);
            $fechaSubida = date('Y-m-d H:i:s');

            // Insertar el registro en la base de datos
            $sql = "INSERT INTO documentos (dni, titulo, descripcion, nombre_archivo, tipo_archivo, contenido, fecha_subida) VALUES (?, ?, ?, ?, ?, ?, ?)";
            $stmt = $conn->prepare($sql);

            if ($stmt === false) {
                echo json_encode(array("status" => "error", "message" => "Error en la preparación de la consulta: " . $conn->error));
                exit();
            }

            $stmt->bind_param('sssssss', $hashedDNI, $titulo, $descripcion, $nombreArchivo, $tipoArchivo, $contenidoArchivo, $fechaSubida);

            if ($stmt->execute()) {
                echo json_encode(array("status" => "success", "message" => "Documento subido correctamente."));
            } else {
                echo json_encode(array("status" => "error", "message" => "Error al subir documento: " . $stmt->error));
            }

            $stmt->close();
            $conn->close();
        } else {
            echo json_encode(array("status" => "error", "message" => "Faltan datos requeridos o archivo no proporcionado."));
        }
    } catch (Exception $e) {
        http_response_code(401);
        file_put_contents($logFile, "Error al decodificar JWT: " . $e->getMessage() . "\n", FILE_APPEND);
        echo json_encode(array(
            "message" => "Acceso denegado.",
            "error" => $e->getMessage()
        ));
    }
} else {
    http_response_code(401);
    file_put_contents($logFile, "Token no proporcionado.\n", FILE_APPEND);
    echo json_encode(array("message" => "Acceso denegado."));
}
?>
