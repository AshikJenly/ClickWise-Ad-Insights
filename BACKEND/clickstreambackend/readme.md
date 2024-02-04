
```markdown
# REST API Documentation

This documentation provides details on the REST API endpoints and their usage for the Clickstream Application.

## Table of Contents

- [Authentication](#authentication)
  - [Register](#register)
  - [Login](#login)
  - [Logout](#logout)

- [Advertisement](#advertisement)
  - [Get All Ads](#get-all-ads)

- [MongoDB Data](#mongodb-data)
  - [Get All Documents](#get-all-documents)
  - [Aggregate Group By Window](#aggregate-group-by-window)

- [User Activity](#user-activity)
  - [Post User Activity](#post-user-activity)

## Authentication

### Register

- **Endpoint:** `POST /api/authentication/reg`
- **Description:** Register a new user.
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
  }
```

- **Response:**
  - 200 OK: User registered successfully.
  - 409 Conflict: User already exists.

### Login

- **Endpoint:** `POST /api/authentication/login`
- **Description:** Log in an existing user.
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Response:**
  - 200 OK: Login successful, session created.
  - 404 Not Found: User not found.
  - 200 OK with error message: Wrong password.

### Logout

- **Endpoint:** `POST /api/authentication/logout`
- **Description:** Log out the currently logged-in user.
- **Response:** 200 OK

## Advertisement

### Get All Ads

- **Endpoint:** `GET /ads/getallads`
- **Description:** Retrieve all advertisements.
- **Response:** Array of ad objects.

## MongoDB Data

### Get All Documents

- **Endpoint:** `GET /api/datas/mongo/all`
- **Description:** Retrieve all documents from MongoDB.
- **Response:** Array of documents.

### Aggregate Group By Window

- **Endpoint:** `GET /api/datas/mongo/agg`
- **Description:** Aggregate data grouped by the window.
- **Response:** Aggregated data.

## User Activity

### Post User Activity

- **Endpoint:** `POST /produce/useractivity`
- **Description:** Post user activity data.
- **Request Body:**
  ```json
  {
    "userId": "user123",
    "activityType": "click",
  }
  ```
- **Response:** 200 OK


