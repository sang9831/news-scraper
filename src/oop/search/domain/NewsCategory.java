package oop.search.domain;

public enum NewsCategory {
    SIM("sim", "정확도순"), DATE("date", "최신순");

    private final String queryValue;
    private final String description;

    NewsCategory(String queryValue, String description){
        this.queryValue = queryValue;
        this.description = description;
    }

    public static void useCategory(NewsCategory category){
        System.out.println(category);
    }

    public static void main(String[] args) {
        System.out.println(NewsCategory.SIM);
        System.out.println(NewsCategory.DATE);
        NewsCategory nc = NewsCategory.SIM;
        System.out.println("nc.getQueryValue() = " + nc.getQueryValue());
        System.out.println("nc.getDescription() = " + nc.getDescription());
        useCategory(NewsCategory.SIM);
    }

    public String getQueryValue() {
        return queryValue;
    }

    public String getDescription() {
        return description;
    }
}
