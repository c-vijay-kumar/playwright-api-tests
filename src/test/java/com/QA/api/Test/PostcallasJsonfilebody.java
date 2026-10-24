package com.QA.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class PostcallasJsonfilebody{
    Playwright p;
    APIRequest request;
    APIRequestContext apicontext;
    @BeforeTest 
    public void setup(){
        p=Playwright.create();
        request = p.request();
        apicontext = request.newContext();
    }
    @Test 
    public void PostCreateTest() throws IOException{

        File file = new File("C:\\Users\\santh\\Documents\\playwright-api-tests\\src\\test\\data\\Payload.json");
        byte[] payload = Files.readAllBytes(file.toPath());

        APIResponse post = apicontext.post("https://gorest.co.in/public/v2/users", RequestOptions.create()
                            .setHeader("Content-Type","application/json")
                            .setHeader("Authorization","Bearer c3939042e849c02e966d28572b17d896a50e6d3512ca3cafd01b706f33f893a4")
                            .setData(payload));

        int postedcode = post.status();
        System.out.println(postedcode);

        ObjectMapper mapper =new ObjectMapper();
        JsonNode json = mapper.readTree(post.body());
        String jsonrespo = json.toPrettyString();
        System.out.println(jsonrespo);
        Assert.assertEquals(json.get("name").asText(), "Ck1");


        String id = json.get("id").asText();
        System.out.println("the id is: " + id);
        

        APIResponse getres = apicontext.get("https://gorest.co.in/public/v2/users/"+ id , 
            RequestOptions.create()
            .setHeader("Authorization","Bearer c3939042e849c02e966d28572b17d896a50e6d3512ca3cafd01b706f33f893a4")
        );

        ObjectMapper map = new ObjectMapper();
        JsonNode getjson = map.readTree(getres.body());
        String jsongetres = getjson.toPrettyString();
        Assert.assertEquals(jsongetres, jsonrespo);

    }

    @AfterTest 
    public void teardown(){
        p.close();    }
    


}
