package com.company;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockSequence block = new BlockSequence();

        block.init(1, 0);
        block.open("https://www.google.com/search?q=whats+my+ip+address&rlz=1C1CHBF_enCA938CA938&oq=wha&aqs=chrome.2.69i57j69i59l2j69i65l2j69i60l3.1341j0j7&sourceid=chrome&ie=UTF-8");
        block.delay(5000);
   }
}

//a series of commands to easily automate web tasks
class BlockSequence {
    //variables for later
    int[] position = new int[] { 0, 0 };
    WebDriver driver;
    Actions actions;
    static ChromeOptions chromeOptions = new ChromeOptions();
    static Random random = new Random();

    //get a more random variable
    public static int betterRandom(int min, int max, int interval, double weight) {
        int output = 0;
        for(int i = 0; i <= (max-min)/interval; i++)
            output += random.nextInt((int) (2 * weight * interval));

        return min + output;
    }

    //run this to initiate the sequence
    public void init(int editLevel, int profileNumber) {
        switch (editLevel) {
            case 0:
                chromeOptions.addArguments("--headless");
            case 1:
                chromeOptions.addArguments("--disable-extensions");
            case 2:
                chromeOptions.addArguments("--disable-notifications");
            default:
                chromeOptions.addArguments("--disable-gpu", "--window-size=1600,900", "--ignore-certificate-errors", "--no-sandbox", "--disable-dev-shm-usage", "--disable-infobars", "--disable-popup-blocking");
        }

        if(profileNumber != 0) {
            chromeOptions.addArguments("--user-data-dir=C:\\Users\\RoyMc\\AppData\\Local\\Google\\Chrome\\User Data");
            chromeOptions.addArguments("profile-directory=Profile " + profileNumber);
        }

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(chromeOptions);
        actions = new Actions(driver);
    }

    //change IP address (Not working)
//    public void newIP() throws InterruptedException {
//        this.open("https://www.proxynova.com/proxy-server-list/country-ca/");
//        String newIP = this.driver.findElements(By.xpath("//*[@id=\"tbl_proxy_list\"]/tbody[1]/tr[1]/td[1]")).get(0).getText();
//        String newPort = this.driver.findElements(By.xpath("//*[@id=\"tbl_proxy_list\"]/tbody[1]/tr[1]/td[2]")).get(0).getText();
//
//        System.out.println(newIP);
//
//        options.addArguments("--proxy-server=" + newIP + ":" + newPort);
//        this.driver = new ChromeDriver(options);
//        this.open("https://whatismyipaddress.com/");
//    }

    //open a new tab
    public void open(String url) {
        driver.get(url);
        new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(By.xpath("/html")));
    }

    //close the current tab
    public void close() {
        driver.close();
    }

    //switch to mobile view (Not working)
    public void toMobile() {
        actions.sendKeys(Keys.F12);
        actions.sendKeys(Keys.CONTROL, Keys.SHIFT, "m");
    }

    //move to a point on a page
    public void moveTo(int x, int y) {
        actions.moveByOffset(x - position[0] , y - position[1]);
        position = new int[] {x, y};
    }

    //click the current location a certain number of times
    public void click(int times) {
        for(int i = 0; i < times; i++) {
            actions.click().build().perform();
        }
    }

    //delay for a period of time in milliseconds
    public void delay(int time) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(time);
    }

    //delay for a period of time in milliseconds
    public void delay(int min, int max) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(betterRandom(min, max, 250, 0.3));
    }

    //type text at the current location (note you may need to click to select the current location)
    public void type(String text) {
        actions.sendKeys(text).perform();
    }

    //get text from an element
    public String getText(String identifier, String type) {
        switch (type){
            case "id":
                return driver.findElement(By.id(identifier)).getText();
            case "xpath":
                return driver.findElement(By.xpath(identifier)).getText();
            case "css":
                return driver.findElement(By.cssSelector(identifier)).getText();
            case "class":
                return driver.findElement(By.className(identifier)).getText();
            default:
                System.out.println("Please use one of the valid identifier formats (\"id\", \"xpath\", \"css\" or \"class\")");
                return null;
        }
    }

    //switch to a certain tab number
    public void switchToTab(int tabNumber) {
        driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(tabNumber));
    }

    //close all tabs but the first tab
    public void cleanTabs() {
        while (true) {
            try {
                this.switchToTab(1);
                driver.close();
            } catch (Exception e) {
                break;
            }
        }

        this.switchToTab(0);
    }

    //get the number of open tabs
    public int tabsLength() {
        return new ArrayList<>(driver.getWindowHandles()).size();
    }

    //move cursor to a certain web element
    public void moveToElement(WebElement element) {
        if(!element.isDisplayed())
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        actions.moveToElement(element);
    }

    //check if a web element is enabled
    public boolean checkEnabled(WebElement element) {
        try {
            return element.isEnabled() && element.isDisplayed() && !Objects.equals(element.getAttribute("disabled"), "disabled");
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //find an element by its cssSelector
    public WebElement[] findCssSelector(String CSSName) {
        return driver.findElements(By.cssSelector(CSSName)).toArray(new WebElement[0]);
    }

    public void clickRandomElementBySelector(String CSSName) throws InterruptedException {
        WebElement[] options = this.findCssSelector(CSSName);
        if(options.length > 0) {
            int itemNum = random.nextInt(options.length);

            this.moveToElement(options[itemNum]);
            this.delay(500, 1000);

            this.click(1);
        }
    }

    //copy text from the clipboard and turn it into a String (Not currently working)
    public String copy() throws IOException, UnsupportedFlavorException {
        actions.keyDown(Keys.CONTROL);
        actions.sendKeys("c");
        actions.keyUp(Keys.CONTROL);

        return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
    }
}
























