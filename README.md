# VTTP SSF Assessment

## Prerequisites

- JDK 23
- Local Redis server

Before running, ensure you have a local Redis server running.

```bash
$ sudo systemctl status redis-server
```

If you have `redis-cli` installed, ping to check for a response:

```bash
$ redis-cli

127.0.0.1:6379> PING
PONG
```

## Run

From source, run:

```bash
$ mvn spring-boot:run
```

If you do not have Maven installed, use the wrapper included.

```bash
$ ./mvnw spring-boot:run
```

## Docker

In the project root folder, build the docker image:

```bash
$ docker build -t ssf .
```

To run with a local Redis server on the host machine:

```bash
$ docker run -p 8080:8080 --network=host ssf
```

If you have a custom noticeboard server address, pass in the address as an environment variable:

```bash
$ docker run -p 8080:8080 -e PUBLISHING_SERVER_URL=https://example.com/ --network=host ssf
```