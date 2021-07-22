package com.company;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.environment.EnvironmentUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        BlockSequence bruv = new BlockSequence();

        bruv.init(1);
        bruv.open("http://google.com");
        bruv.type("vicious syndicate\n");
        bruv.delay(5000);
        bruv.close();
    }

    //a series of commands to easily automate web tasks
    static class BlockSequence {
        //variables for later
        int[] position = new int[]{0,0};
        WebDriver driver;
        Actions actions;
        static ChromeOptions options = new ChromeOptions();

        //run this to initiate the sequence
        public void init(int editLevel) throws IOException {
            switch (editLevel) {
                case 0:
                    options.addArguments("--headless");
                case 1:
                    options.addArguments("--disable-extensions");
                case 2:
                    options.addArguments("--disable-notifications");
                default:
                    options.addArguments("--disable-gpu", "--window-size=1920,1080", "--start-maximized", "--ignore-certificate-errors", "--no-sandbox", "--disable-dev-shm-usage", "--disable-infobars", "--disable-popup-blocking");
            }

            if(System.getProperty("os.name").equals("Windows 10")) {
                String userPath = System.getProperty("user.dir");
                System.setProperty("webdriver.chrome.driver", userPath + "/chromedriver.exe");
            } else {
                options.setBinary(EnvironmentUtils.getProcEnvironment().get("GOOGLE_CHROME_SHIM"));
            }

            driver = new ChromeDriver(options);
            actions = new Actions(driver);
        }

        //open a new tab
        public void open(String url) throws InterruptedException {
            driver.get(url);
            new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(By.xpath("/html")));
        }

        //close the current tab
        public void close() {
            driver.close();
        }

        //move to a point on a page
        public void moveTo(int x, int y) {
            actions.moveByOffset(x - position[0] , y - position[1]);
            position = new int[] {x, y};
        }

        //click the current location
        public void click(int times) {
            for(int i = 0; i < times; i++) {
                actions.click().build().perform();
            }
        }

        //delay for a period of time in milliseconds
        public void delay(int time) throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(time);
        }

        //type text at the current location (note you may need to click to select the current location)
        public void type(String text) {
            actions.sendKeys(text).perform();
        }
    }
}
