import okhttp3.*;
import org.json.JSONObject;


import java.io.IOException;


public class EmotionDetector {
    private final OkHttpClient client = new OkHttpClient();


    public String fetchEmotionData(String imageUrl) throws IOException {
        String apiKey = "API Key";
        String apiSecret = "API Secret";
        String url = "https://api-us.faceplusplus.com/facepp/v3/detect";




        RequestBody requestBody = new FormBody.Builder()
                .add("api_key", apiKey)
                .add("api_secret", apiSecret)
                .add("image_url", imageUrl)
                .add("return_attributes", "emotion")
                .build();




        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        // Execute HTTP request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
            return response.body().string();
        }
    }


    public void analyzeEmotion(String jsonResponse) {
        try {
            JSONObject obj = new JSONObject(jsonResponse);


            if (obj.has("faces") && obj.getJSONArray("faces").length() > 0) {
                JSONObject emotions = obj.getJSONArray("faces")
                        .getJSONObject(0)
                        .getJSONObject("attributes")
                        .getJSONObject("emotion");


                double sadness = emotions.optDouble("sadness", 0);
                double happiness = emotions.optDouble("happiness", 0);
                double anger = emotions.optDouble("anger", 0);
                double neutral = emotions.optDouble("neutral", 0);


                System.out.println("\nMindfulness Tip:");
                if (sadness > 50) {
                    System.out.println("🌿 You seem sad. Try some deep breathing or listen to uplifting music. 🌿");
                } else if (happiness > 50) {
                    System.out.println("You're happy! Enjoy the moment and spread positivity. 😊");
                } else if (anger > 50) {
                    System.out.println("Feeling angry? Try meditation or take a break. 🧘‍♂️");
                } else {
                    System.out.println("Stay present and be kind to yourself. 💙");
                }
            } else {
                System.out.println("No face detected in the image.");
            }
        } catch (Exception e) {
            System.err.println("Error analyzing emotion data: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        EmotionDetector detector = new EmotionDetector();
        String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/a/ab/Sad_Woman.jpg"; // Replace with a valid publicly accessible URL


        try {
            String response = detector.fetchEmotionData(imageUrl);
            System.out.println("Emotion Data:\n" + response);
            detector.analyzeEmotion(response);
        } catch (IOException e) {
            System.err.println("Error fetching emotion data: " + e.getMessage());
        }
    }
}
