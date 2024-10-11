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

$secretKey = "your_secret_key";
$encryptionKey = "your_32_character_encryption_key";
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';

if (!$authHeader) {
    http_response_code(401);
    echo json_encode(array("message" => "Token no proporcionado."));
    exit;
}

$arr = explode(" ", $authHeader);
$jwt = $arr[1] ?? '';

function encryptData($data, $key) {
    $iv = openssl_random_pseudo_bytes(16);
    $encryptedData = openssl_encrypt($data, 'AES-256-CBC', $key, 0, $iv);
    return base64_encode($encryptedData . '::' . $iv);
}

if ($jwt) {
    try {
        $decoded = JWT::decode($jwt, new Key($secretKey, 'HS256'));
        $userId = $decoded->user_id;

        $dni = $_POST['dni'] ?? null;
        $nombre = $_POST['nombre'] ?? null;
        $apellidos = $_POST['apellidos'] ?? null;
        $fecha_nacimiento = $_POST['fecha_nacimiento'] ?? null;
        $ar = $_POST['ar'] ?? null;
        $nss = $_POST['nss'] ?? null;
        $numero_cuenta_bancaria = $_POST['numero_cuenta_bancaria'] ?? null;
        $observaciones = $_POST['observaciones'] ?? null;
        $activo = $_POST['activo'] ?? "true";
        $empadronamiento = $_POST['empadronamiento'] ?? null;
        $foto = $_POST['foto'] ?? null;
        $estado = $_POST['estado'] ?? 1;
        $telefono = $_POST['telefono'] ?? null;
        $email = $_POST['email'] ?? null;
        $tlfn_familiar_1 = $_POST['tlfn_familiar_1'] ?? "+34";
        $tlfn_familiar_2 = $_POST['tlfn_familiar_2'] ?? "+34";
        $fecha_ingreso = date('Y-m-d H:i:s');

        $edad = 1;
        $mes_cumple = 1;

        if (isset($dni, $nombre, $apellidos, $ar, $nss, $fecha_nacimiento)) {
            $hashedDNI = encryptData($dni, $encryptionKey);
            $hashedAR = encryptData($ar, $encryptionKey);
            $hashedNSS = encryptData($nss, $encryptionKey);
            $hashedCuenta = encryptData($numero_cuenta_bancaria ?? "No hay", $encryptionKey);
            $hashedEmpadronamiento = encryptData($empadronamiento ?? "No hay", $encryptionKey);
            $fotoData = $foto ? $foto : null;

            $telefono = $telefono ?? "No hay";
            $email = $email ?? "No hay";
            $observaciones = $observaciones ?? "No hay";

            $sql = "INSERT INTO residentes (dni, nombre, apellidos, fecha_nacimiento, ar, nss, numero_cuenta_bancaria, observaciones, activo, empadronamiento, edad, mes_cumple, foto, estado, telefono, email, fecha_ingreso, tlfn_familiar_1, tlfn_familiar_2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            $stmt = $conn->prepare($sql);

            if ($stmt === false) {
                echo json_encode(array("status" => "error", "message" => "Error en la preparación de la consulta: " . $conn->error));
                exit();
            }

            $stmt->bind_param('sssssssssssisssssss', $hashedDNI, $nombre, $apellidos, $fecha_nacimiento, $hashedAR, $hashedNSS, $hashedCuenta, $observaciones, $activo, $hashedEmpadronamiento, $edad, $mes_cumple, $fotoData, $estado, $telefono, $email, $fecha_ingreso, $tlfn_familiar_1, $tlfn_familiar_2);

            if ($stmt->execute()) {
                $residenteId = $stmt->insert_id;
                echo json_encode(array("status" => "success", "message" => "Residente insertado correctamente.", "residente_id" => $residenteId));
            } else {
                echo json_encode(array("status" => "error", "message" => "Error al insertar residente: " . $stmt->error));
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
