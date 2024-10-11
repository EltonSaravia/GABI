<?php
include 'db.php'; // Asegúrate de que este archivo contiene la conexión correcta a tu base de datos

// Configurar cabeceras para CORS y tipo de contenido
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: Authorization, Content-Type");
header('Content-Type: application/json; charset=utf-8');

// Obtener fecha y hora actuales
$fecha_actual = date('Y-m-d');
$hora_actual = date('H:i:s');

// Consulta SQL para obtener los trabajadores en turno
$query = "SELECT t.id, t.dni, t.nombre, t.apellido_1 AS apellido1, t.apellido_2 AS apellido2, t.puesto, t.telefono, t.email 
          FROM trabajadores t 
          JOIN turnos tu ON t.id = tu.trabajador_id 
          WHERE CURDATE() BETWEEN DATE(tu.fecha_inicio) AND DATE(tu.fecha_fin) 
          AND (
              (CURTIME() BETWEEN '06:00:00' AND '13:59:59' AND tu.tipo = 'diurno') 
              OR 
              (CURTIME() BETWEEN '14:00:00' AND '21:59:59' AND tu.tipo = 'vespertino') 
              OR 
              ((CURTIME() >= '22:00:00' OR CURTIME() < '06:00:00') AND tu.tipo = 'nocturno')
          )";

$stmt = $conn->prepare($query);
$stmt->execute();
$result = $stmt->get_result();
$trabajadores = [];

while ($row = $result->fetch_assoc()) {
    $trabajadores[] = $row;
}

$stmt->close();
$conn->close();

echo json_encode($trabajadores);
?>
