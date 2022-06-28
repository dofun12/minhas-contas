docker volume rm mysql-contas
docker volume create mysql-contas
docker run --rm --name mysql-contas -p 3306:3306 -e MYSQL_DATABASE=contas -e MYSQL_ROOT_PASSWORD=contas_adm -e MYSQL_USER=contas -e MYSQL_PASSWORD=contas -v mysql-contas:/var/lib/mysql mysql:latest
