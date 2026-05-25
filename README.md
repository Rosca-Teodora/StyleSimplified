# StyleSimplified

A simple and elegant desktop application built with JavaFX to help you digitize and manage your personal wardrobe, powered by a containerized PostgreSQL database.

*(Suggestion: Replace this with a real screenshot of your new futuristic UI!)*

---

## Table of Contents

- [About The Project](#about-the-project)
- [Features](#features)
- [Built With](#built-with)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [1. Running the Database Container](#1-running-the-database-container)
  - [2. Running the Application](#2-running-the-application)
- [Project Structure](#project-structure)
- [Future Improvements](#future-improvements)

---

## About The Project

StyleSimplified is a desktop app designed to tackle the age-old problem of "I have a closet full of clothes but nothing to wear." By creating a digital inventory of your clothing items, you can easily see what you own, making it easier to plan outfits and rediscover forgotten pieces.

This application is built using a modern client-server architecture. The frontend is a JavaFX desktop application, which connects to a powerful and persistent PostgreSQL database running in a Docker container. This setup ensures a clean separation between the UI and the data layer, making the application robust and scalable.

## Features

- **Digital Wardrobe Gallery:** View all your clothing items in a clean, scrollable gallery.
- **Add New Clothing Items:** Easily add items to your wardrobe, categorized as Tops, Bottoms, or Accessories.
- **Image Uploads:** Select a photo for each item. The app saves a copy in a local `wardrobe_images` directory for persistence.
- **Persistent Database Backend:** Uses **ORMLite** and a **PostgreSQL** database running in Docker to ensure your data is safely stored and available between sessions.
- **Command Pattern:** Uses the command pattern for adding items, making future extensions like "Undo/Redo" possible.

## Built With

- [Java](https://www.java.com/) - The core programming language.
- [JavaFX](https://openjfx.io/) - The modern GUI framework for the user interface.
- [Maven](https://maven.apache.org/) - For project build management and dependencies.
- [PostgreSQL](https://www.postgresql.org/) - The relational database for data storage.
- [Docker](https://www.docker.com/) - For containerizing and managing the database service.
- [ORMLite](http://ormlite.com/) - For object-relational mapping between Java objects and the database.

## Getting Started

To get a local copy up and running, follow these two main steps.

### Prerequisites

- **JDK (Java Development Kit)** - Version 17 or newer.
- **Apache Maven** - To build and run the JavaFX application.
- **Docker Desktop** - Must be installed and running on your system.

### 1. Running the Database Container

First, you need to start the PostgreSQL database. This command will pull the official Postgres image, start a container, and create a persistent volume to save your data even if the container is removed.

Open a terminal and run the following command. **Choose a secure password** and replace `"your_password"` with it.

```sh
docker run --name stylesimplified-db -d \
  -e POSTGRES_PASSWORD=your_password \
  -e POSTGRES_USER=wardrobe_user \
  -e POSTGRES_DB=wardrobe \
  -p 5432:5432 \
  -v stylesimplified_pgdata:/var/lib/postgresql/data \
  postgres
```

- `--name stylesimplified-db`: Gives the container a memorable name.
- `-d`: Runs the container in detached mode (in the background).
- `-e`: Sets environment variables for the database user, password, and name.
- `-p 5432:5432`: Maps the standard PostgreSQL port from your local machine to the container.
- `-v stylesimplified_pgdata...`: Creates a named volume to persist your database data.

To check if the container is running, use `docker ps`.

### 2. Running the Application

Before running the app, ensure your Java code is configured to connect to the database. The connection string in your `WardrobeService` or a similar configuration class should look like this:

`String databaseUrl = "jdbc:postgresql://localhost:5432/wardrobe";`

1.  **Clone the repository:**
    ```sh
    git clone https://your-repository-url.com/StyleSimplified.git
    ```
2.  **Navigate to the project directory:**
    ```sh
    cd StyleSimplified
    ```
3.  **Run the application using Maven:**
    This command will start the JavaFX application, which will then connect to your running Docker container.
    ```sh
    mvn clean javafx:run
    ```

## Project Structure

-   **`src/main/java/com/example/stylesimplified/`**: Main package root for all `.java` source files.
-   **`src/main/resources/com/example/stylesimplified/`**: Contains all non-code resources like `.fxml` and `.css`.
-   **`wardrobe_images/`**: (Created at runtime) Stores copies of all uploaded clothing images.
-   **`pom.xml`**: The Maven project configuration file, listing all dependencies (including `postgresql` and `ormlite-jdbc`).

## Future Improvements

- **Edit and Delete Items:** Implement functionality to modify or remove existing clothing items from the database.
- **Outfit Creation:** A new section to create and save outfits by combining different `ClothingItem`s.
- **Search and Filter:** Add the ability to search for items by name or filter them by type, color, or tags.
- **Handle Image Rotation:** Read EXIF metadata from images to automatically correct the orientation of photos taken on a phone.
