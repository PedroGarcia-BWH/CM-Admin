package es.uca.cm.admin.openAi.GPT;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.uca.cm.admin.openAi.OpenAiClient;
import org.springframework.stereotype.Controller;

@Controller
public class GPTController {

    private ObjectMapper jsonMapper = new ObjectMapper();

    private OpenAiClient client = new OpenAiClient();

    public String GPT_3(String message) throws Exception {
        var completion = new CompletionRequest(message);
        var postBodyJson = jsonMapper.writeValueAsString(completion);
        System.out.println(postBodyJson);
        var responseBody = client.postToOpenAiApi(postBodyJson);
        /*var completionResponse = jsonMapper.readValue(responseBody, CompletionResponse.class);
        return completionResponse.firstAnswer().orElseThrow();*/
        return responseBody;
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
}


