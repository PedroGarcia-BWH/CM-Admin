package es.uca.cm.admin.openAi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class OpenAiClient {

    //@Value("${openai.api_key}")
    private String openaiApiKey = "";

    private final HttpClient client = HttpClient.newHttpClient();

    private URI uri = URI.create("https://api.openai.com/v1/completions");

    public String postToOpenAiApi(String requestBodyAsJson)
            throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyAsJson)).build();
        System.out.println(request);
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public String postToOpenAiApiImages(String requestBodyAsJson)
            throws IOException, InterruptedException {
        URI uri = URI.create("https://api.openai.com/v1/images/generations");
        var request = HttpRequest.newBuilder().uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyAsJson)).build();
        System.out.println(request);
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}
