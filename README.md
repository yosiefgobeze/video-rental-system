# Video_Rental_System Online API Service

A RESTful API built with **Spring Boot 4.1.1** and **Java 21** to handle video rentals using role-based Access Control via **Spring Security Basic Auth** and **MySQL** database persistence.

## Tech Stack
* **Language:** Java 21
* **Framework:** Spring Boot
* **Security:** Spring Security (Basic Auth + BCrypt Password Hashing)
* **Database:** MySQL
* **Build Tool:** Maven

## Database Setup
1. Create a MySQL database named `video_rental_db`.
2. Configure credentials in `src/main/resources/application.properties`.

## Run the Application
Open the project in IntelliJ IDEA and run `VideoRentalSystemApplication.java`. The server runs on `http://localhost:8080`.

## API Endpoints
* `POST /api/auth/register` - Public Registration (Roles: CUSTOMER, ADMIN)
* `GET /api/auth/login` - Public Login Verification
* `GET /api/videos` - Private (Authenticated users can view videos)
* `POST /api/admin/videos` - Admin Only (Create video)
* `PUT /api/admin/videos/{id}` - Admin Only (Update video)
* `DELETE /api/admin/videos/{id}` - Admin Only (Delete video)