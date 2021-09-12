package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        BlockSequence bruv = new BlockSequence();

        bruv.init(1);
        bruv.find();
        bruv.open("https://www.google.com/search?q=whats+my+ip+address&rlz=1C1CHBF_enCA938CA938&oq=wha&aqs=chrome.2.69i57j69i59l2j69i65l2j69i60l3.1341j0j7&sourceid=chrome&ie=UTF-8");
        bruv.delay(5000);
   }
}


//a series of commands to easily automate web tasks
class BlockSequence {
    //variables for later
    int[] position = new int[] { 0, 0 };
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

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        actions = new Actions(driver);
    }

    public void newIP() throws InterruptedException {
        this.open("https://www.proxynova.com/proxy-server-list/country-ca/");
        String newIP = this.driver.findElements(By.xpath("//*[@id=\"tbl_proxy_list\"]/tbody[1]/tr[1]/td[1]")).get(0).getText();
        String newPort = this.driver.findElements(By.xpath("//*[@id=\"tbl_proxy_list\"]/tbody[1]/tr[1]/td[2]")).get(0).getText();

        System.out.println(newIP);

        options.addArguments("--proxy-server=" + newIP + ":" + newPort);
        this.driver = new ChromeDriver(options);
        this.open("https://whatismyipaddress.com/");
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

    public void find() throws IOException {
//        WebElement element = driver.findElement(By.cssSelector("div[data-testid='nav-Look up items-item-Look up items']"));
//        System.out.println(element.getText());

        String str1="192.168.0.201";
        String str2="255.255.255.0";
        String[] command1 = { "netsh", "interface", "ip", "set", "address",
                "name=", "Local Area Connection" ,"source=static", "addr=",str1,
                "mask=", str2};
        Process pp = java.lang.Runtime.getRuntime().exec(command1);
        System.out.println( pp);

//        netsh interface ipv4 set address name="Wi-Fi" static 192.168.3.8 255.255.255.0 192.168.3.1
    }



}
























