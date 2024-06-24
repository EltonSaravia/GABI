# G.A.B.I (Gestión Administrativa de Bienestar Integral)

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

## Tabla de Contenidos

- [1. Contexto del Proyecto](#1-contexto-del-proyecto)
- [2. Ámbito y Entorno](#2-Ámbito-y-entorno)
- [3. Análisis de la Realidad](#3-análisis-de-la-realidad)
- [4. Solución y Justificación de la Solución Propuesta](#4-solución-y-justificación-de-la-solución-propuesta)
- [5. Destinatarios](#5-destinatarios)
- [6. Objetivo del Proyecto](#6-objetivo-del-proyecto)
- [7. Arquitectura y Funcionamiento](#7-arquitectura-y-funcionamiento)
- [8. Instalación](#8-instalación)
- [9. Uso](#9-uso)


## 1. Contexto del Proyecto

El proyecto "G.A.B.I" es una iniciativa para desarrollar una aplicación móvil destinada a mejorar la gestión de residencias de ancianos. La idea surge ante la necesidad de proporcionar una herramienta eficiente para el personal de estas instituciones, facilitando el seguimiento de las tareas diarias y el acceso a información vital sobre los pacientes.

El nombre GABI es un acrónimo de Gabriela, que es un nombre de niña de origen hebreo cuyo significado es “fuerza de Dios”. Es la variante femenina del nombre de Gabriel, que era uno de los arcángeles más importantes: es el mensajero del cielo que anuncia a la Virgen María el nacimiento de Cristo.

## 2. Ámbito y Entorno

Este proyecto se ubica en el ámbito de la gestión de salud y asistencia a personas mayores, específicamente en residencias de ancianos. El entorno de uso será principalmente dentro de estas instituciones, abarcando diversas áreas como gestión de residentes, comunicación con familiares, y administración de personal.

## 3. Análisis de la Realidad

Actualmente, muchas residencias de ancianos enfrentan desafíos en la gestión eficiente de sus recursos y comunicación efectiva. Falta una solución integrada que facilite el manejo de la información de los residentes, la asignación de tareas al personal y la comunicación con los familiares de los residentes. Después de realizar una investigación en diversas residencias, se llegó a la conclusión de que la administración y documentación de las tareas a día de hoy en muchas de estas instituciones se manejan en papel, y otras ni siquiera tienen en cuenta el valor de la comunicación entre empleados para distribuir las tareas de forma apropiada.

Los encargados suelen crear hojas de bitácoras que los empleados deben rellenar cada vez que realizan una acción, ya sea el fichaje, la limpieza de una habitación o alguna acción con un residente. Me comentan que lo habitual es que los empleados rellenen al final de su jornada lo que han hecho a lo largo del día, cuando en realidad se les da instrucciones de que deben rellenar las hojas en el acto.

Además, me indican que la comunicación en residencias con superficies amplias es complicada ya que en muchas residencias utilizan radios o directamente no utilizan nada y si necesitan ayuda deben recorrer el recinto hasta encontrar a la persona, por ejemplo, para levantar a algún residente que debe ser cambiado de posición cada hora y se debe hacer entre 2 personas.

A las enfermeras se les dificulta la labor de consultar historiales médicos ya que todo lo almacenan en papel en la mayoría de ocasiones en residencias pequeñas, y para suministrar medicamentos suelen imprimir una hoja cuando hay variaciones en algún residente que en ocasiones los auxiliares de geriatría no revisan y se pueden llegar a cometer errores.

## 4. Solución y Justificación de la Solución Propuesta

La solución propuesta es la aplicación "G.A.B.I", diseñada para abordar las necesidades de gestión y comunicación en las residencias de ancianos. La aplicación permitirá la gestión eficiente de los residentes, la asignación de tareas al personal, el acceso a información rápida y un historial de las diferentes acciones que se llevan a cabo al final del día. Esto justifica su desarrollo, ya que mejorará significativamente la operatividad y la calidad del servicio en estas instituciones.

El feedback recibido tras comunicar a los diferentes administradores sobre la intención de crear esta app ha sido positivo. Hemos llegado a la conclusión de que tener la información de manera ágil y llevar a cabo un control informatizado en una residencia acotará el margen de error del personal menos cualificado y ayudará a la administración de la residencia para optimizar los tiempos y el personal.

Los residentes notarán una mejoría en su ambiente debido a que por causa de errores se crean a veces conflictos entre empleados que pueden llegar a enturbiar el ambiente de calma y tranquilidad que las residencias quieren transmitir.

Los empleados agradecerán tener unas vías para reclamar reconocimiento por su trabajo y les permitirá centrarse únicamente en sus tareas sin tener que estar pensando en que se les culpe de un error de alguien más.

Para el administrador es conveniente obtener datos del trabajo de sus empleados ya que esto le puede impulsar a tomar medidas de refuerzo positivo para sus empleados u otras acciones.

## 5. Destinatarios

Los principales destinatarios de "G.A.B.I" son las residencias de ancianos, incluyendo administradores, personal de salud y asistentes. Además, indirectamente beneficia a los residentes y sus familiares, mejorando la calidad del cuidado y la comunicación.

## 6. Objetivo del Proyecto

El objetivo es desarrollar una aplicación móvil que centralice y facilite la gestión de las tareas en residencias de ancianos, mejorando la eficiencia operativa y la calidad de la comunicación entre el personal y los residentes.

Los empleados tendrán una herramienta para dejar reflejadas sus tareas realizadas y además tendrán acceso fácil a la información de cada paciente. El administrador podrá tener un histórico de las diferentes acciones realizadas para un residente. Es importante almacenar toda la información relevante para que los enfermeros puedan seguir las recetas de los doctores y controlar los calendarios de las dosis o vacunas que deben realizar, por ello deben tener un acceso sencillo a la información de los residentes.

## 7. Arquitectura y Funcionamiento

La aplicación realiza llamadas a un servidor con un dominio que tiene la lógica en PHP para comunicarse con MySQL y realizar operaciones CRUD. Aquí hay un resumen de su funcionamiento:

1. **Autenticación**: 
    - La primera API que se llama desde la app es la de inicio de sesión.
    - Devuelve un token JWT que permite el uso del resto de APIs.
2. **CORS**:
    - El servidor tiene CORS habilitado para permitir su uso con PHP.
3. **Roles y Permisos**:
    - Existen diferentes roles en la aplicación: administrador y auxiliar de enfermería.
    - Cada rol tiene diferentes actividades que interactúan con diversos fragmentos.
4. **Solicitud de Usuario de Prueba**:
    - Para obtener un usuario de prueba, los interesados deben contactar conmigo para que les habilite un usuario.
5. **Postman**:
    - Para realizar solicitudes a los endpoints, se pueden usar las colecciones de Postman que están en el paquete `collections_postman`.

## 8. Instalación

Para ver el proyecto, sigue estos pasos:

1. Descarga e instala [Android Studio](https://developer.android.com/studio).
2. Clona el repositorio:
    ```bash
    git clone https://github.com/usuario/mi-aplicacion-android.git
    ```
3. Importa el proyecto en Android Studio.
4. Asegúrate de tener instalado el JDK y las dependencias necesarias.
5. Compila y ejecuta la aplicación en un emulador o dispositivo físico.

## 9. Uso

Para iniciar la aplicación, simplemente ábrela en tu dispositivo. Aquí tienes algunos ejemplos de uso:

### Login

El login se realiza mediante la primera API y devuelve un token JWT que permite interactuar con las demás APIs.

### Roles

- **Administrador**: Puede crear usuarios auxiliares.
- **Auxiliar de enfermería**: Tiene acceso a actividades específicas relacionadas con su rol.

### Postman

Para probar las API, puedes usar las colecciones de Postman incluidas en el paquete `collections_postman`.



Para cualquier duda o solicitud de usuario de prueba, por favor, contacta conmigo.
