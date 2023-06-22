package es.uca.cm.admin.openAi.GPT;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.uca.cm.admin.openAi.OpenAiClient;
import org.springframework.stereotype.Controller;

@Controller
public class GPTController {

    public ObjectMapper jsonMapper = new ObjectMapper();

    public OpenAiClient client = new OpenAiClient();

    public String GPT_3(String message) throws Exception {
        var completion = new CompletionRequest("Creame un articulo con titulo, descripcion y cuerpo de articulo de la siguiente temática: " +message);
        var postBodyJson = jsonMapper.writeValueAsString(completion);
        System.out.println(postBodyJson);
        var responseBody = client.postToOpenAiApi(postBodyJson);
        //System.out.println(responseBody);
        ObjectMapper objectMapper = new ObjectMapper();


        // Leer el JSON y mapearlo a una clase POJO
        MyPojo myPojo = objectMapper.readValue(responseBody, MyPojo.class);

        // Obtener el valor de la clave "text" como variable String
        String text = myPojo.getChoices()[0].getText();

        // Obtener el valor del campo "text"
        //String text = response_json.get("text").asText();

        return text;
    }


    public class CompletionRequest {

        private final String model = "text-davinci-003";

        private String prompt;

        private final double temperature = 0.7;

        private final int max_tokens = 3000;

        public CompletionRequest(String  prompt) {
            this.prompt = prompt;
        }

        //setter and getter
        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        public String getModel() {
            return model;
        }

        public double getTemperature() {
            return temperature;
        }

        public int getMax_tokens() {
            return max_tokens;
        }




    }


   /* public record CompletionResponse(Usage usage, List<Choice> choices) {

        public Optional<String> firstAnswer() {
            if (choices == null || choices.isEmpty())
                return Optional.empty();
            return Optional.of(choices.get(0).text);
        }

        record Usage(int total_tokens, int prompt_tokens, int completion_tokens) {}

        record Choice(String text) {}
    }*/

    @JsonIgnoreProperties(ignoreUnknown = true)
    // Clase POJO para mapear el JSON
    public static class MyPojo {
        private String id;
        private String object;
        private long created;
        private String model;
        private Choice[] choices;
        private Usage usage;

        // Getters y Setters
        // ...
        public Choice[] getChoices() {
            return choices;
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        static class Choice {
            private String text;
            private int index;
            private Object logprobs;
            private String finish_reason;

            // Getters y Setters
            // ...

            public String getText() {
                return text;
            }
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        static class Usage {
            private int prompt_tokens;
            private int completion_tokens;
            private int total_tokens;

            // Getters y Setters
            // ...
        }
    }
}


