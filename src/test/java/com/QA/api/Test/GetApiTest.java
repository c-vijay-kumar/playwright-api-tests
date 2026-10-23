package com.QA.api.Test;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;
import org.testng.Assert;
import com.fasterxml.jackson.databind.*;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class GetApiTest {
    Playwright p;                 
    APIRequest request ;                  
    APIRequestContext apicontext;
    
    @BeforeTest 
    public void setup(){
        p= Playwright.create();                 
        request = p.request();                  
        apicontext= request.newContext();
    }

    @Test 
    public void getapitest() throws IOException{
        APIResponse response = apicontext.get("https://gorest.co.in/public/v2/users");
        int statusCode = response.status();
        System.out.println("Status code is: "+statusCode);
        Assert.assertEquals(statusCode, 200);
        String statusResponsetext = response.statusText();
        System.out.println("Status response text is: "+statusResponsetext);
        Assert.assertEquals(statusResponsetext, "OK");
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode Jsonresponse = mapper.readTree(response.body());
        String prettyJson = Jsonresponse.toPrettyString();
        System.out.println("Response body is: "+prettyJson);
    
        Assert.assertEquals(Jsonresponse.get(0).get("id").asInt(), 8680372);
        System.out.println("response body is validated");


        String responseURL = response.url();
        Assert.assertEquals(responseURL, "https://gorest.co.in/public/v2/users");


       Map<String,String> headers =  response.headers();
       System.out.println("Response headers are: "+headers);
       Assert.assertEquals(headers.get("content-type"), "application/json; charset=utf-8");
       System.out.println("Response headers are validated ");
    }

    @Test 
    public void getuserbyIdTest() throws IOException{
        System.out.println("this is test case 2 for users ");
        APIResponse response = apicontext.get("https://gorest.co.in/public/v2/users",
                                RequestOptions.create()
                                .setQueryParam("status", "active")
                                .setQueryParam("gender", "male" ));
        

        String responseCode = response.statusText();
        System.out.println("Response code is: "+responseCode);
        Assert.assertEquals(responseCode, "OK");

        String responseURL = response.url();
        System.out.println("Response URL is: "+responseURL);
        Assert.assertEquals(responseURL, "https://gorest.co.in/public/v2/users?status=active&gender=male");

        Assert.assertEquals(response.ok(), true);
        System.out.println("Response code is validated");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonresponse = mapper.readTree(response.body());
        String prettyJson = jsonresponse.toPrettyString();
        System.out.println("Response body is: "+prettyJson);


    }

    @AfterTest 
    public void tearndown(){
        p.close();
    }

}
