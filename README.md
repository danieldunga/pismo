# Pismo
Pismo test

#### Requirements
- Mavem 3.6.3
- Java 11
- Docker

#### Build
```sh
mvn clean package dockerfile:build
```
#### Run
```sh
docker run -it -p 8080:8080 dbraga/pismo-app
```

#### Use
Endpoints are available at ``http://localhost:8080``