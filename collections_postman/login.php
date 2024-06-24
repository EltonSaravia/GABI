<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

$autoloadPath = __DIR__ . '/../vendor/autoload.php'; // Ajusta la ruta según la estructura de tu proyecto

if (!file_exists($autoloadPath)) {
    die("Error: No se encontró el archivo autoload.php en la ruta: $autoloadPath");
}

require $autoloadPath;
use \Firebase\JWT\JWT;
use \Firebase\JWT\Key;

$key = "your_secret_key"; // Asegúrate de que esta clave sea la misma en ambos archivos

// Incluir el archivo de conexión a la base de datos
include 'db.php';

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Authorization, Content-Type");
header('Content-Type: application/json; charset=utf-8');

$username = $_POST['username'] ?? '';
$password = $_POST['password'] ?? '';

$response = ['status' => 'error', 'message' => 'Credenciales incorrectas'];

if (!empty($username) && !empty($password)) {
    $query = "SELECT id, nombre, contrasena, puesto, trabajador_activo FROM trabajadores WHERE nombre = ?";
    $stmt = $conn->prepare($query);
    if (!$stmt) {
        echo json_encode(['status' => 'error', 'message' => 'Error en la preparación de la consulta: ' . $conn->error]);
        exit;
    }

    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($row = $result->fetch_assoc()) {
        if ($row['trabajador_activo'] == 1) { // Verificar si el trabajador está activo
            if (password_verify($password, $row['contrasena'])) {
                $payload = [
                    'iss' => 'https://residencialontananza.com',
                    'aud' => 'https://residencialontananza.com',
                    'iat' => time(),
                    'exp' => time() + 360000, // Token válido por 10 horas
                    'user_id' => $row['id'],
                    'role' => $row['puesto']
                ];

                $jwt = JWT::encode($payload, $key, 'HS256');
                $response = [
                    'status' => 'success',
                    'message' => 'Login correcto',
                    'role' => $row['puesto'],
                    'token' => $jwt,
                    'nombre' => $row['nombre'], // Incluir el nombre del trabajador
                    'user_id' => $row['id'] // Incluir el ID del trabajador
                ];
            } else {
                $response['message'] = 'Contraseña incorrecta';
            }
        } else {
            $response['message'] = 'Trabajador no activo';
        }
    } else {
        $response['message'] = 'Usuario no encontrado';
    }

    $stmt->close();
}

$conn->close();

echo json_encode($response);
?>
