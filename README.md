# GeoRegistro

## Descripción
Aplicación Android para registrar incidencias georreferenciadas en terreno, desarrollada en Java. Permite guardar datos en Firebase Firestore, capturar la ubicación actual mediante GPS, visualizar las incidencias en Google Maps y detectar movimiento del dispositivo mediante el acelerómetro.

## Requisitos de Entorno
* **Android Studio:** 3.0 o superior
* **Lenguaje de Programación:** Java
* **Dependencias:** Google Maps SDK, Firebase Firestore, SensorManager para acelerómetro
* **SDK Mínimo:** API 27

## Instrucciones de Ejecución y Pruebas
1. Clonar este repositorio en Android Studio.
2. Sincronizar el proyecto con Gradle.
3. Ejecutar en un emulador o dispositivo físico con conexión a internet y servicios de Google Play.
4. Otorgar permisos de ubicación cuando la aplicación lo solicite.

## Configuración de Seguridad (Firebase y Google Maps)
Por motivos de seguridad, las credenciales privadas no están incluidas en este repositorio. Para ejecutar el proyecto debes:
1. **Google Maps:** Crear un archivo `local.properties` en la raíz del proyecto y agregar tu clave API así: `MAPS_API_KEY=TuClaveAqui`.
2. **Firebase:** Conectar el proyecto a tu propia instancia de Firebase Firestore desde *Tools > Firebase* en Android Studio, asegurando que las reglas de lectura/escritura en la consola web no estén completamente abiertas.