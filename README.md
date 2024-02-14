# Splinter API

## Introduction
This project is a Kotlin application built using the Ktor framework, designed to merge Json file and find the common keys and values. It provides a foundation for building RESTful APIs, web applications, and more.

## Features
- **RESTful API**: Utilizes Ktor's routing capabilities to create RESTful endpoints.
- **Asynchronous Processing**: Takes advantage of Kotlin's coroutines for handling asynchronous tasks efficiently.
- **Static Content Serving**: Demonstrates serving static files such as HTML, CSS, and JavaScript.
- **JSON Serialization**: Utilizes kotlinx.serialization for easy conversion between Kotlin objects and JSON.
- **Custom Routing**: Shows how to define custom routes for handling specific requests.

## Requirements
- Kotlin 1.9.x
- JDK 19 or higher
- Gradle 8.x

## Getting Started
1. Clone the repository:
   ```bash
   git clone https://github.com/BastienCarbonnier/splinter-api.git
   ```
2. Navigate into the project directory:
   ```bash
   cd splinter-api
   ```
3. Build the project:
   ```bash
   ./gradlew build
   ```
4. Run the application:
   ```bash
   ./gradlew run
   ```
5. Open a web browser and go to `http://localhost:8080` to see the application in action.

## Project Structure
The project follows a standard Gradle structure with the following main directories:

- `src/main/kotlin`: Contains the Kotlin source files.
- `src/main/resources`: Contains static resources such as HTML, CSS, and JavaScript files.

## Dependencies
- Ktor: A framework for building asynchronous servers and clients.
- kotlinx.serialization: A Kotlin library for serializing and deserializing JSON.
