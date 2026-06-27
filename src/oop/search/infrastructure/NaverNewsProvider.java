package oop.search.infrastructure;

import oop.search.application.NewsProvider;
import oop.search.domain.NewsCategory;
import oop.search.domain.NewsResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NaverNewsProvider extends AbstractHttpClient implements NewsProvider{
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
        String url = endpoint + "?query="
                + URLEncoder.encode(searchQuery, StandardCharsets.UTF_8)
                + "&display=" + limit
                + "&sort="
                + category.getQueryValue()
                + "&start=1";

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .build();

        List<NewsResult> results = new ArrayList<>();

        try{
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
//            System.out.println("body = " + body);

            String items = body.split("items")[1];
//            System.out.println("items = " + items);

            String[] itemArray = items.split("},");

            for (String item : itemArray) {
                System.out.println("item = " + item);
//                String title=item.split("\"title\":")[1].split("\",")[0];
                String title=cutText(item, "\"title\":", "\",");
                String link =cutText(item, "\"link\":\"", "\",");
                String description=cutText(item, "\"description\":\"", "\",");
                String pubDate = cutText(item, "\"pubDate\":\"", "\"");

                link = link.replace("\\/", "/");

                Document doc = Jsoup.connect(link).get();

                String imageUrl = doc.select("meta[property=og:image]")
                        .attr("content");

                System.out.println(imageUrl);

                NewsResult result = new NewsResult(title, description, link, pubDate, imageUrl);
                results.add(result);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return results;
    }

    public String cutText(String original, String prefix, String suffix){
        return original.split(prefix)[1].split(suffix)[0];
    }

    public static void main(String[] args) {
        NewsProvider provider = new NaverNewsProvider();
        List<NewsResult> results = provider.fetchNews("앨리엇 앤더슨", 10);
        for (NewsResult newsItem : results) {
            System.out.println("newsItem = " + newsItem);
        }
    }
}
