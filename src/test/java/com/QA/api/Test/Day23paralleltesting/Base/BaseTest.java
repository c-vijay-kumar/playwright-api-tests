package com.QA.api.Test.Day23paralleltesting.Base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BaseTest {

    private static final ThreadLocal<Playwright> playwrightTL = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserTL = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextTL = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageTL = new ThreadLocal<>();
    private static final ThreadLocal<APIRequestContext> APIcontextTL = new ThreadLocal<>();
    private static final ThreadLocal<APIRequest> requestTL = new ThreadLocal<>();
    private static final ThreadLocal<String> BrowserNameTL = new ThreadLocal<>();

    protected Page getPage() {
        return pageTL.get();
    }

    protected APIRequestContext getAPIContext() {
        return APIcontextTL.get();
    }

    protected APIRequest getAPIRequest() {
        return requestTL.get();
    }

    protected String getBrowserName() {
        return BrowserNameTL.get();
    }

    public void log(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + message);
    }

    // "browser" comes from testng.xml; if the XML doesn't set it, chromium is used
    @Parameters("browser")
    @BeforeMethod
    public void setup(@Optional("chromium") String browserName) {
        Playwright playwright = Playwright.create();
        playwrightTL.set(playwright);

        BrowserType type;
        switch (browserName.toLowerCase()) {
            case "firefox": type = playwright.firefox(); break;
            case "webkit":  type = playwright.webkit();  break;
            default:        type = playwright.chromium();
        }

        Browser b = type.launch(new BrowserType.LaunchOptions().setHeadless(false));
        browserTL.set(b);

        BrowserContext context = b.newContext();
        contextTL.set(context);

        Page page = context.newPage();
        pageTL.set(page);

        APIRequest request = playwright.request();
        requestTL.set(request);
        APIcontextTL.set(request.newContext(
                new APIRequest.NewContextOptions().setBaseURL("https://api.practicesoftwaretesting.com")));

        BrowserNameTL.set(browserName);

        System.out.println("Test started in thread: " + Thread.currentThread().getName());
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() {
        System.out.println("Test finished in thread: " + Thread.currentThread().getName());
        try {
            if (contextTL.get() != null) contextTL.get().close();
            if (browserTL.get() != null) browserTL.get().close();
            if (APIcontextTL.get() != null) APIcontextTL.get().dispose();
            if (playwrightTL.get() != null) playwrightTL.get().close();
        } finally {
            pageTL.remove();
            contextTL.remove();
            browserTL.remove();
            APIcontextTL.remove();
            requestTL.remove();
            BrowserNameTL.remove();
            playwrightTL.remove();
        }
    }
}