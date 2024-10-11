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

$secretKey = "your_secret_key"; // Asegúrate de que esta clave sea la misma utilizada para generar el token
$encryptionKey = "your_32_character_encryption_key"; // Clave de cifrado de 32 caracteres
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';

file_put_contents($logFile, "Inicio del script actualizarResidente.php\n", FILE_APPEND);

if (!$authHeader) {
    http_response_code(401);
    file_put_contents($logFile, "Token no proporcionado.\n", FILE_APPEND);
    echo json_encode(array("status" => "error", "message" => "Token no proporcionado."));
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

        $id = $_POST['id'] ?? null;
        $dni = $_POST['dni'] ?? null;
        $nombre = $_POST['nombre'] ?? null;
        $apellidos = $_POST['apellidos'] ?? null;
        $fecha_nacimiento = $_POST['fecha_nacimiento'] ?? null;
        $ar = $_POST['ar'] ?? null;
        $nss = $_POST['nss'] ?? null;
        $numero_cuenta_bancaria = $_POST['numero_cuenta_bancaria'] ?? null;
        $observaciones = $_POST['observaciones'] ?? null;
        $empadronamiento = $_POST['empadronamiento'] ?? null;
        $telefono = $_POST['telefono'] ?? null;
        $email = $_POST['email'] ?? null;
        $tlfn_familiar_1 = $_POST['tlfn_familiar_1'] ?? null;
        $tlfn_familiar_2 = $_POST['tlfn_familiar_2'] ?? null;
        $foto = $_POST['foto'] ?? null; // Imagen codificada en base64

        file_put_contents($logFile, "Datos recibidos: " . json_encode($_POST) . "\n", FILE_APPEND);

        if (isset($id, $dni, $nombre, $apellidos, $ar, $nss, $fecha_nacimiento)) {
            $hashedDNI = encrypt($dni, $encryptionKey);
            $hashedAR = encrypt($ar, $encryptionKey);
            $hashedNSS = encrypt($nss, $encryptionKey);
            $hashedCuenta = encrypt($numero_cuenta_bancaria ?? "No hay", $encryptionKey);
            $hashedEmpadronamiento = encrypt($empadronamiento ?? "No hay", $encryptionKey);

            $telefono = $telefono ?? "No hay";
            $email = $email ?? "No hay";
            $observaciones = $observaciones ?? "No hay";
            $tlfn_familiar_1 = $tlfn_familiar_1 ?? "+34";
            $tlfn_familiar_2 = $tlfn_familiar_2 ?? "+34";

            $sql = "UPDATE residentes SET dni=?, nombre=?, apellidos=?, fecha_nacimiento=?, ar=?, nss=?, numero_cuenta_bancaria=?, observaciones=?, empadronamiento=?, telefono=?, email=?, tlfn_familiar_1=?, tlfn_familiar_2=?, foto=? WHERE id=?";
            $stmt = $conn->prepare($sql);

            if ($stmt === false) {
                file_put_contents($logFile, "Error en la preparación de la consulta: " . $conn->error . "\n", FILE_APPEND);
                echo json_encode(array("status" => "error", "message" => "Error en la preparación de la consulta: " . $conn->error));
                exit();
            }

            $stmt->bind_param('ssssssssssssssi', $hashedDNI, $nombre, $apellidos, $fecha_nacimiento, $hashedAR, $hashedNSS, $hashedCuenta, $observaciones, $hashedEmpadronamiento, $telefono, $email, $tlfn_familiar_1, $tlfn_familiar_2, $foto, $id);

            if ($stmt->execute()) {
                file_put_contents($logFile, "Datos actualizados correctamente\n", FILE_APPEND);
                echo json_encode(array("status" => "success", "message" => "Datos actualizados correctamente."));
            } else {
                file_put_contents($logFile, "Error al actualizar datos: " . $stmt->error . "\n", FILE_APPEND);
                echo json_encode(array("status" => "error", "message" => "Error al actualizar datos: " . $stmt->error));
            }

            $stmt->close();
            $conn->close();
        } else {
            file_put_contents($logFile, "Faltan datos requeridos.\n", FILE_APPEND);
            echo json_encode(array("status" => "error", "message" => "Faltan datos requeridos."));
        }
    } catch (Exception $e) {
        http_response_code(401);
        file_put_contents($logFile, "Error al decodificar JWT: " . $e->getMessage() . "\n", FILE_APPEND);
        echo json_encode(array(
            "status" => "error",
            "message" => "Acceso denegado.",
            "error" => $e->getMessage()
        ));
    }
} else {
    http_response_code(401);
    file_put_contents($logFile, "Token no proporcionado.\n", FILE_APPEND);
    echo json_encode(array("status" => "error", "message" => "Acceso denegado."));
}
?>
