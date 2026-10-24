package com.QA.api.Test;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;

public class APIResponseHeader {
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
    public void ResponeHeaders(){

        APIResponse response = apicontext.get("https://gorest.co.in/public/v2/users");
        int statusCode = response.status();
        System.out.println("Status code is: "+statusCode);
        Assert.assertEquals(statusCode, 200);
        System.out.println(response.url());

        //headers bu using map
        Map<String,String> headersresponse= response.headers();
        headersresponse.forEach((k, v) -> System.out.println(k + " :" + v));
        Assert.assertEquals(headersresponse.get("content-type"),"application/json; charset=utf-8");
        Assert.assertEquals(headersresponse.get("server"), "cloudflare");
    }
    @Test 
    public void listheaders(){
        APIResponse response = apicontext.get("https://gorest.co.in/public/v2/users");
        //headers by using list
        System.out.println("this is featching headers thorugh list");
        List<HttpHeader> headerslistresposne = response.headersArray();
        for(HttpHeader e : headerslistresposne){
            System.out.println(e.name + " :" + e.value );
        }
        
    }
    @AfterTest 
    public  void teardown(){
        p.close();
    }
}
