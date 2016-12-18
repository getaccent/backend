import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ArticleFetcher {

//    public static void retrieveAllArticles() {
//
//    }

    public static void retrieveArticles(Language language) {
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

            System.out.println(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
