package com.QA.api.Test.PUT;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.API.Data.users;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class putAPI {
    Playwright p;
    APIRequest request;
    APIRequestContext context;
    @BeforeTest
    public void setup(){
        p=Playwright.create();
        request=p.request();
        context=request.newContext();
    } 
    @AfterTest 
    public void tearDown(){
        p.close();
    }

    @Test 
    public void PUTAPI()throws IOException{
        users user1 = users.builder()
                        .name("vk")
                        .email("vk"+ System.currentTimeMillis() + "@email.com")
                        .gender("male")
                        .status("active").build();

        APIResponse postresponse = context.post("https://gorest.co.in/public/v2/users",
                                            RequestOptions.create()
                                            .setHeader("Content-Type", "application/json")
                                            .setHeader("Authorization","Bearer c3939042e849c02e966d28572b17d896a50e6d3512ca3cafd01b706f33f893a4")
                                            .setData(user1));
        String statuscode = postresponse.statusText();
        System.out.println(statuscode);


        String responsebody= postresponse.text();
        System.out.println(responsebody);

        ObjectMapper mapper = new ObjectMapper();
        users json = mapper.readValue(responsebody,users.class);
        System.out.println("this is new mapped response");
        System.out.println(json);
        //Assert.assertEquals(statuscode, "OK");
        Assert.assertEquals(postresponse.status(),201);

        String userid= json.getId();
        System.out.println("this is the id that is created: "+ userid);


        user1.setEmail("this.vkupdated"+ System.currentTimeMillis() +"@gmail.com");
        user1.setStatus("inactive");
        APIResponse PUTresponse = context.put("https://gorest.co.in/public/v2/users/" + userid, 
                                        RequestOptions.create()
                                        .setData(user1)
                                        .setHeader("Content-Type", "application/json")  
                                   .setHeader("Authorization","Bearer c3939042e849c02e966d28572b17d896a50e6d3512ca3cafd01b706f33f893a4"));


        int putstatuscode = PUTresponse.status();
        System.out.println("the status code is :"+ putstatuscode);
        Assert.assertEquals(putstatuscode, 200);

        System.out.println("this is the put response ;"+ PUTresponse.text());

        users putresposeupdated = mapper.readValue(PUTresponse.text(), users.class);

        System.out.println("this is converted json put resposne " + putresposeupdated);

        Assert.assertEquals(putresposeupdated.getId(), userid);
        Assert.assertEquals(putresposeupdated.getStatus(),user1.getStatus());



        System.out.println("here getting get by id");
        APIResponse getResponse = context.get("https://gorest.co.in/public/v2/users/"+ putresposeupdated.getId(),
                            RequestOptions.create()
                            .setHeader("Content-Type", "application/json")
                            .setHeader("Authorization","Bearer c3939042e849c02e966d28572b17d896a50e6d3512ca3cafd01b706f33f893a4"));

        String gettext= getResponse.text();
        users getjsonres = mapper.readValue(gettext, users.class);

        System.out.println("this is getjson response; " + getjsonres);

        Assert.assertEquals(putresposeupdated, getjsonres);

        System.out.println("hence proved");

    }






}
