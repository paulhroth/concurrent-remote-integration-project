package com.paulhroth.selenium.tests;

import com.paulhroth.selenium.parser.Interpreter;
import com.paulhroth.selenium.parser.SaucePlatform;
import com.paulhroth.selenium.parser.StaXParser;
import com.saucelabs.junit.Parallelized;
import com.saucelabs.saucerest.SauceREST;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(Parallelized.class)
public class TestNew {

    // setup strings: feel free to modify these as required
    private final String sauce_user = Interpreter.getLink("src/main/config/userinfo.properties", "username");
    private final String sauce_access = Interpreter.getLink("src/main/config/userinfo.properties", "accesskey");
    private final String sauce_pass = Interpreter.getLink("src/main/config/userinfo.properties", "password");
    private final String baseUrl = Interpreter.getLink(
            "src/main/config/baseurls.properties", "CMO");
    private final String initialOwnerGroup = "AcmePublisher";
    private final String finalOwnerGroup = "AcmeAdvertising";

    // these are required for the test to work
    private WebDriver driver;
    private SauceREST client;
    private String jobID;
    private boolean passed;
    private String browser;
    private String os;
    private String version;

    public TestNew(String os, String version, String browser) {
        super();
        this.os = os;
        this.version = version;
        this.browser = browser;
    }

    @Parameterized.Parameters
    public static LinkedList<String[]> browsersStrings() throws Exception {
        StaXParser parser = new StaXParser();
        List<SaucePlatform> sauceplatforms = parser
                .readConfig("src/main/config/config.xml");
        LinkedList<String[]> browsers = Interpreter.interpret(sauceplatforms);
        return browsers;
    }

    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        capabilities.setCapability(CapabilityType.VERSION, version);
        capabilities.setCapability(CapabilityType.PLATFORM, os);
        client = new SauceREST(sauce_user, sauce_access);
        this.driver = new RemoteWebDriver(new URL("http://" + sauce_user + ":"
                + sauce_access + "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);
        jobID = ((RemoteWebDriver) driver).getSessionId().toString();
        passed = false;
    }

    @Test
    public void webDriver() throws Exception {
        // loads site
        driver.get(baseUrl);
        if (((RemoteWebDriver) driver).getCapabilities().getBrowserName().equals("internet explorer")) {
            driver.manage().window().maximize();
        }

        // logs in
        driver.findElement(By.id("id_username")).clear();
        driver.findElement(By.id("id_username")).sendKeys(sauce_user);
        driver.findElement(By.id("id_password")).clear();
        driver.findElement(By.id("id_password")).sendKeys(sauce_pass);
        driver.findElement(By.cssSelector("button.btn.btn-primary")).click();

        // navigates to the Native Ad Creator
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElements(By.linkText("Native Ad Creator")).size() > 0;
            }
        });       
        driver.findElement(By.linkText("Native Ad Creator")).click();

        // creates a New Group with the name
        // "test_<random 20 character long string>"
        final String groupName = generateString();
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElements(By.cssSelector("a[href*=\"/native_ads/group/new\"]")).size() > 0;
            }
        });
        if (((RemoteWebDriver) driver).getCapabilities().getBrowserName().equals("firefox")
            || ((RemoteWebDriver) driver).getCapabilities().getBrowserName().equals("chrome")) {
            driver.findElement(By.cssSelector("a[href*=\"/native_ads/group/new\"]")).click();
        } else {
            driver.findElement(By.cssSelector("a[href*=\"/native_ads/group/new\"]")).sendKeys(Keys.ENTER);
        }
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElements(By.id("id_name")).size() > 0;
            }
        });        
        driver.findElement(By.id("id_name")).clear();
        driver.findElement(By.id("id_name")).sendKeys("test_" + groupName);
        new Select(driver.findElement(By.id("id_ownergroup")))
                .selectByVisibleText(initialOwnerGroup);
        driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div/a[contains(text(),\"Save\")]")).click();

        // modifies the newly created group
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElements(By.xpath(".//*[@id='main']/div/div/div/div/p[contains(text(),'" + groupName + "')]/../div/a[contains(text(),'Modify Group')]")).size() > 0;
            }
        });
        driver.findElement(
                By.xpath(".//*[@id='main']/div/div/div/div/p[contains(text(),'" + groupName + "')]/../div/a[contains(text(),'Modify Group')]"))
                .click();
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElements(By.id("id_ownergroup")).size() > 0;
            }
        });
        new Select(driver.findElement(By.id("id_ownergroup")))
                .selectByVisibleText(finalOwnerGroup);
        driver.findElement(By.xpath("//*[@id=\"header\"]/div/div/div/div/a[contains(text(),\"Save\")]")).click();

        // passes the test
        passed = true;
    }
    
    // generates a random string in order to allow everything created by this
    // test to be unique
    public String generateString() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return (sb.toString());
    }

    @After
    public void tearDown() throws Exception {
        if (passed) {
            client.jobPassed(jobID);
        } else {
            client.jobFailed(jobID);
        }
        driver.quit();
    }
}
