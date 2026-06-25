package oop.search.infrastructure;

import oop.search.application.NewsPublisher;
import oop.search.domain.NewsResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class GitHubNewsPublisher extends AbstractHttpClient implements NewsPublisher {
    private final static String GITHUB_API_URL = "https://api.github.com/repos/%s/issues";

    private final String token;

    public GitHubNewsPublisher() {
        super(GITHUB_API_URL.formatted(System.getenv("GITHUB_REPOSITORY")));
        this.token = System.getenv("GITHUB_TOKEN");
    }

    @Override
    public void publish(String topic, List<NewsResult> newsResults) {
//        httpClient
        String url = endpoint;
        String payload = """
                {
                "title": "%s",
                "body": "%s"
                }
                """.formatted("%s (%s)".formatted(topic, ZonedDateTime.now(ZoneId.of("Asia/Seoul"))), newsResults).trim();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .uri(URI.create(url))
//                .header("X-Naver-Client-Id", clientId)
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        try {
            httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
