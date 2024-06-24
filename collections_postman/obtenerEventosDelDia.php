<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

$autoloadPath = __DIR__ . '/../vendor/autoload.php';

if (!file_exists($autoloadPath)) {
    die("Error: No se encontró el archivo autoload.php en la ruta: $autoloadPath");
}

require $autoloadPath;
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

        $today = date("Y-m-d");

        $query = "SELECT c.id, c.residente_id, c.fecha_cita, c.hora_cita, c.lugar_cita, c.motivo_cita, c.detalles, r.nombre as nombre_residente, r.apellidos as apellidos_residente
                  FROM citas_externas c
                  JOIN residentes r ON c.residente_id = r.id
                  WHERE c.fecha_cita = ?";
        $stmt = $conn->prepare($query);

        if (!$stmt) {
            echo json_encode(['status' => 'error', 'message' => 'Error en la preparación de la consulta: ' . $conn->error]);
            exit;
        }

        $stmt->bind_param("s", $today);
        $stmt->execute();
        $result = $stmt->get_result();

        $eventos = array();

        while ($row = $result->fetch_assoc()) {
            array_push($eventos, array(
                'id' => $row['id'],
                'residente_id' => $row['residente_id'],
                'fecha_cita' => $row['fecha_cita'],
                'hora_cita' => $row['hora_cita'],
                'lugar_cita' => $row['lugar_cita'],
                'motivo_cita' => $row['motivo_cita'],
                'detalles' => $row['detalles'],
                'nombre_residente' => $row['nombre_residente'],
                'apellidos_residente' => $row['apellidos_residente']
            ));
        }

        $stmt->close();
        $conn->close();

        echo json_encode($eventos);
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
