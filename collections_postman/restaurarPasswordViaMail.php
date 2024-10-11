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
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Authorization, Content-Type");

$secretKey = "your_secret_key"; // Clave secreta para JWT
$encryptionKey = "your_32_character_encryption_key"; // Clave de cifrado de 32 caracteres

function generateRandomPassword($length = 8) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomPassword = '';
    for ($i = 0; $i < $length; $i++) {
        $randomPassword .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomPassword;
}

function sendPasswordByEmail($email, $newPassword) {
    $subject = "Nueva contraseña";
    $message = "Su nueva contraseña es: " . $newPassword;
    $headers = "From: no-reply@gabi.com\r\n" .
               "Reply-To: no-reply@gabi.com\r\n" .
               "X-Mailer: PHP/" . phpversion();

    mail($email, $subject, $message, $headers);
}

function encryptData($data, $encryptionKey) {
    $iv = openssl_random_pseudo_bytes(16);
    $encryptedData = openssl_encrypt($data, 'AES-256-CBC', $encryptionKey, 0, $iv);
    return base64_encode($encryptedData . ':' . base64_encode($iv));
}

function decryptData($data, $encryptionKey) {
    $parts = explode(':', base64_decode($data));
    if (count($parts) === 2) {
        $encryptedData = $parts[0];
        $iv = base64_decode($parts[1]);
        return openssl_decrypt($encryptedData, 'AES-256-CBC', $encryptionKey, 0, $iv);
    }
    return false;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $dni = $_POST['dni'] ?? '';

    if ($dni) {
        $sql = "SELECT id, dni, email FROM trabajadores WHERE trabajador_activo = TRUE";
        $result = $conn->query($sql);

        if ($result->num_rows > 0) {
            $trabajadores = array();
            while ($row = $result->fetch_assoc()) {
                $row['dni'] = decryptData($row['dni'], $encryptionKey);
                $trabajadores[] = $row;
            }

            $trabajadorEncontrado = null;
            foreach ($trabajadores as $trabajador) {
                if ($trabajador['dni'] === $dni) {
                    $trabajadorEncontrado = $trabajador;
                    break;
                }
            }

            if ($trabajadorEncontrado) {
                $newPassword = generateRandomPassword();
                $hashedPassword = password_hash($newPassword, PASSWORD_DEFAULT);

                $updateSql = "UPDATE trabajadores SET contrasena = ? WHERE id = ?";
                $updateStmt = $conn->prepare($updateSql);
                $updateStmt->bind_param("si", $hashedPassword, $trabajadorEncontrado['id']);
                $updateStmt->execute();

                sendPasswordByEmail($trabajadorEncontrado['email'], $newPassword);

                echo json_encode(array("status" => "success", "message" => "Contraseña cambiada correctamente. Revise su email."));

                $updateStmt->close();
            } else {
                echo json_encode(array("status" => "error", "message" => "DNI no encontrado."));
            }
        } else {
            echo json_encode(array("status" => "error", "message" => "No se encontraron trabajadores."));
        }
    } else {
        echo json_encode(array("status" => "error", "message" => "DNI no proporcionado."));
    }

    $conn->close();
} else {
    http_response_code(405);
    echo json_encode(array("message" => "Método no permitido."));
}
?>
