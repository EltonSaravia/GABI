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

        $titulo = $_POST['titulo'] ?? null;
        $notas = $_POST['notas'] ?? null;
        $trabajador_id = $_POST['trabajador_id'] ?? null;
        $fecha_tarea_asignada = $_POST['fecha_tarea_asignada'] ?? null;
        $hora_tarea_asignada = $_POST['hora_tarea_asignada'] ?? null;

        if (isset($titulo, $trabajador_id, $fecha_tarea_asignada, $hora_tarea_asignada)) {
            // Verificar y formatear las fechas y horas
            $fecha_tarea_asignada = date('Y-m-d', strtotime($fecha_tarea_asignada));
            $hora_tarea_asignada = date('H:i:s', strtotime($hora_tarea_asignada));
            $fecha_hora_tarea_asignada = $fecha_tarea_asignada . ' ' . $hora_tarea_asignada;
            
            // Preparar la consulta SQL
            $sql = "INSERT INTO tareas (titulo, notas, estado, trabajador_id, fecha_tarea_asignada, hora_tarea_asignada) VALUES (?, ?, 0, ?, ?, ?)";
            $stmt = $conn->prepare($sql);

            if ($stmt === false) {
                echo json_encode(array("status" => "error", "message" => "Error en la preparación de la consulta: " . $conn->error));
                exit();
            }

            $stmt->bind_param('ssiss', $titulo, $notas, $trabajador_id, $fecha_tarea_asignada, $fecha_hora_tarea_asignada);

            if ($stmt->execute()) {
                echo json_encode(array("status" => "success", "message" => "Tarea insertada correctamente."));
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
