import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ArticleFetcher {

    public static void retrieveAllArticles() {
        for (Language language : Language.values()) {
            retrieveArticles(language);
        }
    }

    public static void retrieveArticles(Language language) {
        JSONObject articlesData;

        try {
            URL url = new URL("https://api.cognitive.microsoft.com/bing/v5.0/news?mkt=" + language.getId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", Keys.getBingSearchKey());

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);

            StringBuffer response = new StringBuffer();
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
                Article article = ArticleParser.parseArticle(url, language);
                System.out.println(article.title);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}
