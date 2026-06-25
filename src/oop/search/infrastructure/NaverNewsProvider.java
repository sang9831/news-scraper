package oop.search.infrastructure;

import oop.search.application.NewsProvider;
import oop.search.domain.NewsCategory;
import oop.search.domain.NewsResult;

import java.util.List;

public class NaverNewsProvider extends AbstractHttpScraper {
    private static final String NEWS_API_URL = "https://openapi.naver.com/v1/search/news.json";
    private final String clientId;
    private final String clientSecret;
    private final NewsCategory category;

    public NaverNewsProvider(){
        super(NEWS_API_URL);
        this.clientId = System.getenv("NAVER_CLIENT_ID");
        this.clientSecret = System.getenv("NAVER_CLIENT_SECRET");
        this.category = NewsCategory.valueOf(System.getenv("NEWS_CATEGORY"));

        System.out.println("clientId = " + clientId.substring(0,3)+"...");
        System.out.println("clientSecret = " + clientSecret.substring(0,3)+"...");
        System.out.println("category = " + category);
    }

    @Override
    public List<NewsResult> fetchNews(String searchQuery, int limit) {
        return List.of();
    }

    public static void main(String[] args) {
        NewsProvider provider = new NaverNewsProvider();
        List<NewsResult> results = provider.fetchNews("축구", 10);
        System.out.println("results = " + results);
    }
}
