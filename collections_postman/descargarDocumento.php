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
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? '';

if (!$authHeader) {
    http_response_code(401);
    echo json_encode(array("message" => "Token no proporcionado."));
    exit;
}

$arr = explode(" ", $authHeader);
$jwt = $arr[1] ?? '';

if ($jwt) {
    try {
        $decoded = JWT::decode($jwt, new Key($secretKey, 'HS256'));
        $userId = $decoded->user_id;

        $documentoId = $_POST['id'] ?? null;

        if (!$documentoId) {
            echo json_encode(array("status" => "error", "message" => "ID no proporcionado."));
            exit;
        }

        $sql = "SELECT nombre_archivo, tipo_archivo, contenido FROM documentos WHERE id = ?";
        $stmt = $conn->prepare($sql);
        if ($stmt === false) {
            echo json_encode(array("status" => "error", "message" => "Error en la preparación de la consulta: " . $conn->error));
            exit();
        }

        $stmt->bind_param('i', $documentoId);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows === 0) {
            echo json_encode(array("status" => "error", "message" => "Documento no encontrado."));
            exit();
        }

        $row = $result->fetch_assoc();

        $nombreArchivo = $row['nombre_archivo'];
        $tipoArchivo = $row['tipo_archivo'];
        $contenido = $row['contenido'];

        header('Content-Type: ' . $tipoArchivo);
        header('Content-Disposition: attachment; filename="' . $nombreArchivo . '"');
        echo $contenido;

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
