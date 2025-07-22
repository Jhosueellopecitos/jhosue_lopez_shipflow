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

- Dirigirse a la carpeta donde esta el archivo docker-compose y ejecutar el docker-compose:
```
docker-compose up -d
```
- Ejecuta la clase principal:
```
com.puce.
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

## Pruebas de Endpoint para garantizar funcionamiento 

Si se desea realizar las pruebas referentes al funcionamiento del proyecto, ejecutar en Postman las siguientes direcciones en base a la prueba que desee realizar:

*En las secciones con {trackingid} reemplazarlo con el id del envio*

**Usuarios**

- POST /api/users - Crear usuario

- GET /api/users - Obtener todos

- GET /api/users/{id} - Por ID

- PUT /api/users/{id} - Actualizar

- DELETE /api/users/{id} - Eliminar



**Tareas (Assignments)**

- POST /api/assignments - Crear tarea (requiere user)

- GET /api/assignments - Todas las tareas

- GET /api/assignments/{id} - Por ID

- GET /api/assignments/user/{userId} - Por usuario

- PUT /api/assignments/{id} - Actualizar

- DELETE /api/assignments/{id} - Eliminar
