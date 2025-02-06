# Spring Boot Todo API

A RESTful API for managing todos built with Spring Boot.

## Features

- CRUD operations for todos
- Pagination and sorting support
- Validation with detailed error responses
- Global exception handling
- Caching implementation
- Comprehensive test coverage

## API Endpoints

### Todos
- `GET /api/todos` - Get all todos (with pagination)
  - Query params: `page`, `size`, `sortBy`
- `GET /api/todos/{id}` - Get a specific todo
- `POST /api/todos` - Create a new todo
- `PUT /api/todos/{id}` - Update an existing todo
- `DELETE /api/todos/{id}` - Delete a todo

## Error Handling

The API provides detailed error responses for:
- Invalid input validation
- Resource not found
- Invalid request format
- Server errors

## Development

### Prerequisites
- Java 17+
- Maven

### Running the Application
```bash
mvn spring-boot:run