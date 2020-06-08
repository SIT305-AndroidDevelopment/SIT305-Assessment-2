git pull origin master
docker-compose down
mvn clean package dockerfile:build
docker-compose up -d