# SIT Booking Backend

This project implements the API that powers the internal workspace booking application for Studentersamfundets Interne Teater.
It is still a work in progress, and not yet deployed.

# Local development

The API is written in Kotlin using [Ktor](https://ktor.io/) as a server framework and [exposed](https://github.com/JetBrains/Exposed) to interact with the database.
The dependencies are defined in [the gradle config](build.gradle.kts).
The application expects a Postgres database to be running at `localhost:55000` with a database called `sitbooking` and an admin user with the username `sitbooking` and password `secret`.
You can either create it yourself, or you can use the supplied [`docker-compose`](docker-compose.yml) file:

```sh
docker-compose up
```
You do not need to define any tables.

To run the application, use the supplied Gradle wrapper:

```sh
./gradlew run --console=plain
```
After a short while, you should see the following logged to the console:
```log
[main] INFO  ktor.application - Responding at http://0.0.0.0:8080
```
The application is now running and listening for requests on `localhost:8080`. A description of the available routes will be made available as work with the application moves forward.