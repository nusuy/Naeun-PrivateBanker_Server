package com.naeun.naeun_server.global.util;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class SttUtil {
    public String transcribe(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Required part 'file' is not present.");
        }

        // Decode file to byte array
        byte[] audioBytes = file.getBytes();

        try (SpeechClient speechClient = SpeechClient.create()) {
            ByteString audioData = ByteString.copyFrom(audioBytes);
            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(audioData)
                    .build();

            RecognitionConfig recognitionConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                            .setSampleRateHertz(44100)
                            .setLanguageCode("ko-KR")
                            .build();

            // Speech-to-Text
            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            if (!results.isEmpty()) {
                // Results
                SpeechRecognitionResult result = results.get(0);
                return result.getAlternatives(0).getTranscript();
            } else {
                log.error("No transcription result found");
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
