package demo;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;



public class TestLoginPage {
    public static void main(String[] args) {
        System.out.println(proceduraLogin("tomsmith", "SuperSecretPassword"));

    
}

private static String proceduraLogin(String user, String pass) {
        String message=null;
        WebDriver driver = new ChromeDriver();
		System.setProperty("webdriver.chrome.driver", "chromedriver");
        
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		driver.get("http://the-internet.herokuapp.com/login");
        WebElement username = driver.findElement(By.name("username"));
        WebElement password = driver.findElement(By.name("password"));
        WebElement invio = driver.findElement(By.tagName("button"));
        username.sendKeys(user);
       // SleepUtility.sleepMilliseconds(3000);
        password.sendKeys(pass);
       // SleepUtility.sleepMilliseconds(3000);
        invio.click();
        String UrlArrivo ="http://the-internet.herokuapp.com/secure";
        String UrlAttuale = driver.getCurrentUrl();
        
        if(UrlAttuale.equals(UrlArrivo)) {
            System.out.println("Login Succesfully!");
            WebElement SuccesMessage = driver.findElement(By.id("flash"));
            System.out.println(SuccesMessage.getText());
           // WebElement LogOutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
           // LogOutButton.click();
        }
        else {
            WebElement successMessage = driver.findElement(By.id("flash"));
            if(successMessage.getText().substring(0,13).equals("Your username")){
                message="Username errato: "+user;

            }
            if (successMessage.getText().substring(0,13).equals("Your password")) {
                message="Password errata: "+pass;
            }

            System.out.println("Login Failed!");
        }
       // SleepUtility.sleepMilliseconds(3000);
        driver.quit();
        return message;
    }
}
