# Todo App with Spring Boot and Couchbase

RESTful API for managing todo items.

## Prerequisites

- Docker
- Java 17
- Maven

## Build

   Clone the repository:
   ```
   git clone https://github.com/erdiiyilmazz/todoapp.git
   cd todoapp
   ```

   Build the application:
   ```
   mvn clean install
   ```
## Test

   Run unit tests: 
   ```
   mvn test
   ```
## Run the app with dependencies

   Build and run on docker container: 
   ```
   docker-compose up --build
   ```

  Once the application is running, you can access the Swagger UI for API documentation at:

  - http://localhost:8080/swagger-ui.html

  Couchbase UI is available at:

  - http://localhost:8091

### User Operations
- `POST /api/users/register`: Register a new user
  - Request body: `{ "username": "string", "password": "string" }`
  - Response: `{ "token": "string" }`
- `POST /api/users/login`: Authenticate a user and receive a JWT
  - Request body: `{ "username": "string", "password": "string" }`
  - Response: `{ "token": "string" }`

### Todo Item Operations
- `GET /api/items`: Get all todo items for the authenticated user
  - Response: `[{ "id": "string", "title": "string", "description": "string", "completed": boolean, "createdAt": "string", "updatedAt": "string" }]`
- `GET /api/items/{itemId}`: Get a specific todo item by ID
  - Response: `{ "id": "string", "title": "string", "description": "string", "completed": boolean, "createdAt": "string", "updatedAt": "string" }`
- `POST /api/items`: Create a new todo item
  - Request body: `{ "title": "string", "description": "string", "completed": boolean }`
  - Response: `{ "id": "string", "title": "string", "description": "string", "completed": boolean, "createdAt": "string", "updatedAt": "string" }`
- `PUT /api/items/{itemId}`: Update an existing todo item
  - Request body: `{ "title": "string", "description": "string", "completed": boolean }`
  - Response: `{ "id": "string", "title": "string", "description": "string", "completed": boolean, "createdAt": "string", "updatedAt": "string" }`
- `DELETE /api/items/{itemId}`: Delete a todo item by ID
  - Response: No content (204)

All endpoints except `/api/users/register` and `/api/users/login` require a valid JWT token in the Authorization header:

Authorization: Bearer <your_token_here>

