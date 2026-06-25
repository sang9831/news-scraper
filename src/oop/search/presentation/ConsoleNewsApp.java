package oop.search.presentation;

import jdk.jshell.SourceCodeAnalysis;
import oop.search.application.NewsService;
import oop.search.infrastructure.NaverNewsProvider;

import java.util.Scanner;

public class ConsoleNewsApp {
    private final NewsService newsService;
    public ConsoleNewsApp(NewsService newsService){
        this.newsService = newsService;
    }

    public void run(){
        Scanner sc = new Scanner(System.in);
        while (true){

            System.out.println("[검색할 키워드를 입력해주세요(q 입력 시 종료)]");
            String keyword = sc.nextLine().trim();

            if (keyword.equals("q")){
                System.out.println("[검색을 종료합니다]");
                break;
            }

            System.out.println("[몇 건 검색하시겠습니까? (양의 정수)]");
            int limit = sc.nextInt();
            sc.nextLine();
            newsService.search(keyword, limit);
            System.out.println("[검색이 완료되었습니다]");
        }
    }

    public static void main(String[] args) {
        NewsService newsService = new NewsService(
                new NaverNewsProvider(),
                new ConsoleNewsPublisher());
        ConsoleNewsApp app = new ConsoleNewsApp(newsService);
        app.run();
    }
}
