package com.peko.houshoukaizokudan.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peko.houshoukaizokudan.config.GoogleOAuth2Config;
import com.peko.houshoukaizokudan.service.MemberService;

import jakarta.servlet.http.HttpServletResponse;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@RestController
public class GoogleOAuth2NativeHttpController {
	@Autowired
	private GoogleOAuth2Config googleOauth2Config;

	@Autowired
	private MemberService memberService;

	private final String scope = "https://www.googleapis.com/auth/userinfo.email";

	@GetMapping("/public/api/google-login")
	public String googleLogin(HttpServletResponse response) {
		// 直接返回包含认证URL的JSON数据
		String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
				"client_id=" + googleOauth2Config.getClientId() +
				"&response_type=code" +
				"&scope=openid%20email%20profile" +
				"&redirect_uri=" + googleOauth2Config.getRedirectUri() +
				"&state=state";
		return "{\"authUrl\": \"" + authUrl + "\"}";
	}

	@GetMapping("/public/api/google-callback")
	public String oauth2Callback(@RequestParam(required = false) String code) throws IOException {
		if (code == null) {
			String authUri = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code" +
					"&client_id=" + googleOauth2Config.getClientId() +
					"&redirect_uri=" + googleOauth2Config.getRedirectUri() +
					"&scope=" + scope;
			return "{\"authUri\": \"" + authUri + "\"}";
		} else {
			OkHttpClient client = new OkHttpClient();
			RequestBody requestBody = new FormBody.Builder()
					.add("code", code)
					.add("client_id", googleOauth2Config.getClientId())
					.add("client_secret", googleOauth2Config.getClientSecret())
					.add("redirect_uri", googleOauth2Config.getRedirectUri())
					.add("grant_type", "authorization_code")
					.build();

			Request request = new Request.Builder()
					.url("https://oauth2.googleapis.com/token")
					.post(requestBody)
					.build();

			try (Response response = client.newCall(request).execute()) {
				String credentials = response.body().string();

				JsonNode jsonNode = new ObjectMapper().readTree(credentials);
				String accessToken = jsonNode.get("access_token").asText();
				String idToken = jsonNode.get("id_token").asText();

				Request request2 = new Request.Builder()
						.url("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken)
						.addHeader("Bearer", idToken)
						.get()
						.build();

				try (Response response2 = client.newCall(request2).execute()) {
					String payloadResponse = response2.body().string();

					JsonNode payloadJsonNode = new ObjectMapper().readTree(payloadResponse);
					String payloadGoogleId = payloadJsonNode.get("id").asText();
					String payloadEmail = payloadJsonNode.get("email").asText();
					String payloadPicture = payloadJsonNode.get("picture").asText();

					// 将用户数据返回为JSON
					return "{\"googleId\": \"" + payloadGoogleId + "\", \"email\": \"" + payloadEmail + "\", \"picture\": \"" + payloadPicture + "\"}";
				}
			}
		}
	}
}
