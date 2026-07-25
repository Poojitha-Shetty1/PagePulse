import { useState } from "react";
import axios from "axios";
import "./App.css";

function statusTone(status) {
  if (status >= 200 && status < 300) return "good";
  if (status >= 300 && status < 400) return "warn";
  return "bad";
}

function App() {
  const [url, setUrl] = useState("");
  const [report, setReport] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const auditWebsite = async () => {
    setError("");
    setReport(null);

    if (!url.trim()) {
      setError("Please enter a URL.");
      return;
    }

    try {
      const parsedUrl = new URL(url);
      if (!parsedUrl.hostname.includes(".")) {
        setError("Please enter a valid website URL (e.g. https://example.com)");
        return;
      }
    } catch {
      setError("Please enter a valid website URL (e.g. https://example.com)");
      return;
    }

    setLoading(true);

    try {
      const response = await axios.post("https://pagepulse-gctb.onrender.com/api/audit", { url });
      setReport(response.data);
    } catch (err) {
      setError(err.response?.data?.error || "Something went wrong.");
    } finally {
      setLoading(false);
    }
};

  const onKeyDown = (e) => {
    if (e.key === "Enter") auditWebsite();
  };

  const rows = report
    ? [
        { label: "Status", value: report.status, tone: statusTone(report.status) },
        { label: "Response Time", value: `${report.responseTime} ms` },
        { label: "Title", value: report.title || "N/A" },
        { label: "Meta Description", value: report.metaDescription || "N/A" },
        { label: "H1 Count", value: report.h1Count },
        { label: "Missing Alt Images", value: report.missingAltImages },
        { label: "Word Count", value: report.wordCount },
      ]
    : [];

  return (
    <div className="container">
      <h1>Page Pulse</h1>
      <p className="subtitle">Quick SEO & health check for any webpage</p>

      <div className="input-row">
        <input
          type="text"
          placeholder="https://example.com"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          onKeyDown={onKeyDown}
          disabled={loading}
        />
        <button onClick={auditWebsite} disabled={loading}>
          {loading && <span className="spinner" />}
          {loading ? "Auditing..." : "Audit Website"}
        </button>
      </div>

      {error && <p className="error">⚠ {error}</p>}

      {report && (
        <div className="card">
          <h2>Audit Report</h2>

          {rows.map((r) => (
            <div className="row" key={r.label}>
              <span>{r.label}</span>
              <span className={r.tone ? `badge tone-${r.tone}` : ""}>{r.value}</span>
            </div>
          ))}
        </div>
      )}

      <footer>
        Built for Digital Heroes Training Task |{" "}
        <a href="https://digitalheroesco.com" target="_blank" rel="noreferrer">
          Digital Heroes
        </a>
      </footer>
    </div>
  );
}

export default App;