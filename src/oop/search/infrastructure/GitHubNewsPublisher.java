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
////        try {
////            httpClient.send(
////                    request,
////                    HttpResponse.BodyHandlers.ofString()
////            );
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
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
    String url = endpoint; //

    // 1. 뉴스 리스트를 가독성 좋은 일반 텍스트 문자열로 먼저 조립합니다.
    StringBuilder bodyBuilder = new StringBuilder();
    bodyBuilder.append("## 수집된 뉴스 목록\\n\\n");
    for (NewsResult result : newsResults) {
        bodyBuilder.append("### ").append(result.getTitle()).append("\\n");
        bodyBuilder.append("- **요약:** ").append(result.getDescription()).append("\\n");
        bodyBuilder.append("- **링크:** ").append(result.getLink()).append("\\n");
        bodyBuilder.append("- **게시일:** ").append(result.getPubDate()).append("\\n\\n");
    }

    // 2. 조립된 텍스트에서 JSON 문법을 깨뜨리는 특수문자들을 안전하게 이스케이프 처리합니다.
    String safeBody = bodyBuilder.toString()
            .replace("\\", "\\\\")    // 역슬래시 치환
            .replace("\"", "\\\"")    // 쌍따옴표 치환
            .replace("\r", "")        // 캐리지 리턴 제거
            .replace("\n", "\\n");    // 실제 줄바꿈을 JSON용 \n 문자로 변환

    // 3. 제목(Topic)도 특수문자가 있을 수 있으므로 함께 처리합니다.
    String titleText = "%s (%s)".formatted(topic, ZonedDateTime.now(ZoneId.of("Asia/Seoul"))); //
    String safeTitle = titleText.replace("\"", "\\\"");

    // 4. 깨지지 않는 안전한 JSON 페이로드를 생성합니다.
    String payload = """
            {
            "title": "%s",
            "body": "%s"
            }
            """.formatted(safeTitle, safeBody).trim(); //

    HttpRequest request = HttpRequest.newBuilder() //
            .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8)) //
            .uri(URI.create(url)) //
            .header("Authorization", "Bearer " + token) //
            .header("Accept", "application/vnd.github+json") //
            .header("X-GitHub-Api-Version", "2022-11-28") //
            .header("Content-Type", "application/json; charset=UTF-8") //
            .build(); //

    try {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // 201 Created가 아니면 에러를 내뿜고 종료하게 유지
        if (response.statusCode() != 201) {
            System.err.println("GitHub API 오류 발생! 상태코드: " + response.statusCode());
            System.err.println("응답 본문: " + response.body());
            System.exit(1);
        } else {
            System.out.println("🎉 성공적으로 GitHub Issue가 생성되었습니다!");
        }
    } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
    }
}
}
