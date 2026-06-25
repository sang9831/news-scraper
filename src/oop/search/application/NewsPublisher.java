package oop.search.application;

import oop.search.domain.NewsResult;

import java.util.List;

public interface NewsPublisher {
    void publish(String topic, List<NewsResult> newsResults);
}
