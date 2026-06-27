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

//    @Override
//    public void publish(String topic, List<NewsResult> newsResults) {
////        httpClient
//        String url = endpoint;
//        String payload = """
//                {
//                "title": "%s",
//                "body": "%s"
//                }
//                """.formatted(
//                // %s -> topic. %s -> 한국기준 현재 시간
//                "%s (%s)".formatted(topic, ZonedDateTime.now(ZoneId.of("Asia/Seoul"))),
//                newsResults
//        ).trim();
//        HttpRequest request = HttpRequest.newBuilder()
//                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
//                .uri(URI.create(url))
////                .header("X-Naver-Client-Id", clientId)
//                .header("Authorization", "Bearer " + token)
//                .header("Accept", "application/vnd.github+json")
//                .header("X-GitHub-Api-Version", "2022-11-28")
//                .header("Content-Type", "application/json; charset=UTF-8")
//                .build();
//

    /// /        try {
    /// /            httpClient.send(
    /// /                    request,
    /// /                    HttpResponse.BodyHandlers.ofString()
    /// /            );
    /// /
    /// /        } catch (Exception e) {
    /// /            e.printStackTrace();
    /// /        }
//        try {
//            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//            // 응답 코드가 201(Created)이 아니면 로그를 찍어보도록 개선
//            if (response.statusCode() != 201) {
//                System.err.println("GitHub API 오류 발생! 상태코드: " + response.statusCode());
//                System.err.println("응답 본문: " + response.body());
//                System.exit(1); // 🔴 에러 발생 시 Action이 실패하도록 종료 코드 1 반환
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }
    @Override
    public void publish(String topic, List<NewsResult> newsResults) {
        String url = endpoint;

        StringBuilder sb = new StringBuilder();
        sb.append("<strong>%s</strong>(와)과 관련된 최신 뉴스입니다<br><br>".formatted(topic));
        for (NewsResult newsResult : newsResults) {
            sb.append("[%s]".formatted(newsResult.title()));
            sb.append("(%s) <br> `%s` <br>".formatted(
                    newsResult.url(),
                    newsResult.pubDate()));
            sb.append("<blockquote>%s</blockquote>".formatted(newsResult.description()));
        }
        String body = sb.toString();
        body = body
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
//        System.out.println("body = " + body);

        String payload = """
                {
                "title": "%s",
                "body": "%s"
                }
                """.formatted(
                // %s -> topic. %s -> 한국기준 현재 시간
                "%s (%s)".formatted(topic, ZonedDateTime.now(ZoneId.of("Asia/Seoul"))),
//                newsResults
                body
        ).trim();

        System.out.println("payload = " + payload);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201) {
                System.err.println("GitHub API 오류 발생! 상태코드: " + response.statusCode());
                System.err.println("응답 본문: " + response.body());
                System.exit(1);
            } else {
                System.out.println("🎉 성공적으로 GitHub Issue가 생성되었습니다!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
