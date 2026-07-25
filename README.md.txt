# Page Pulse

## Overview

Page Pulse is a simple web application that audits a website and provides a basic SEO and page health report. Users can enter any valid website URL, and the application analyzes the page and displays useful information such as the HTTP status, response time, page title, meta description, H1 count, images missing alt text, and approximate word count.

This project was built as part of the **Digital Heroes Internship Qualification Task**.

---

## Tech Stack

### Backend
- Java 17
- Spring Boot
- Jsoup
- Maven

### Frontend
- React
- Axios
- CSS

---

## Features

- Audit any valid website URL
- Display HTTP status code
- Measure page response time
- Extract page title
- Extract meta description
- Count H1 tags
- Count images without alt text
- Calculate approximate word count
- Handle invalid URLs, non-HTML pages, and request failures with meaningful error messages

---

# API

### Endpoint

```
POST /api/audit
```

### Request

```json
{
  "url": "https://example.com"
}
```

### Success Response

```json
{
  "status": 200,
  "responseTime": 140,
  "title": "Example Domain",
  "metaDescription": "",
  "h1Count": 1,
  "missingAltImages": 0,
  "wordCount": 21
}
```

### Error Response

```json
{
  "error": "Please enter a valid website URL."
}
```

---

# Setup

## Backend

Clone the repository

```
git clone <repository-url>
```

Move to the backend folder

```
cd backend
```

Run the application

```
mvn spring-boot:run
```

The backend runs on:

```
http://localhost:8080
```

---

## Frontend

Move to the frontend folder

```
cd frontend
```

Install dependencies

```
npm install
```

Start the React application

```
npm run dev
```

The frontend runs on:

```
http://localhost:5173
```

---

# Testing

JUnit tests have been added for the service layer.

The tests cover:

- Successful website audit
- Invalid URL handling
- Non-HTML response handling

Run the tests using:

```
mvn test
```

---

# Design Decisions

### 1. Using Jsoup

I used **Jsoup** because it provides a simple way to fetch and parse HTML pages. It made extracting elements like the page title, meta description, H1 tags, and images much easier without writing complex parsing logic.

### 2. Keeping the Business Logic in a Service

Instead of writing all the logic inside the controller, I created a separate `AuditService` class. This keeps the controller focused on handling requests while the service handles the auditing logic, making the code easier to read, maintain, and test.

### 3. Handling Errors Gracefully

I added a global exception handler to return clear error messages for invalid URLs, timeouts, and non-HTML responses. This prevents the application from crashing and provides better feedback to the user.

---

# If I Had More Time

If I had another day to work on this project, I would:

- Add an overall SEO score
- Check for broken links
- Store previous audit reports in a database
- Add loading animations and improve the UI
- Generate downloadable PDF reports

---

# Live Demo

Frontend:

```
(Add deployed frontend URL here)
```

Backend:

```
(Add deployed backend URL here)
```

---

# Author

**Poojitha Singamshetty**

---

### Footer Credit

Built for Digital Heroes Training Task

https://digitalheroesco.com