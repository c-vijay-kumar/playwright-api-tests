package com.QA.api.Test;


import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;

public class APIDisposeTest {
    Playwright p;
    APIRequest request;
    APIRequestContext apicontext;

    @BeforeTest 
    public void setup(){
        p= Playwright.create();
        request=p.request();
        apicontext = request.newContext();
        
    }

    @Test 
    public void disposeTest(){
        APIResponse response = apicontext.get("https://gorest.co.in/public/v2/users");
        int statusCode = response.status();
    System.out.println("Status code is: "+statusCode);
    Assert.assertEquals(statusCode, 200);
    String statusResponsetext = response.statusText();
    System.out.println("Status response text is: "+statusResponsetext);
    Assert.assertEquals(statusResponsetext, "OK");

    response.dispose();

    System.out.println(response.text());
    System.out.println("Response disposed successfully");
        
        

    }




    @AfterTest 
    public void teardown(){
        p.close();
    }

}
