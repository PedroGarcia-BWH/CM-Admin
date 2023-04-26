package es.uca.cm.admin.openAi.DALL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.uca.cm.admin.openAi.OpenAiClient;
import org.springframework.stereotype.Controller;

@Controller
public class DALLEController {

    private ObjectMapper jsonMapper = new ObjectMapper();

    private OpenAiClient client = new OpenAiClient();

    public String DALLE(String message) throws Exception {
        var completion = new CompletionRequest( message);
        var postBodyJson = jsonMapper.writeValueAsString(completion);
        System.out.println(postBodyJson);
        var responseBody = client.postToOpenAiApiImages(postBodyJson);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        // Obtener los valores de "created" y "url"
        long created = jsonNode.get("created").asLong();
        String url = jsonNode.get("data").get(0).get("url").asText();


// Obtener la URL del primer elemento en el array "data"
        //System.out.println(url);
        return url;
    }


    public class CompletionRequest {



        private String prompt;

        public CompletionRequest(String prompt) {
            this.prompt = prompt;
        }

        //setter and getter
        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        //DATOS OPCIONALES
        /*n
        integer
        Optional
        Defaults to 1
        The number of images to generate. Must be between 1 and 10.

        size
        string
        Optional
        Defaults to 1024x1024
        The size of the generated images. Must be one of 256x256, 512x512, or 1024x1024.

        response_format
        string
        Optional
        Defaults to url
        The format in which the generated images are returned. Must be one of url or b64_json.

        user
        string
        Optional*/

    }

}