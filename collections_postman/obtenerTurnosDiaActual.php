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

error_log("Authorization Header: " . $authHeader); // Log de la cabecera de autorización

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
        error_log("Decoded JWT: " . json_encode($decoded)); // Log del JWT decodificado
        $userId = $decoded->user_id;

        $today = date("Y-m-d");

        $query = "SELECT t.id, t.trabajador_id, t.fecha_inicio, t.fecha_fin, t.tipo, tr.nombre, tr.apellido_1, tr.apellido_2, tr.puesto
                  FROM turnos t
                  JOIN trabajadores tr ON t.trabajador_id = tr.id
                  WHERE DATE(t.fecha_inicio) = ?";
        $stmt = $conn->prepare($query);

        if (!$stmt) {
            echo json_encode(['status' => 'error', 'message' => 'Error en la preparación de la consulta: ' . $conn->error]);
            exit;
        }

        $stmt->bind_param("s", $today);
        $stmt->execute();
        $result = $stmt->get_result();

        $turnos = array();

        while ($row = $result->fetch_assoc()) {
            array_push($turnos, array(
                'id' => $row['id'],
                'trabajador_id' => $row['trabajador_id'],
                'fecha_inicio' => $row['fecha_inicio'],
                'fecha_fin' => $row['fecha_fin'],
                'tipo' => $row['tipo'],
                'nombre' => $row['nombre'],
                'apellido_1' => $row['apellido_1'],
                'apellido_2' => $row['apellido_2'],
                'puesto' => $row['puesto']
            ));
        }

        $stmt->close();
        $conn->close();

        echo json_encode(['status' => 'success', 'turnos' => $turnos]);
    } catch (Exception $e) {
        http_response_code(401);
        error_log("Error: " . $e->getMessage()); // Log del error
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
