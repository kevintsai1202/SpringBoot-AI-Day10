package com.example.ai.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.AudioResponseFormat;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.Voice;
import org.springframework.ai.openai.audio.speech.SpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class AiController {
	private final SpeechModel speechModel;
	@GetMapping("/tts")
    public ResponseEntity<byte[]> speech(String prompt) throws IOException {
		OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
			    .withModel("tts-1-hd")
			    .withVoice(Voice.ALLOY)
			    .withResponseFormat(AudioResponseFormat.MP3)
			    .withSpeed(1f)
			    .build();

			SpeechPrompt speechPrompt = new SpeechPrompt(prompt, speechOptions);
			SpeechResponse response = speechModel.call(speechPrompt);
        return ResponseEntity.ok()
        		.header(HttpHeaders.CONTENT_DISPOSITION, 
        				ContentDisposition
        					.attachment()
        					.filename("配音.mp3", StandardCharsets.UTF_8)
        					.build().toString())
        		.body(response.getResult().getOutput());
    }
}
