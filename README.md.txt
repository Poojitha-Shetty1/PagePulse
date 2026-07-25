# Page Pulse

A web tool that audits any URL and returns a report on HTTP status, response time, SEO basics (title, meta description), and content structure (H1 count, missing alt text on images, word count).

**Live app**: https://page-pulse-jade.vercel.app
**Backend API**: https://pagepulse-gctb.onrender.com

Built for the Digital Heroes SDE training task.

---

## Tech Stack

- **Backend**: Java, Spring Boot, Jsoup (HTML fetching/parsing)
- **Frontend**: React, Axios
- **Deployment**: Render (backend, via Docker), Vercel (frontend)

---

## Setup

### Backend
```bash
cd backend/backend
./mvnw spring-boot:run
```
Runs on `http://localhost:8080`

### Frontend
```bash
cd Frontend_Assign_DH/frontend
npm install
npm run dev
```
Runs on `http://localhost:5173` (Vite) — update the axios URL in `App.jsx` to point at your local backend if testing locally.

---

## API Contract

### `POST /api/audit`

**Request**
```json
{ "url": "https://example.com" }
```

**Success response — 200**
```json
{
  "status": 200,
  "responseTime": 342,
  "title": "Example Domain",
  "metaDescription": "N/A",
  "h1Count": 1,
  "missingAltImages": 0,
  "wordCount": 28
}
```

**Error responses**

| Scenario | Status | Body |
|---|---|---|
| Blank/missing URL | 400 | `{ "error": "URL is required" }` |
| Malformed URL | 400 | `{ "error": "Invalid URL" }` |
| Non-HTML response (e.g. PDF, image) | 400 | `{ "error": "URL does not contain HTML content." }` |
| Request timeout | 408 | `{ "error": "Request timed out" }` |
| Target site blocks automated requests (403) | 403 | `{ "error": "Access denied. This website blocks automated requests." }` |
| Target site not found (404) | 404 | `{ "error": "Website not found." }` |
| Anything unexpected | 500 | `{ "error": "Something went wrong" }` |

---

## Design Decisions

**1. Used Jsoup instead of a separate HTTP client + manual HTML parsing**
Jsoup fetches the page and parses the HTML in one step, and it doesn't break on messy real-world pages with unclosed tags or bad formatting. Since this tool audits random public URLs — not just clean, well-built pages — I needed something forgiving rather than strict.

**2. Used one global exception handler instead of try/catch everywhere**
Instead of writing try/catch blocks in the controller for every possible failure, I put all the error-handling logic in one place (`GlobalExceptionHandler`). So timeouts, invalid URLs, blocked requests, etc. all get turned into clear, consistent error messages from one spot, and the controller itself stays simple and focused on the actual audit logic.

**3. Used separate request/response classes (DTOs) instead of raw data**
Instead of passing around loose maps or strings, I made proper `AuditRequest` and `AuditResponse` classes. This makes it obvious what data the API expects and returns, just by looking at the class — and it let me validate the input (like rejecting a blank URL) automatically, before the code even tries to process it.

---

## Tests

Located in `src/test/java/...`. Cover:
- **Happy path**: valid HTML page returns a correctly populated report (status, title, word count, etc.)
- **Failure case 1**: invalid/malformed URL returns a 400 with a clear error message
- **Failure case 2**: non-HTML response (e.g. a PDF or image URL) is rejected with a sensible error instead of attempting to parse it as HTML

---

## What I'd change with another day

- **Add a database layer to cache audit results.** Right now, every audit re-fetches and re-parses the page from scratch, even if the same URL was just checked seconds ago. I'd store past audit results (URL, timestamp, and the report) in a database like  MySQL, keyed by URL. 

- **Refine word count to reflect actual content, not boilerplate.** Currently `wordCount` counts every visible word on the page, including navigation menus, footers, and sidebars .I'd target the main content area specifically (e.g. by looking for a `<main>` or `<article>` tag) so the metric better reflects the actual page content rather than the site's template.
