package com.QA.api.Test.Day23paralleltesting.Tests;

import com.microsoft.playwright.*;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CrossBrowserTest {

    @DataProvider(
        name = "browserProvider",
        parallel = true
    )
    public Object[][] browserProvider() {

        return new Object[][] {
            {"chromium"},
            {"firefox"},
            {"webkit"}
        };
    }

    @Test(dataProvider = "browserProvider")
    public void verifyToolshopAcrossBrowsers(
        String browserName
    ) {

        try (Playwright playwright = Playwright.create()) {

            Browser browser;

            // Launch selected browser
            if (browserName.equals("chromium")) {

                browser = playwright.chromium().launch();

            } else if (browserName.equals("firefox")) {

                browser = playwright.firefox().launch();

            } else {

                browser = playwright.webkit().launch();
            }

            try {
                BrowserContext context = browser.newContext();

                try {
                    Page page = context.newPage();

                    page.navigate(
                        "https://practicesoftwaretesting.com/"
                    );

                    Assert.assertTrue(
                        page.locator("body").isVisible(),
                        "Toolshop should load in " + browserName
                    );

                    System.out.println(
                        "Successfully tested on: " + browserName
                    );

                } finally {
                    context.close();
                }

            } finally {
                browser.close();
            }
        }
    }
}