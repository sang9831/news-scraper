package oop.search.application;

import oop.search.domain.NewsResult;

import java.util.List;

public class NewsService {
    private final NewsProvider newsProvider;
    private final NewsPublisher newsPublisher;

    public NewsService(NewsProvider newsProvider, NewsPublisher newsPublisher){
        this.newsProvider = newsProvider;
        this.newsPublisher = newsPublisher;
    }

    public List<NewsResult> search(String searchQuery, int limit){
        List<NewsResult> results = newsProvider.fetchNews(searchQuery, limit);
        newsPublisher.publish(searchQuery, results);
        return results;
    }

}
