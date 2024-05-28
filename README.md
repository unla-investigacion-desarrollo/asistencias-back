### prueba Jenkins 1

http://localhost:8080/swagger-ui/index.html, ver: https://www.baeldung.com/spring-rest-openapi-documentation

# Asistencias

Para correr por linea de comando se puede realizar lo siguiente:

- set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_221

- mvn clean install

Antes de configurar el proyecto se tiene crear la base de datos, se puede hacer con un comando simple de MySQL:

- CREATE DATABASE nombreDB;

Luego, para usar el proyecto, tenemos que configurarlo, para ello se tiene que crear un archivo llamado ".env" y dentro agregar las siguiente credenciales:
```
DB_URL=TusDatos
DB_USERNAME=TusDatos
DB_PASSWORD=TusDatos
MAIL_EMAIL=MailAsistencias
MAIL_PASS=PassMailAsistencias
```
Por ejemplo:
```
DB_URL=jdbc:mysql://localhost:3306/nombreDeLaDB
DB_USERNAME=rootUser
DB_PASSWORD=rootPass
MAIL_EMAIL=alertaasistencias@gmail.com
MAIL_PASS=pppasistencias2022
```
Si esto no funciona, puede ser que no se tomen correctamente las env vars, otra forma de setearlas es:
- si estas usando Sprint Tool Suite, seleccionando la opción Run>RunConfigurations -> dentro ir a Environment, y en ese lugar colocar todas las variables de entorno.
- si estás usando VS Code, tenes que agregar la extensión de STS, luego, ir a Run->add configurations, esto te va a abrir un archivo llamado launch.json. Dentro del array configurations, en el primer json agregar la key "env" y asignarle un json con las variables de entorno, por ejemplo:
```
"env": {
  "DB_URL": "jdbc:mysql://localhost:3306/nombreDeLaDB",
  "DB_USERNAME": "rootUser",
  "DB_PASSWORD": "rootPass", 
  "MAIL_EMAIL": "alertaasistencias@gmail.com",
  "MAIL_PASS": "pppasistencias2022"
}
```

Para correr el proyecto lo podes hacer desde el IDE que tengas, o sino, desde consola con el comando:

-  mvn spring-boot:run
