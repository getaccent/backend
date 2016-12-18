import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Keys {

    private static Map<String, String> keys() {
        Map<String, String> keys = new HashMap<>();

        try {
            String keysFilePath = "./src/main/resources/keys.txt";
            BufferedReader reader = new BufferedReader(new FileReader(keysFilePath));
            String line = reader.readLine();

            while (line != null) {
                String[] data = line.split("=");
                keys.put(data[0], data[1]);

                line = reader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keys;
    }

    public static String bingSearchKey() {
        return keys().get("bing_search");
    }

    public static String googleTranslateKey() {
        return keys().get("google_translate");
    }
}
