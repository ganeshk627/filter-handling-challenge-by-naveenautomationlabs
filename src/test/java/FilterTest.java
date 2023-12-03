import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static java.lang.String.format;
import static org.testng.Assert.assertTrue;

public class FilterTest {

    private static WebDriver driver;

    @DataProvider(name = "filter")
    public String[][] testdata() {
        String data[][] = new String[][]{
                {"Deals", "New"},
                {"Deals", "New", "Special offer"},
                {"Deals", "all"},

                {"Brands", "Apple", "T-Mobile®"},
                {"Brands", "All"},

                {"Operating System", "Android"},
                {"Operating System", "ALL"},
    };
        return data;
    }

    @Test(dataProvider = "filter")
    public void searchByFiltersTest(String filter, String... options) {
        filterProduct(filter, options);
        waitfor(10);
    }






    private static void filterProduct(String filter, String... options) {
        By FILTER = By.xpath("//legend[normalize-space()='" + filter + "']/ancestor::mat-expansion-panel-header");
        String OPTIONS_FORMAT = "//label[contains(.,'%s')]//input[@type='checkbox']";

        if(driver.findElement(FILTER).getAttribute("aria-expanded").equalsIgnoreCase("false")) {
            jsClick(FILTER);
            waitfor(2);
        }


            for (String option:options) {
                if(option.equalsIgnoreCase("all")) {
                    System.out.println("All options should be selected!!!!");
                    selectAll(filter);
                    break;
                }
                jsClick(By.xpath(format(OPTIONS_FORMAT, option)));
                assertTrue(driver.findElement(By.xpath(format(OPTIONS_FORMAT, option))).isSelected());
            }


    }

    @BeforeMethod
    public void beforeMethod() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().window().maximize();
        driver.get("https://www.t-mobile.com/tablets");
    }

    // closing browser
    @AfterMethod(alwaysRun = true)
    public void teardown() {
        driver.quit();
    }

    private static void selectAll(String filter) {
        By OPTIONS_ALL = By.xpath("//legend[normalize-space()='" + filter + "']/ancestor::fieldset//input[@type='checkbox']");
        List<WebElement> options = driver.findElements(OPTIONS_ALL);
        for (WebElement option:options) {
            jsClick(option);
            assertTrue(option.isSelected());
        }
    }

    private static void jsClick(By locator) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", driver.findElement(locator));
        waitfor(1);
        js.executeScript("arguments[0].click();", driver.findElement(locator));
        waitfor(2);
    }
    private static void jsClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
        waitfor(1);
        js.executeScript("arguments[0].click();", element);
        waitfor(2);
    }

    private static void waitfor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
