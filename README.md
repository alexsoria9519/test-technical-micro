# test-technical-micro

## Descripcion

Este proyecto de prueba de microservicios

## Tecnologías

- Java 17
- Spring Boot
- Gradle
- Postgresql

## Base de datos

- Se debe descargar el archivo script.sql que esta en la raiz del proyecto y ejecutarlo en una base de datos postgresql
- Son dos base de datos una para el microservicio de clientes y otra base de datos para el microservicio de cuentas

## Microservicio de clientes

- Se debe ejecutar el microservicio de clientes en el puerto 8082

### Instalación

- Se debe ejecutar el siguiente comando para instalar las dependencias

```bash
 cd person-client-microservices
 ./gradlew build
```


## Microservicio de cuentas

- Se debe ejecutar el microservicio de cuentas en el puerto 8081

### Instalación

- Se debe ejecutar el siguiente comando para instalar las dependencias

```bash
 cd accountmicroservice
 ./gradlew build
```

## Prueba

- Se agregaron pruebas unitarias para los microservicios
- Se agrega un archivo de postman para probar los servicios


 