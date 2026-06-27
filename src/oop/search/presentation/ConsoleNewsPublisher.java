package oop.search.presentation;

import oop.search.application.NewsPublisher;
import oop.search.domain.NewsResult;

import java.util.List;

public class ConsoleNewsPublisher implements NewsPublisher {
    @Override
    public void publish(String topic, List<NewsResult> newsResults) {
        System.out.println("News topic: %s".formatted(topic));
        for (NewsResult newsResult : newsResults) {
            System.out.println("=".repeat(16));
            String output = """
                    Title : %s
                    Description : %s
                    Link : %s
                    Publish date : %s
                    Image URL : %s
                    """.formatted(
                    newsResult.title(),
                    newsResult.description(),
                    newsResult.url(),
                    newsResult.pubDate(),
                    newsResult.imageUrl()
            ).trim();
            System.out.println(output);
        }
    }

    public static void main(String[] args) {
        NewsPublisher cnp = new ConsoleNewsPublisher();
        cnp.publish("Man City", List.of(new NewsResult(
                "News title",
                "News description",
                "https://naver.com",
                "2026.12.32",
                "imageUrl"
        )));
    }
}
