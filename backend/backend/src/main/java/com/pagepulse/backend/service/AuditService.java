package com.pagepulse.backend.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.pagepulse.backend.dto.AuditResponse;

@Service
public class AuditService {

    public AuditResponse auditWebsite(String websiteUrl) throws IOException {

        validateUrl(websiteUrl);

        long startTime = System.currentTimeMillis();

        Connection connection = Jsoup.connect(websiteUrl)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .ignoreContentType(false);

        Connection.Response response = connection.execute();

        long responseTime = System.currentTimeMillis() - startTime;

        String contentType = response.contentType();

        if (contentType == null || !contentType.toLowerCase().contains("text/html")) {
            throw new IOException("URL does not contain HTML content.");
        }

        Document document = response.parse();

        String title = document.title();

        String metaDescription = document
                .select("meta[name=description]")
                .attr("content");

        int h1Count = document.select("h1").size();

        int missingAltImages = countImagesWithoutAlt(document);

        int wordCount = countWords(document.text());

        return new AuditResponse(
                response.statusCode(),
                responseTime,
                title,
                metaDescription,
                h1Count,
                missingAltImages,
                wordCount
        );
    }

    private void validateUrl(String url) throws MalformedURLException {

        URL parsedUrl = new URL(url);

        String host = parsedUrl.getHost();

        if (host == null || host.isBlank() || !host.contains(".")) {
            throw new MalformedURLException("Please enter a valid website URL.");
        }
    }

    private int countImagesWithoutAlt(Document document) {

        Elements images = document.select("img");

        int count = 0;

        for (var image : images) {

            if (!image.hasAttr("alt") || image.attr("alt").trim().isEmpty()) {
                count++;
            }

        }

        return count;
    }

    private int countWords(String text) {

        if (text == null || text.isBlank()) {
            return 0;
        }

        return text.trim().split("\\s+").length;
    }

}