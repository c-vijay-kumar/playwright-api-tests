package com.QA.api.Test.Day22;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class apiUIintegrationTest {
    Playwright p ;
    APIRequest request;
    APIRequestContext apicontext;
    Browser b ;
    BrowserContext bcontext;
    Page page;


    static final String EMAIL = "customer2@practicesoftwaretesting.com";
    static final String PASSWORD = "welcome01";
    ObjectMapper mapper = new ObjectMapper();


    String Email = "//input[@id='email']";
    String password = "//input[@id='password']";
    String loginButtun = "//input[@value='Login']";
    String UI = "https://practicesoftwaretesting.com";
    String prodname = "//input[@id='search-query']";
    String searchbuttun = "//button[normalize-space()='Search']";
    @BeforeTest 
    public void setup(){
        p= Playwright.create();
        request = p.request();
        apicontext = request.newContext();

        b=p.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        bcontext = b.newContext();
        page=bcontext.newPage();
    }

    @AfterTest 
    public void teardown(){
        p.close();
    }

    @Test 
    public void apilogin()throws Exception{
        APIResponse  apiresponse = apicontext.post("https://api.practicesoftwaretesting.com/users/login", RequestOptions.create()
                                            .setHeader("Content-Type", "application/json")
                                        .setData(mapper.writeValueAsString(Map.of("email",EMAIL,"password",PASSWORD))));
        Assert.assertEquals(apiresponse.status(), 200);

        System.out.println("login success through api : "+ apiresponse.text());
        String token = mapper.readTree(apiresponse.text()).get("access_token").asText();

        System.out.println("getting token");
        Assert.assertFalse(token.isBlank());
        System.out.println("token is success");

        APIResponse apiproductresponse = apicontext.get("https://api.practicesoftwaretesting.com/products");
        Assert.assertEquals(apiproductresponse.status(), 200);

        JsonNode products = mapper.readTree(apiproductresponse.text()).get("data");

        JsonNode product = null;
        for (JsonNode item : products) {
            if (item.path("in_stock").asBoolean(true)) {   // skip out-of-stock products
                product = item;
                break;
            }
        }
        //Assert.assertNotNull(product, "No in-stock product found on page 1");
        String productId = product.get("id").asText();
        String productName = product.get("name").asText();
        System.out.println("Chosen product: " + productName + " (" + productId + ")");
                


        //UI login 
        page.navigate("https://practicesoftwaretesting.com/auth/login");
        page.locator(Email).fill("customer@practicesoftwaretesting.com");
        page.locator(password).fill("welcome01");
        page.locator(loginButtun).click();
        page.waitForURL("**/account");

        System.out.println("success login through ui: ");
        page.locator("//a[normalize-space()='Home']").click();
        page.locator(prodname).fill(productName);
        page.locator(searchbuttun).click();
        page.locator("//img[@alt='Combination Pliers']").click();
        assertThat(page.locator("h1[data-test='product-name']")).hasText(productName);
        System.out.println("product name is same in api and ui: " + productName);
        System.out.println("Checked until the prodcut , all are same in api and ui - Hence validation done for one product by id ....");

        }
}
