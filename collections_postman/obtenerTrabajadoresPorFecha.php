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

        // Obtener la fecha de la solicitud POST
        $fecha = $_POST['fecha'] ?? '';

        if (empty($fecha)) {
            echo json_encode(array("status" => "error", "message" => "Fecha no proporcionada."));
            exit;
        }

        // Consulta para obtener trabajadores por fecha
        $sql = "SELECT t.id, t.nombre, t.apellido_1, t.apellido_2, t.puesto, tu.tipo AS turno
                FROM trabajadores t
                JOIN turnos tu ON t.id = tu.trabajador_id
                WHERE DATE(tu.fecha_inicio) <= ? AND DATE(tu.fecha_fin) >= ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ss", $fecha, $fecha);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $trabajadores = array();
            while($row = $result->fetch_assoc()) {
                $trabajadores[] = $row;
            }
            echo json_encode(array("status" => "success", "data" => $trabajadores));
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
