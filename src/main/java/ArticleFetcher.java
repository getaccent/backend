import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            System.out.println(urlString);

            try {
                URL url = new URL(urlString);
                Article article = parseArticle(url, language);
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
}
