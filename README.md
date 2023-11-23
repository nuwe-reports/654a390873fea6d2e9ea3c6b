# Reto Nuwe-Accenture

Java back end developer challenge

### Requisitos

- [Docker](https://www.docker.com/get-started)
- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)

### Configuración Local

1. Clona este repositorio: `git clone https://github.com/nuwe-reports/654a390873fea6d2e9ea3c6b.git accwe-hospital`
2. Navega al directorio del proyecto: `cd accwe-hospital`
3. Construye el contenedor Docker para la base de datos: `docker build -t accwe-hospital-db -f Dockerfile.mysql .`
4. Construye el contenedor Docker para el microservicio: `docker build -t accwe-hospital -f Dockerfile.maven . `
5. Ejecuta el contenedor MySQL: `docker run -d -p 3306:3306 --name accwe-hospital-db accwe-hospital-docker run -d -p 3306:3306 --name accwe-hospital-db accwe-hospitaldb`
6. Ejecuta el contenedor Maven: `docker run -d -p 8080:8080 --name accwe-hospital accwe-hospital`

### Acceso a la Aplicación

- Microservicio: [http://localhost:8080](http://localhost:8080)
- Base de Datos (MySQL):
    - Usuario: root
    - Contraseña: root
    - Nombre de la Base de Datos: accwe-hospital

## Uso

-

## UML - GRAPH. CLASS DIAGRAM

<img src="./docs/UML-Acce-hospital.png" width="1600"/>

## Contribuciones

-

## Licencia

-

## Contacto

Para preguntas o comentarios, ponte en contacto con [Manuel Valera](mailto:manuelvalera@hotmail.com).


**Estado del Proyecto:**
- Versión Actual: 0.0.1
