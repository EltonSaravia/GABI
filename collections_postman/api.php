<?php
include 'db.php'; // Incluye la conexión a la base de datos

header("Content-Type: application/json"); // Configura el contenido como JSON
$response = array(); // Array para la respuesta JSON

$sql = "SELECT * FROM tu_tabla"; // Cambia 'tu_tabla' por el nombre de tu tabla real
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // Datos de salida de cada fila
    while($row = $result->fetch_assoc()) {
        array_push($response, $row);
    }
    echo json_encode($response); // Convertir la respuesta a JSON
} else {
    echo json_encode(array("message" => "No se encontraron datos"));
}
$conn->close(); // Cerrar la conexión
?>
