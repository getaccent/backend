import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        createArticlesTable();

        ArticleFetcher.retrieveArticles(Language.FRENCH);

        port(8000);

        get("/translate", (req, res) -> {
            String term = req.queryParams("term");
            String sourceStr = req.queryParams("source");
            String targetStr = req.queryParams("target");

            Language source = Language.fromISO839_1(sourceStr);
            Language target = Language.fromISO839_1(targetStr);

            return Translator.translateTerm(term, source, target);
        });
    }

    public static void createArticlesTable() {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:main.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS ARTICLES" +
                         "(ID INTEGER PRIMARY KEY    AUTOINCREMENT," +
                         " URL                TEXT   NOT NULL," +
                         " TITLE              TEXT   NOT NULL," +
                         " IMAGEURL           TEXT," +
                         " CONTENT            TEXT," +
                         " AUTHOR             TEXT," +
                         " PUBLISH_DATE       TEXT," +
                         " LANGUAGE           TEXT   NOT NULL)";

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
