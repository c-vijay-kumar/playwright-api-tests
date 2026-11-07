package com.QA.api.Test.Day22;
import java.io.IOException;
import com.API.Data.apiUI;
import com.API.Data.apiuiList;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;



public class ProductAPIUITest {
    Playwright p;
    APIRequest apiRequest;
    APIRequestContext context;

    @BeforeTest 
    public void startengine(){
        p=Playwright.create();
        apiRequest = p.request();
        context = apiRequest.newContext();
    }

    // @AfterTest 
    // public void teardown(){
        // p.close();
    // }

    @Test 
    public void api()throws IOException{

        APIResponse getResponse = context.get("https://api.practicesoftwaretesting.com/products");

        int statuscode = getResponse.status();
        String reponsetext = getResponse.text();
        System.out.println("this is teh response text: " + reponsetext);
        Assert.assertEquals(statuscode, 200);
        ObjectMapper mapper = new ObjectMapper();
        apiuiList getres = mapper.readValue(reponsetext, apiuiList.class);
        
        System.out.println("this is the list of get response: " + getres);
        

        apiUI get = getres.getData().get(0);

        System.out.println("tis is the get id : " + get.getId());
        System.out.println("this is the first name : "+ get.getName());

        System.out.println("this is the description : "+ get.getDescription());

        System.out.println("this is the price of the itea : "+ get.getPrice());
    }

    @Test 
    public void UI(){
        p = Playwright.create();
        Browser b = p.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        BrowserContext context = b.newContext();
        Page page = context.newPage();
        page.navigate("https://practicesoftwaretesting.com/auth/login");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email address"))
            .fill("customer@practicesoftwaretesting.com");

        page.locator("//input[@id='password']").fill("welcome01");

        page.locator("//input[@value='Login']").click();

    
        page.locator("//a[normalize-space()='Home']").click();

        page.locator("//input[@id='search-query']").fill("Combination Pliers");
        page.locator("//button[normalize-space()='Search']").click();

        page.locator("//img[@alt='Combination Pliers']").click();

        

        







        

    
    }


}
