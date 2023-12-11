package demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestFile1 {
    public static void main(String[] args) {
        String filepath = "utenti.txt";
        ArrayList<Login> login = new ArrayList<>();
        String url = "http://the-internet.herokuapp.com/login";

        // Impostazioni del sistema per il WebDriver

        try (Scanner reader = new Scanner(new File(filepath));
                BufferedWriter writer = new BufferedWriter(new FileWriter("risultati.txt"))) {
            while (reader.hasNext()) {
                Login loginRead = new Login();
                loginRead.setUsername(reader.next());
                loginRead.setPassword(reader.next());
                loginRead.setBrowser(reader.next());
                login.add(loginRead);
            }

            // Utilizzo dell'iteratore per scorrere l'ArrayList e stampare le informazioni di ciascun oggetto Login
            Iterator<Login> iterator = login.iterator();
            while (iterator.hasNext()) {
                Login loginObj = iterator.next();
                System.out.println("Username: " + loginObj.getUsername());
                System.out.println("Password: " + loginObj.getPassword());
                System.out.println("Browser: " + loginObj.getBrowser());
                System.out.println("-----------");

                try {
                    // Esegui il login per ciascun utente
                    String message = proceduraLogin(loginObj.getUsername(), loginObj.getPassword(), url,
                            loginObj.getBrowser());
                    System.out.println(message);

                    // Ottieni lo status code dell'URL
                    int statusCode = getURLStatusCode(url);

                    // Scrivi il risultato nel file
                    writer.write("Username: " + loginObj.getUsername() + "\n");
                    writer.write("Password: " + loginObj.getPassword() + "\n");
                    writer.write("Browser: " + loginObj.getBrowser() + "\n");
                    writer.write("Risultato: " + message + "\n");
                    writer.write("Status Code: " + statusCode + "\n");
                    writer.write("-----------\n");

                } catch (Exception e) {
                    // Gestisci l'eccezione in base alle tue esigenze
                    System.out.println("Errore durante l'esecuzione: " + e.getMessage());
                    // Scrivi un messaggio di errore nel file
                    writer.write("Errore durante l'esecuzione: " + e.getMessage() + "\n");
                    writer.write("-----------\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String proceduraLogin(String user, String pass, String url, String browser) throws Exception {
        WebDriver driver = null;
        String message = null;

        try {
            switch (browser) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                default:
                    driver = new org.openqa.selenium.edge.EdgeDriver();
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
            if (browser.equals("edge")) {
                UrlArrivo = "https://the-internet.herokuapp.com/secure";
            }

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
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            connection.disconnect();
            return statusCode;
        } catch (IOException e) {
            // Gestisci l'eccezione se l'URL non è raggiungibile o è errato
            throw new IOException("Errore durante l'ottenimento dello status code dell'URL: " + e.getMessage());
        }
    }
}

