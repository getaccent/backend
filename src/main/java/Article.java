import java.net.URL;
import java.util.Date;

/**
 * Represents an article that is readable through Accent.
 */
public class Article {

    /**
     * A unique identifier for the article.
     */
    public int id;

    /**
     * The URL at which this article was retrieved from.
     */
    public URL url;

    /**
     * The title of the article.
     */
    public String title;

    /**
     * The URL of an image that can be displayed alongside the article.
     */
    public URL imageURL;

    /**
     * The article's content in its original language, in HTML format.
     */
    public String content;

    /**
     * The name of the author of the article.
     */
    public String author;

    /**
     * The date the article was published.
     */
    public Date publishedDate;

    /**
     * The language this article was originally written in.
     */
    public Language language;

    public Article(URL url, String title, URL imageURL, String text, String author, Date publishedDate, Language language) {
        this.url = url;
        this.title = title;
        this.imageURL = imageURL;
        this.content = content;
        this.author = author;
        this.publishedDate = publishedDate;
        this.language = language;
    }
}
