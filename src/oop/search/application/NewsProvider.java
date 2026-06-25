package oop.search.application;
import oop.search.domain.NewsResult;
import java.util.List;

public interface NewsProvider {
    List<NewsResult> fetchNews(String searchQuery, int limit);
}