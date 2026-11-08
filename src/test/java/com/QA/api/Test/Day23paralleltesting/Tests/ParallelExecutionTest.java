package com.QA.api.Test.Day23paralleltesting.Tests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.QA.api.Test.Day23paralleltesting.Base.BaseTest;


public class ParallelExecutionTest extends BaseTest {
    @Test 
    public void test1(){
        getPage().navigate("https://practicesoftwaretesting.com/");

        Assert.assertTrue(getPage().url().contains("practicesoftwaretesting.com"));
        System.out.println("test 1 is completed");

    }
    @Test 
    public void test2(){
        getPage().navigate("https://practicesoftwaretesting.com/");
        Assert.assertTrue(getPage().locator("body").isVisible());
        System.out.println("test 2 is completed");
    }

    @Test 
    public void test3(){
        getPage().navigate("https://practicesoftwaretesting.com/");
        Assert.assertTrue(getPage().title()!=null);
        System.out.println("test 3 is completed");
    }
    
}
