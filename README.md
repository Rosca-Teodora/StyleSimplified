# StyleSimplified

A simple and elegant desktop application built with JavaFX to help you digitize and manage your personal wardrobe.

## Table of Contents

- [About The Project](#about-the-project)
- [Features](#features)
- [Built With](#built-with)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Future Improvements](#future-improvements)

---

## About The Project

StyleSimplified is a desktop app designed to tackle the age-old problem of "I have a closet full of clothes but nothing to wear." By creating a digital inventory of your clothing items, you can easily see what you own, making it easier to plan outfits and rediscover forgotten pieces.

This application is built using modern Java technologies and follows the Model-View-Controller (MVC) design pattern for a clean and scalable architecture.

## Features

- **Add New Clothing Items:** Easily add new items to your digital wardrobe.
- **Categorize Your Clothes:** Differentiate between types of clothing like Tops, Bottoms, and Accessories.
- **Custom Attributes:** Each category has specific fields (e.g., *Sleeve Length* for Tops, *Fit Type* for Bottoms).
- **Image Uploads:** Select a photo for each clothing item from your computer for easy visual identification.
- **Persistent Gallery:** Your uploaded images are saved locally in a `wardrobe_images` folder, and your collection is displayed in a clean, scrollable gallery.
- **Command Pattern:** Uses the command pattern for adding items, making future extensions like "Undo/Redo" possible.

## Built With

- [Java](https://www.java.com/) - The core programming language.
- [JavaFX](https://openjfx.io/) - The modern GUI framework for building the user interface.
- [Maven](https://maven.apache.org/) - For project build management and dependencies.

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

Make sure you have the following software installed on your machine:
- **JDK (Java Development Kit)** - Version 17 or newer is recommended.
- **Apache Maven** - To build the project and run it from the command line.

### Running the Application

1.  **Clone the repository:**
    ```sh
    git clone https://your-repository-url.com/StyleSimplified.git
    ```
2.  **Navigate to the project directory:**
    ```sh
    cd StyleSimplified
    ```
3.  **Run the application using Maven:**
    The JavaFX Maven plugin makes it easy to compile and run the project with a single command.
    ```sh
    mvn clean javafx:run
    ```

The application window should now open.

## Project Structure

The project follows a standard MVC (Model-View-Controller) architecture to separate concerns:

-   **`src/main/java/com/example/stylesimplified/`**: Main package root.
    -   **`backend/models`**: Contains the data classes (POJOs) like `ClothingItem`, `Top`, `Bottom`, etc. This is the **Model**.
    -   **`backend/controllers`**: Contains the logic that connects the UI to the data, such as `WardrobeController` and `AddItemController`. This is the **Controller**.
    -   **`backend/services`**: Contains business logic classes like `WardrobeService` (using a Singleton pattern) that perform operations on the data.
    -   **`backend/commands`**: Implements the Command design pattern for actions like adding items.
    -   **`HelloApplication.java` / `Launcher.java`**: The main entry points for the JavaFX application.
-   **`src/main/resources/com/example/stylesimplified/`**: Contains the FXML files that define the user interface. This is the **View**.
-   **`wardrobe_images/`**: (Created at runtime in the project root) The directory where uploaded clothing images are stored.

## Future Improvements

- **Edit and Delete Items:** Implement functionality to modify or remove existing clothing items.
- **Outfit Creation:** A new section to create and save outfits by combining different `ClothingItem`s.
- **Search and Filter:** Add the ability to search for items by name or filter them by type, color, or tags.
- **Handle Image Rotation:** Read EXIF metadata from images to automatically correct the orientation of photos taken on a phone.
