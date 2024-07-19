El proyecto se corre en /eventos
El nombre del proyectos es: eventos
Ante cualquier duda consultar con Gustavo Siciliano (gussiciliano@gmail.com)
Se espera que el proyecto esté corriendo siempre

DEPLOY
Si se va a correr el proyecto con Docker Compose se tiene que crear un .env en la raíz del proyecto (al mismo nivel que el archivo de docker-compose.yml) con la siguiente información:

CODE_EMAIL_SENDER=cvlbdrttfgbhovpy

EMAIL_SENDER=test.unla.labs@gmail.com

PUBLIC_QR_LINK_SERVER=localhost:8080/eventos/qr/

MYSQLDB_DOCKER_PORT=3306

MYSQLDB_DATABASE=eventos

MYSQLDB_USER=root

MYSQLDB_ROOT_PASSWORD=root12345678

MYSQLDB_LOCAL_PORT=3309


Cabe destacar que el mail que se está usando es de prueba, cuando se defina el mail principal se va a cambiar la propiedad EMAIL_SENDER.

Por otro lado, la propiedad PUBLIC_QR_LINK_SERVER tiene el link de local (localhost:8080/eventos/qr/) al momento de deployar se debe replanzar localhost:8080 por la URL del server que corresponda, por ejem:
sg.unla.edu.ar/eventos/qr/

Al momento de levantar el proyecto se corre el script de la DB que está en /db/init/init.sql, hay que revisar si se crean las 4 tablas y si se inserta el usuario y el rol admin por defecto, en caso de que eso no pase, correr estos dos inserts:

INSERT INTO `eventos`.`user`
(`created_at`, `enabled`, `password`, `updated_at`, `username`)
VALUES
('2024-01-07 00:00:00', TRUE, '$2a$10$uXx1ScuQG5/TdSXm1Jm4TekjXJfzu9/vqhZxJGdNa6NG9abA.EZk.', '2024-01-07 00:00:00', 'admin');

INSERT INTO `eventos`.`user_role`
(`created_at`, `role`, `updated_at`, `user_id`)
VALUES
('2024-01-07 00:00:00', 'ROLE_ADMIN', '2024-01-07 00:00:00', 1);

El usuario por defecto es admin y su pass es user

-----

En caso de correr el proyecto en localhost se tiene que correr el script de /db/init/init.sql a mano y configurar las claves faltantes en el application.yml
