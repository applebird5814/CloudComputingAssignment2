package com.example.cloudass2.Util;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;

import java.io.IOException;
public class CloudTranslation {

    private String projectId;
    private String targetLanguage;

    public CloudTranslation() {
        this.projectId = "sunny-mender-270008";
        this.targetLanguage = "zh";
    }

    public String translation(String text) throws IOException {
        return translateText(projectId,targetLanguage,text);
    }

    /*
    public static void translateText() throws IOException {
        String projectId = "YOUR-PROJECT-ID";
        // Supported Languages: https://cloud.google.com/translate/docs/languages
        String targetLanguage = "your-target-language";
        String text = "your-text";
        translateText(projectId, targetLanguage, text);
    }*/

    // Translating Text
    public String translateText(String projectId, String targetLanguage, String text)
            throws IOException {

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            // Supported Locations: `global`, [glossary location], or [model location]
            // Glossaries must be hosted in `us-central1`
            // Custom Models must use the same location as your model. (us-central1)
            LocationName parent = LocationName.of(projectId, "global");

            // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(targetLanguage)
                            .addContents(text)
                            .build();

            TranslateTextResponse response = client.translateText(request);

            // Display the translation for each input text provided
            StringBuilder stringBuilder = new StringBuilder();
            for (Translation translation : response.getTranslationsList()) {
                stringBuilder.append(translation.getTranslatedText());
            }
            return stringBuilder.toString();
        }
    }
}

