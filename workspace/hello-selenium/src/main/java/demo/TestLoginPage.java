package demo;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;



public class TestLoginPage {
    public static void main(String[] args) {
        String url = "http://the-internet.herokuapp.com/login";
        String broswer="chrome";
        String messaggio_di_ritorno =proceduraLogin("tomsmith", "SuperSecretPassword!", url,broswer);
        System.out.println(messaggio_di_ritorno+ broswer);
        broswer="edge";
        messaggio_di_ritorno =proceduraLogin("tomsmith", "SuperSecretPassword!", url,broswer);
        System.out.println(messaggio_di_ritorno+ broswer);
        broswer="firefox";
        messaggio_di_ritorno =proceduraLogin("tomsmith", "SuperSecretPassword!", url,broswer);
        System.out.println(messaggio_di_ritorno+ broswer);


    
}

private static String proceduraLogin(String user, String pass, String url, String broswer) {
        String message=null;
        WebDriver driver =null;
       // WebDriver driver = new ChromeDriver();
       switch(broswer){
           case "chrome":driver = new ChromeDriver();
                        System.setProperty("webdriver.chrome.driver", "chromedriver");
                        
                        break;
            case "firefox":driver = new FirefoxDriver();
                        System.setProperty("webdriver.gecko.driver", "geckodriver");
                        
                        break;
           default : driver = new org.openqa.selenium.edge.EdgeDriver();
                       System.setProperty("webdriver.edge.driver", "msedgedriver.exe");
                        
                        break;
                        

       }
		
        
		

		driver.get(url);
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
            message="Login Succesfully: "+ user + " " + pass+ " ";
          //  WebElement SuccesMessage = driver.findElement(By.id("flash"));
           // System.out.println(SuccesMessage.getText());
           // WebElement LogOutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
           // LogOutButton.click();
        }
        else {
            WebElement successMessage = driver.findElement(By.id("flash"));
            if(successMessage.getText().substring(0,13).equals("Your username")){
                message="Username errato: "+user+ " ";

            }
            if (successMessage.getText().substring(0,13).equals("Your password")) {
                message="Password errata: "+pass+" ";
            }

            System.out.println("Login Failed!");
        }
       // SleepUtility.sleepMilliseconds(3000);
        driver.quit();
        return message;
    }
}
