# Photos Application

## Overview

The **Photos Application** is a JavaFX desktop application for managing photo albums. It allows users to:

* Create, view, and manage albums.
* Search photos by date range or tags.
* Create a new album from search results.
* Store user and album data persistently.

---

## Features

### Album Management

* Add new albums.
* Add/remove photos within albums.
* Display album contents with captions and photo metadata.

### Photo Search

* **By Date Range:** Filter photos based on start and end dates.
* **By Tags:** Search photos using one or two tags, with `AND`/`OR` operators.
* **Search Results:** Display matching photos in a ListView.

### Album Creation from Search

* Create a new album from search results.
* Prevent duplicate album names.
* Save newly created albums persistently.

---

## Project Structure

```
Photos/
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ photos/
│  │  │     ├─ controller/
│  │  │     │  ├─ UserController.java
│  │  │     │  └─ SearchController.java
│  │  │     ├─ model/
│  │  │     │  ├─ User.java
│  │  │     │  ├─ Album.java
│  │  │     │  ├─ Photo.java
│  │  │     │  └─ UserManager.java
│  │  │     └─ StorageManager.java
│  │  └─ resources/
│  │     └─ photos/
│  │        └─ view/
│  │           ├─ UserView.fxml
│  │           └─ SearchView.fxml
├─ pom.xml
└─ README.md
```

---

## Requirements

* **Java JDK 21**
* **JavaFX 21**
* **Maven** for building and dependency management

---

## Setup Instructions

1. **Clone the repository**

```bash
git clone <repository-url>
cd Photos
```

2. **Build with Maven**
```bash
mvn clean install
```

3. **Run the application**

```bash
mvn javafx:run
```

---

## Usage

1. Launch the application.
2. Log in or create a new user using admin privileges.
3. Create a new album or open an existing album.
4. Add photos to albums, move or copy photos between albums, drag and drop photos to the album.
5. To search:

   * Use **Search by Date Range**: Enter start and end dates (`yyyy-mm-dd`) and click `Search by Date`.
   * Use **Search by Tags**: Enter tag types and values, select `AND`/`OR` operator, and click `Search by Tags`.
6. Search results appear in the ListView.
7. Click **Create Album from Results** to save the search results as a new album.


---


## Author

**Devanshi Parekh**
Rutgers University – Software Methodology Course Project
