import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Translator {

    public static String translateTerm(String term, Language source, Language target) {
        JSONObject translationData;

        try {
            URL url = new URL("https://www.googleapis.com/language/translate/v2?key=" + Keys.getGoogleTranslateKey() + "&q=" + term + "&source" + source.getISO839_1() + "&target=" + target.getISO839_1());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            translationData = new JSONObject(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return translationData.getJSONObject("data").getJSONArray("translations").getJSONObject(0).getString("translatedText");
    }
}
