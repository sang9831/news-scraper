package oop.search.presentation;

import oop.search.application.NewsService;
import oop.search.infrastructure.GitHubNewsPublisher;
import oop.search.infrastructure.NaverNewsProvider;

import java.util.Scanner;

public class GitHubNewsApp {
    private final NewsService newsService;
    public GitHubNewsApp(NewsService newsService){
        this.newsService = newsService;
    }

    public void run(){
        String keyword = System.getenv("NEWS_QUERY");
        String limit = System.getenv("NEWS_DISPLAY");
        newsService.search(keyword, Integer.parseInt(limit));
    }

    public static void main(String[] args) {
        NewsService newsService = new NewsService(
                new NaverNewsProvider(),
                new GitHubNewsPublisher());
        GitHubNewsApp app = new GitHubNewsApp(newsService);
        app.run();
    }
}
