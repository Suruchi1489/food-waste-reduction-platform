# Food Waste Reduction Platform

## About the Project
The Food Waste Reduction Platform is a web application built using Java, Spring Boot, and MySQL to help manage surplus food donations. It allows users to add food donations, search for available food, track donation status, and manage the claiming and pickup process.

## Features
- Add, view, update, and delete food donations
- Search donations by food name and pickup location
- Filter donations by status: Available, Claimed, and Picked Up
- Claim available food donations
- Confirm food pickup
- View donation summary statistics
- Validate input and handle errors
- Interactive web interface

## Technologies Used
- **Backend:** Java, Spring Boot
- **Frontend:** HTML, CSS, JavaScript
- **Database:** MySQL
- **Build Tool:** Maven
- **API Testing:** Postman
- **Version Control:** Git and GitHub

## Main API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/donations` | Add a donation |
| GET | `/api/donations` | Get all donations |
| GET | `/api/donations/{id}` | Get a donation by ID |
| PUT | `/api/donations/{id}` | Update a donation |
| DELETE | `/api/donations/{id}` | Delete a donation |
| PATCH | `/api/donations/{id}/status` | Update donation status |
| GET | `/api/donations/status/{status}` | Filter donations by status |
| GET | `/api/donations/search?foodName=...` | Search by food name |
| GET | `/api/donations/location/{location}` | Search by pickup location |
| PATCH | `/api/donations/{id}/claim` | Claim a donation |
| PATCH | `/api/donations/{id}/pickup` | Confirm food pickup |
| GET | `/api/donations/summary` | View donation statistics |

## How to Run the Project

1. Install Java 21, MySQL, and Git.
2. Clone or download this repository.
3. Configure your MySQL database connection in `src/main/resources/application.properties`.
4. Create the database configured in your application settings.
5. Open a terminal in the project root directory, where `pom.xml` is located.
6. Run the application:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

7. Open the application in your browser:

   `http://localhost:8080/`

## Future Improvements
- User authentication and authorization
- Role-based access for donors and food recipients
- Improved responsive user interface
- Automated testing

## Author
Suruchi Kumari
