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

        $trabajador_id = $_POST['trabajador_id'] ?? null;
        $tipo_turno = $_POST['tipo_turno'] ?? null;
        $fecha = $_POST['fecha'] ?? null; // Nueva variable para la fecha seleccionada

        if (isset($trabajador_id, $tipo_turno, $fecha)) {
            // Calcular la fecha y hora de inicio y fin del turno según el tipo
            $fecha_inicio = new DateTime($fecha);
            $fecha_fin = new DateTime($fecha);

            switch ($tipo_turno) {
                case 'diurno':
                    $fecha_inicio->setTime(6, 0);
                    $fecha_fin->setTime(14, 0);
                    break;
                case 'vespertino':
                    $fecha_inicio->setTime(14, 0);
                    $fecha_fin->setTime(22, 0);
                    break;
                case 'nocturno':
                    $fecha_inicio->setTime(22, 0);
                    $fecha_fin->modify('+1 day')->setTime(6, 0);
                    break;
                default:
                    echo json_encode(array("status" => "error", "message" => "Tipo de turno no válido."));
                    exit();
            }

            // Convertir a formato de fecha y hora de MySQL
            $fecha_inicio_str = $fecha_inicio->format('Y-m-d H:i:s');
            $fecha_fin_str = $fecha_fin->format('Y-m-d H:i:s');

            // Preparar la consulta SQL
            $sql = "INSERT INTO turnos (trabajador_id, fecha_inicio, fecha_fin, tipo) VALUES (?, ?, ?, ?)";
            $stmt = $conn->prepare($sql);

            if ($stmt === false) {
                echo json_encode(array("status" => "error", "message" => "Error en la preparación de la consulta: " . $conn->error));
                exit();
            }

            $stmt->bind_param('isss', $trabajador_id, $fecha_inicio_str, $fecha_fin_str, $tipo_turno);

            if ($stmt->execute()) {
                echo json_encode(array("status" => "success", "message" => "Turno asignado correctamente."));
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
