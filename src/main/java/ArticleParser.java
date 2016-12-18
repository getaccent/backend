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

public class ArticleParser {

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
        String author = articleData.getString("author");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        Date publishedDate = new Date();

        try {
            publishedDate = format.parse(articleData.getString("date_published"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Article(articleURL, title, imageURL, content, author, publishedDate, language);
    }
}
