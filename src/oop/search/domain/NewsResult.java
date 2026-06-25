package oop.search.domain;

public record NewsResult(
        String title,
        String description,
        String url,
        String pubDate
) {
    public static void main(String[] args) {
        NewsResult result = new NewsResult("환율 1600원 되나?" ,"현재 1500원 간당간당하고 혹시 달러로 구독중이면 조심하세요", "https://naver.com", "2026.12.32");
        System.out.println("result = " + result);
    }
}
