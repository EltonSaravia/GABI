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

        $trabajadorId = $_POST['trabajador_id'] ?? null;

        if (!$trabajadorId) {
            echo json_encode(array("status" => "error", "message" => "Trabajador ID no proporcionado."));
            exit;
        }

        $sql = "SELECT id, titulo, notas, fecha_tarea_asignada, hora_tarea_asignada, trabajador_id FROM tareas WHERE trabajador_id = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("i", $trabajadorId);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $tareas = array();
            while ($row = $result->fetch_assoc()) {
                $tareas[] = $row;
            }
            echo json_encode(array("status" => "success", "data" => $tareas));
        } else {
            echo json_encode(array("status" => "error", "message" => "No se encontraron tareas."));
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
