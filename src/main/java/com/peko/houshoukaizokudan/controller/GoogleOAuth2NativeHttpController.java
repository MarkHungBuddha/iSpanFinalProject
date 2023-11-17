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
	public void oauth2Callback(@RequestParam(required = false) String code, HttpServletResponse httpResponse) throws IOException {
		if (code == null) {
			// 如果没有提供 code，重定向用户去登录或者做其他操作
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "授权码缺失");
			return;
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

				String accessToken = jsonNode.has("access_token") ? jsonNode.get("access_token").asText() : null;

				if (accessToken == null) {
					httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "获取访问令牌失败");
					return;
				}

				Request requestUserInfo = new Request.Builder()
						.url("https://www.googleapis.com/oauth2/v3/userinfo")
						.addHeader("Authorization", "Bearer " + accessToken)
						.get()
						.build();

				try (Response responseUserInfo = client.newCall(requestUserInfo).execute()) {
					String userInfoResponse = responseUserInfo.body().string();
					JsonNode userInfoJsonNode = new ObjectMapper().readTree(userInfoResponse);

					// ...省略其他字段

					// 设置响应内容类型为HTML
					httpResponse.setContentType("text/html");

					// 发送JavaScript代码到客户端
					httpResponse.getWriter().write("<script>\n" +
							"window.opener.postMessage(" + userInfoResponse + ", '*');\n" +
							"window.close();\n" +
							"</script>");
				}
			} catch (IOException e) {
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器错误: " + e.getMessage());
			}
		}
	}
}
