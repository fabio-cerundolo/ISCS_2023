package demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.HttpURLConnection;
import java.net.URL;
public class TestFile {
    public static void main(String[] args) throws IOException {
        String filepath = "utenti.txt";
        ArrayList<Login> login = new ArrayList<>();
        String url = "http://the-internet.herokuapp.com/login";
        

        // Impostazioni del sistema per il WebDriver
        
        
        

        try (Scanner reader = new Scanner(new File(filepath))) {
            while (reader.hasNext()) {
                Login loginRead = new Login();
                loginRead.setUsername(reader.next());
                loginRead.setPassword(reader.next());
                loginRead.setBrowser(reader.next());
                login.add(loginRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Utilizzo dell'iteratore per scorrere l'ArrayList e stampare le informazioni di ciascun oggetto Login
        Iterator<Login> iterator = login.iterator();
        BufferedWriter writer = new BufferedWriter(new FileWriter("risultati.txt"));
        java.util.Date date= new java.util.Date();
        writer.write("Reportato dal file utenti.txt"+ date);
        writer.write("\n");
        writer.newLine();
        while (iterator.hasNext()) {
            Login loginObj = iterator.next();
            System.out.println("Username: " + loginObj.getUsername());
            System.out.println("Password: " + loginObj.getPassword());
            System.out.println("Browser: " + loginObj.getBrowser());
            System.out.println("-----------");
            
            // Esegui il login per ciascun utente
            long start = System.currentTimeMillis();
            String message = proceduraLogin(loginObj.getUsername(), loginObj.getPassword(), url,
                    loginObj.getBrowser());
                    long end = System.currentTimeMillis();
                    long timeElapsed = end - start;
                    System.out.println("Time elapsed: " + timeElapsed + " ms");
                    int statusCode = getURLStatusCode(url);
                    writer.write("username: " + loginObj.getUsername() + " password: " + loginObj.getPassword());
                    writer.newLine();
                    writer.write("Browser: " + loginObj.getBrowser() + " Time elapsed: " + timeElapsed +
                    " ms "+" Status code: " + statusCode+" ");
                    writer.write(message);
                    writer.newLine();
                    
            System.out.println(message);
        }
        writer.close();
    }

    private static String proceduraLogin(String user, String pass, String url, String browser) {
        String message = null;
        WebDriver driver = null;
        

        try {
            switch (browser) {
                case "chrome":
                    driver = new ChromeDriver();
                  //  System.setProperty("webdriver.chrome.driver", "chromedriver");
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                  //  System.setProperty("webdriver.gecko.driver", "geckodriver");
                    break;
                default:
                    driver = new org.openqa.selenium.edge.EdgeDriver();
                   // System.setProperty("webdriver.edge.driver", "msedgedriver");
                    break;
            }

            driver.get(url);
            WebElement username = driver.findElement(By.name("username"));
            WebElement password = driver.findElement(By.name("password"));
            WebElement invio = driver.findElement(By.tagName("button"));
            username.sendKeys(user);
            password.sendKeys(pass);
            invio.click();
            String UrlArrivo = "http://the-internet.herokuapp.com/secure";
            if(browser.equals("edge")){UrlArrivo= "https://the-internet.herokuapp.com/secure";}
            String UrlAttuale = driver.getCurrentUrl();

            if (UrlAttuale.equals(UrlArrivo)) {
                message = "Login Successfully: " + user + " " + pass + " ";
            } else {
                WebElement successMessage = driver.findElement(By.id("flash"));
                if (successMessage.getText().substring(0, 13).equals("Your username")) {
                    message = "Username errato: " + user + " ";
                }
                if (successMessage.getText().substring(0, 13).equals("Your password")) {
                    message = "Password errata: " + pass + " ";
                }
                System.out.println("Login Failed!");
            }
        } finally {
            // Chiudi il WebDriver
            if (driver != null) {
                driver.quit();
            }
        }
        return message;
    }
        private static int getURLStatusCode(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int statusCode = connection.getResponseCode();
        connection.disconnect();
        return statusCode;
    }
}
