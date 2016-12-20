public enum Language {
    CHINESE_SIMPLIFIED("zh-CN", "zh-CN"),
    CHINESE_TRADITIONAL("zh-TW", "zh-TW"),
    ENGLISH("en-US", "en"),
    FRENCH("fr-FR", "fr"),
    GERMAN("de-DE", "de"),
    ITALIAN("it-IT", "it"),
    JAPANESE("ja-JP", "ja"),
    KOREAN("ko-KR", "ko"),
    SPANISH("es-ES", "es"),
    SWEDISH("sv-SE", "sv"),
    RUSSIAN("ru-RU", "ru");

    private String code;
    private String iso839_1;

    Language(String code, String iso839_1) {
        this.code = code;
        this.iso839_1 = iso839_1;
    }

    public static Language fromISO839_1(String iso839_1) {
        switch (iso839_1) {
            case "zh-CN":
                return CHINESE_SIMPLIFIED;
            case "zh-TW":
                return CHINESE_TRADITIONAL;
            case "en":
                return ENGLISH;
            case "fr":
                return FRENCH;
            case "de":
                return GERMAN;
            case "it":
                return ITALIAN;
            case "ja":
                return JAPANESE;
            case "ko":
                return KOREAN;
            case "es":
                return SPANISH;
            case "sv":
                return SWEDISH;
            case "ru":
                return RUSSIAN;
            default:
                return ENGLISH;
        }
    }

    /**
     * Returns the language's market code. To be used with the Bing Search API.
     * @return language's market code
     */
    public String getMarketCode() {
        return code;
    }

    /**
     * Returns the language's ISO 839-1 code. To be used with the Google Translate API.
     * @return language's ISO 839-1 code
     */
    public String getISO839_1() {
        return iso839_1;
    }
}
