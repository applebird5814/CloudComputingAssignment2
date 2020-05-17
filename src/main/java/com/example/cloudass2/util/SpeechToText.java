package com.example.cloudass2.util;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.util.List;
public class SpeechToText {

    public SpeechToText() {
    }

    /** Demonstrates using the Speech API to transcribe an audio file. */

    public String transferBytesIntoTextString(byte[] stream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (SpeechClient speechClient = SpeechClient.create()) {
            byte[] data = stream;
            System.out.println("data length "+data.length);
            ByteString audioBytes = ByteString.copyFrom(data);

            // Builds the sync recognize request
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(AudioEncoding.OGG_OPUS)
                            .setSampleRateHertz(8000)
                            .setLanguageCode("en-US")
                            .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            System.out.println("result size "+results.size());
            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.printf("Transcription: %s%n", alternative.getTranscript());
                stringBuilder.append(alternative.getTranscript());
            }
        }
        return stringBuilder.toString();
    }

}
