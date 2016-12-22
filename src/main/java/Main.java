import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
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
}
