# Shipflow

Este proyecto es una aplicación dedicada a la gestion de entrega de tareas con **Kotlin** y **Spring Boot**. Incluye un CRUD para los paquetes (package) usando una base de datos externa como Postgresql.

---

## Requisitos

- Java 21
- IntelliJ IDEA (recomendado)
- Gradle (incluido en el wrapper del proyecto)
- Postman o similar para pruebas
- Docker
- Postgres

## Ejecucion del proyecto

- Abre el proyecto en IntelliJ.

- Dirigirse a la carpeta resource donde esta el archivo docker-compose y ejecutar el siguiente comando:
```
docker-compose up -d
```
- Ejecuta la clase principal:
```
com.pucetec.LopezJhosueShipflowApplication.kt
```

- El servidor se iniciará en:

```
http://localhost:8080
```

- La base de datos va a correr en el puerto 5432

- Ejecutar el siguiente comando para bajar el docker:
```
docker-compose down
```

## Ejecutar coleccion de Postman para las tareas

Para importar la coleccion que tiene las pruebas de endpoints realizar los siguientes pasos:

- Abrir Postman
- En el menu izquierdo, en la parte superior dar click en el boton **import**
- En la ventana que aparece dar click en el boton files
- Navegar hasta la carpeta del proyecto y en la ruta raiz estara el documento
  
```
 shipflow.postman_collection
```
-Seleccionalo y en el postman aparecera la coleccion.

## Pruebas de Endpoint para garantizar funcionamiento 

Si se desea realizar las pruebas referentes al funcionamiento del proyecto, ejecutar en Postman las siguientes direcciones en base a la prueba que desee realizar:

*En las secciones con {trackingId} reemplazarlo con el id del envio*



**Paquetes (Package)**

- POST /api/packages - Crear entrega

- GET /api/packages/{trackingId} - Obtener detalles de una entrega

- PUT /api/packages/{trackingId}/status - Actualizar el estado y comentario de la entrega

- GET /api/packages/{trackingId}/history - Obtener el historial de cambios de estado de la entrega
