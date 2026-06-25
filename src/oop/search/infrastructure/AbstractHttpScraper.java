package oop.search.infrastructure;

import oop.search.application.NewsProvider;

import java.net.http.HttpClient;

public abstract class AbstractHttpScraper implements NewsProvider {
    protected final HttpClient httpClient = HttpClient.newHttpClient();

    private final String endpoint;

    protected AbstractHttpScraper(String endpoint){
        this.endpoint = endpoint;
    }
}
