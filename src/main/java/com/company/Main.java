package com.company;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.environment.EnvironmentUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

public class Main {

    static int x = 0;
    static int y = 0;

    static WebDriver driver;
    static Actions acts;
    static ChromeOptions option = new ChromeOptions();

    public static void main(String[] args) throws IOException {
//        option.addArguments("--headless");
//        option.addArguments("--disable-extensions");
        option.addArguments("--disable-gpu", "--window-size=1920,1080", "--start-maximized", "--ignore-certificate-errors", "--no-sandbox", "--disable-dev-shm-usage", "--disable-notifications", "--disable-infobars", "--disable-popup-blocking");

        //this is to get the chrome driver even if its on a different operating system
        //(Mostly user for if this program goes on a Heroku server
        if(System.getProperty("os.name").equals("Windows 10")) {
            String userPath = System.getProperty("user.dir");
            System.setProperty("webdriver.chrome.driver", userPath + "/chromedriver.exe");
        } else {
            option.setBinary(EnvironmentUtils.getProcEnvironment().get("GOOGLE_CHROME_SHIM"));
        }

        //start the chrome driver
        driver = new ChromeDriver(option);
        acts = new Actions(driver);

        String url = "http://google.com";
        System.out.println("bruh");

        driver.get(url);
    }

    public static void moveTo(int xPos, int yPos, int timeout) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(timeout);
        acts.moveByOffset(xPos - x, yPos - y).click().build().perform();
        x = xPos;
        y = yPos;
    }
}
