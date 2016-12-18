import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
//        URL url;
//
//        try {
//            url = new URL("http://www.lefigaro.fr/international/2016/12/17/01003-20161217ARTFIG00062-environ-40000-syriens-attendent-la-reprise-des-evacuations-d-alep.php");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        ArticleParser.parseArticle(url, Language.FRENCH);

        ArticleFetcher.retrieveArticles(Language.FRENCH);
    }
}
