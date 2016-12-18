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
     * The article's text in its original language.
     */
    public String text;

    /**
     * A list of the people who wrote the article.
     */
    public String[] authors;

    /**
     * The date the article was published.
     */
    public Date publishedDate;

    /**
     * The language this article was originally written in.
     */
    public Language language;
}
