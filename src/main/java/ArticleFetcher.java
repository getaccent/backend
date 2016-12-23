import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleFetcher {

    public static void retrieveAllArticles() {
        for (Language language : Language.values()) {
            retrieveArticles(language);
        }
    }

    public static void retrieveArticles(Language language) {
        JSONObject articlesData;

        try {
            URL url = new URL("https://api.cognitive.microsoft.com/bing/v5.0/news?mkt=" + language.getMarketCode());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", Keys.getBingSearchKey());

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            articlesData = new JSONObject(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        JSONArray retrievedArticles = articlesData.getJSONArray("value");

        for (int i = 0; i < retrievedArticles.length(); i++) {
            JSONObject retrievedArticle = retrievedArticles.getJSONObject(i);
            String urlString = retrievedArticle.getString("url");

            try {
                URL url = new URL(urlString);
                Article article = lookupArticle(url);

                if (article == null) {
                    article = parseArticle(url, language);
                    storeArticle(article);
                }

                System.out.println(article.title);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Article parseArticle(URL articleURL, Language language) {
        JSONObject articleData;

        try {
            String encodedArticleURL = URLEncoder.encode(articleURL.toString(), "UTF-8");

            URL url = new URL("http://mercury.postlight.com/parser?url=" + encodedArticleURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("x-api-key", Keys.getMercuryKey());

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            articleData = new JSONObject(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String title = articleData.getString("title");
        URL imageURL = null;

        try {
            imageURL = new URL(articleData.getString("lead_image_url"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String content = articleData.getString("content");
        String author = null;

        if (!articleData.isNull("author")) {
            author = articleData.getString("author");
        }

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date publishedDate = null;

        if (!articleData.isNull("date_published")) {
            try {
                publishedDate = format.parse(articleData.getString("date_published"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return new Article(articleURL, title, imageURL, content, author, publishedDate, language);
    }

    public static Article lookupArticle(URL url) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:main.db");
            c.setAutoCommit(false);

            System.out.println("SELECT * FROM ARTICLES WHERE URL = '" + url.toString() + "';");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ARTICLES WHERE URL = '" + url.toString() + "';");

            while (rs.next()) {
                int id = rs.getInt("ID");
                System.out.println(id);
            }

            System.out.println("Done getting stuff");

            rs.close();
            stmt.close();
            c.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void storeArticle(Article article) {
        Connection c = null;
        Statement stmt = null;

        String sql = null; // up here for easy debugging

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:main.db");

            List<String> keys = new ArrayList<>();
            List<String> values = new ArrayList<>();

            keys.add("URL");
            values.add("'" + article.url.toString() + "'");

            keys.add("TITLE");
            values.add("'" + article.title.replace("'", "''") + "'");

            if (article.imageURL != null) {
                keys.add("IMAGEURL");
                values.add("'" + article.imageURL.toString() + "'");
            }

            if (article.content != null) {
                keys.add("CONTENT");
                values.add("'" + article.content.replace("'", "''") + "'");
            }

            if (article.author != null) {
                keys.add("AUTHOR");
                values.add("'" + article.author + "'");
            }

            if (article.publishedDate != null) {
                keys.add("PUBLISH_DATE");
                values.add("'" + article.publishedDate.toString() + "'");
            }

            keys.add("LANGUAGE");
            values.add("'" + article.language.getISO839_1() + "'");

            String keysString = "";

            for (String key : keys) {
                keysString += key + ", ";
            }

            keysString = keysString.substring(0, keysString.length() - 2);

            String valuesString = "";

            for (String value : values) {
                valuesString += value + ", ";
            }

            valuesString = valuesString.substring(0, valuesString.length() - 2);

            stmt = c.createStatement();
            sql = "INSERT INTO ARTICLES (" + keysString + ") " +
                         "VALUES (" + valuesString + ");";

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Attempted statement: " + sql);
        }
    }
}
