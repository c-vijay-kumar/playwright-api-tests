package com.QA.api.Test.Day22;



import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class AuthenticationApiTest {

    static final String BASE = "https://api.practicesoftwaretesting.com";
    static final String EMAIL = "customer@practicesoftwaretesting.com";

    @Test
    public void loginThroughAPI() {
        try (Playwright playwright = Playwright.create()) {

            APIRequestContext api = playwright.request().newContext(
                    new APIRequest.NewContextOptions().setBaseURL(BASE));

            try {
                // 1. Login
                String loginPayload = "{\"email\":\"" + EMAIL + "\",\"password\":\"welcome01\"}";
                APIResponse response = api.post("/users/login",
                        RequestOptions.create()
                                .setHeader("Content-Type", "application/json")
                                .setData(loginPayload));

                Assert.assertEquals(response.status(), 200, "Login should be successful");

                JsonObject json = JsonParser.parseString(response.text()).getAsJsonObject();
                Assert.assertTrue(json.has("access_token"), "Response should contain access_token");

                String token = json.get("access_token").getAsString();
                Assert.assertFalse(token.isBlank(), "Access token should not be empty");

                // 2. Call a protected endpoint with the token
                APIRequestContext authenticatedApi = playwright.request().newContext(
                        new APIRequest.NewContextOptions()
                                .setBaseURL(BASE)
                                .setExtraHTTPHeaders(Map.of("Authorization", "Bearer " + token)));

                try {
                    APIResponse me = authenticatedApi.get("/users/me");
                    Assert.assertEquals(me.status(), 200, "/users/me should return 200 with a valid token");

                    JsonObject user = JsonParser.parseString(me.text()).getAsJsonObject();
                    Assert.assertEquals(user.get("email").getAsString(), EMAIL);
                    System.out.println("Logged-in user: " + user);
                } finally {
                    authenticatedApi.dispose();
                }

                // 3. Negative check: same endpoint without a token
                APIResponse noToken = api.get("/users/me");
                Assert.assertEquals(noToken.status(), 401, "No token should be rejected");

            } finally {
                api.dispose();
            }
        }
    }

    @Test
    public void loginWithWrongPasswordFails() {
        try (Playwright playwright = Playwright.create()) {
            APIRequestContext api = playwright.request().newContext(
                    new APIRequest.NewContextOptions().setBaseURL(BASE));
            try {
                APIResponse response = api.post("/users/login",
                        RequestOptions.create()
                                .setHeader("Content-Type", "application/json")
                                .setData("{\"email\":\"" + EMAIL + "\",\"password\":\"wrong-password\"}"));

                Assert.assertEquals(response.status(), 401);
            } finally {
                api.dispose();
            }
        }
    }
}