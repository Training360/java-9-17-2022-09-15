package training;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class IndexMain {

    public static void main(String[] args) throws Exception {
        var request = HttpRequest.newBuilder().uri(new URI("https://index.hu"))
                .build();
        var client = HttpClient.newBuilder().build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofLines());

        long start = System.currentTimeMillis();

        response
                .body()
                .flatMap(IndexMain::getLinks)
                .filter(IndexMain::startsWithHttp)
                .distinct()
                .limit(100)
                .filter(IndexMain::statusNotOk)
                .parallel()
                .forEach(System.out::println);

        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start));
    }

    private static boolean statusNotOk(String link) {
        try {
            var request = HttpRequest.newBuilder().uri(new URI(link))
                    .build();
            var client = HttpClient.newBuilder().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofLines());
            return response.statusCode() != 200;
        }
        catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private static Stream<String> getLinks(String line) {
        var pattern = Pattern.compile("href=\"([^\"]*)\"");
        var matcher = pattern.matcher(line);
        return matcher.results().map(r -> r.group(1));
    }

    private static boolean startsWithHttp(String link) {
        return link.startsWith("https://");
    }
}
