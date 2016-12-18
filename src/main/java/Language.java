public enum Language {
    CHINESE_SIMPLIFIED("zh-CN"),
    CHINESE_TRADITIONAL("zh-TW"),
    ENGLISH("en-US"),
    FRENCH("fr-FR"),
    GERMAN("de-DE"),
    ITALIAN("it-IT"),
    JAPANESE("ja-JP"),
    KOREAN("ko-KR"),
    SPANISH("es-ES"),
    SWEDISH("sv-SE"),
    RUSSIAN("ru-RU");

    private String id;

    Language(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
