package com.peko.houshoukaizokudan.controller;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peko.houshoukaizokudan.config.*;

import jakarta.servlet.http.HttpServletResponse;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Controller
public class GoogleOAuth2NativeHttpController {
	// 原生使用(okhttp) http request, response 執行 OAuth2 的寫法 
	
	@Autowired
	private GoogleOAuth2Config googleOauth2Config;
	
	
	private final String scope = "https://www.googleapis.com/auth/userinfo.email";
	
	@GetMapping("/google-login")
	public String googleLogin(HttpServletResponse response)   {
		
		// 直接 redirect 導向 Google OAuth2 API
		String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + googleOauth2Config.getClientId() +
                "&response_type=code" +
                "&scope=openid%20email%20profile" +
                "&redirect_uri=" + googleOauth2Config.getRedirectUri() +
                "&state=state";
        return "redirect:" + authUrl;
	}

	// 這邊要 google 那邊設定 return uri http:xxxxxxxxxxx/google-callback
    @GetMapping("/google-callback")
    public String oauth2Callback(@RequestParam(required = false) String code) throws IOException {
        if (code == null) {
            String authUri = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code" +
                    "&client_id=" + googleOauth2Config.getClientId() +
                    "&redirect_uri=" + googleOauth2Config.getRedirectUri() +
                    "&scope=" + scope;
            return "redirect:" + authUri;
        } else {
        	
        	// 從google回來後會拿到code, 要用code加底下一些屬性再發給 google 一次，主要準備拿token
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("code", code)
                    .add("client_id", googleOauth2Config.getClientId())
                    .add("client_secret", googleOauth2Config.getClientSecret())
                    .add("redirect_uri", googleOauth2Config.getRedirectUri())
                    .add("grant_type", "authorization_code")
                    .build();

            // 因有敏感資訊，這邊要用 post request
            Request request = new Request.Builder()
                    .url("https://oauth2.googleapis.com/token")
                    .post(requestBody)
                    .build();

            // 執行request後解析 response 拿credentials裡面的access_token跟id_token
            try (Response response = client.newCall(request).execute()) {
                String credentials = response.body().string();   
//                System.out.println("credentials:" + credentials);
                
                JsonNode jsonNode = new ObjectMapper().readTree(credentials);
                String accessToken = jsonNode.get("access_token").asText();                
	            String idToken = jsonNode.get("id_token").asText();
	            
	            // 拿到 access_token 和 id_token 後再發給 google 一次，主要想要拿到使用者資訊
	            Request request2 = new Request.Builder()
	                    .url("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+ accessToken)
	                    .addHeader("Bearer", idToken)
	                    .get()
	                    .build();
	            // 執行request後解析 response 拿使用者資訊的 json 字串
	            try (Response response2 = client.newCall(request2).execute()) {
	            	String payloadResponse = response2.body().string();
	            	System.out.println("payloadResponse: " + payloadResponse);
	            	
//	            	response2String: {
//	            		  "id": "1sdfgsdfgfdg478",
//	            		  "email": "xxxxemail",
//	            		  "verified_email": true,
//	            		  "name": "Jerry Tai",
//	            		  "given_name": "Jerry",
//	            		  "family_name": "Tai",
//	            		  "picture": "https://lh3.googleusercontent.com/a/AGNmyxZsergsdrgfsdgsdgX6Q0RKEFji4snhy4d8g=s96-c",
//	            		  "locale": "zh-TW"
//	            		}
	            	
	            	JsonNode payloadJsonNode = new ObjectMapper().readTree(payloadResponse);
	            	String payloadGoogleId = payloadJsonNode.get("id").asText();
	            	String payloadEmail = payloadJsonNode.get("email").asText();
	            	String payloadName = payloadJsonNode.get("name").asText();
	            	String payloadPicture = payloadJsonNode.get("picture").asText();
	            	String payloadLocale = payloadJsonNode.get("locale").asText();
	            	
	            	
	            	System.out.println("payloadGoogleId: "+ payloadGoogleId);
	            	System.out.println("payloadEmail: "+ payloadEmail);
	            	System.out.println("payloadName: "+ payloadName);
	            	System.out.println("payloadPicture: "+ payloadPicture);
	            	System.out.println("payloadLocale: "+ payloadLocale);

	            	
	            }
                
                
               
            }

            return "redirect:/";
        }
    }

}