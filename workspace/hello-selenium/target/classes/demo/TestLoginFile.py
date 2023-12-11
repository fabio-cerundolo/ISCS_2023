import os
from selenium import webdriver
from selenium.webdriver.common.keys import Keys

class Login:
    def __init__(self, username, password, browser):
        self.username = username
        self.password = password
        self.browser = browser

def main():
    filepath = "utenti.txt"
    logins = []
    url = "http://the-internet.herokuapp.com/login"

    # Impostazioni del sistema per il WebDriver

    with open(filepath, 'r') as reader, open("risultati.txt", 'w') as writer:
        for line in reader:
            data = line.split()
            login = Login(data[0], data[1], data[2])
            logins.append(login)

        # Utilizzo dell'iteratore per scorrere la lista e stampare le informazioni di ciascun oggetto Login
        for loginObj in logins:
            print("Username: " + loginObj.username)
            print("Password: " + loginObj.password)
            print("Browser: " + loginObj.browser)
            print("-----------")

            try:
                # Esegui il login per ciascun utente
                message = procedura_login(loginObj.username, loginObj.password, url, loginObj.browser)
                print(message)

                # Ottieni lo status code dell'URL
                statusCode = get_url_status_code(url)

                # Scrivi il risultato nel file
                writer.write("Username: " + loginObj.username + "\n")
                writer.write("Password: " + loginObj.password + "\n")
                writer.write("Browser: " + loginObj.browser + "\n")
                writer.write("Risultato: " + message + "\n")
                writer.write("Status Code: " + str(statusCode) + "\n")
                writer.write("-----------\n")

            except Exception as e:
                # Gestisci l'eccezione in base alle tue esigenze
                print("Errore durante l'esecuzione:", str(e))
                # Scrivi un messaggio di errore nel file
                writer.write("Errore durante l'esecuzione: " + str(e) + "\n")
                writer.write("-----------\n")

def procedura_login(user, password, url, browser):
    driver = None
    message = None

    try:
        if browser == "chrome":
            driver = webdriver.Chrome()
        elif browser == "firefox":
            driver = webdriver.Firefox()
        else:
            driver = webdriver.Edge()

        driver.get(url)
        username = driver.find_element("name", "username")
        password_field = driver.find_element("name", "password")
        submit_button = driver.find_element("tag name", "button")
        username.send_keys(user)
        password_field.send_keys(password)
        submit_button.click()

        url_arrivo = "http://the-internet.herokuapp.com/secure"
        if browser == "edge":
            url_arrivo = "https://the-internet.herokuapp.com/secure"

        url_attuale = driver.current_url

        if url_attuale == url_arrivo:
            message = "Login Successfully: " + user + " " + password + " "
        else:
            success_message = driver.find_element("id", "flash")
            if success_message.text.startswith("Your username"):
                message = "Username errato: " + user + " "
            elif success_message.text.startswith("Your password"):
                message = "Password errata: " + password + " "
            else:
                print("Login Failed!")

    finally:
        # Chiudi il WebDriver
        if driver is not None:
            driver.quit()
    
    return message

def get_url_status_code(url):
    try:
        import requests
        response = requests.get(url)
        return response.status_code
    except requests.RequestException as e:
        # Gestisci l'eccezione se l'URL non è raggiungibile o è errato
        raise Exception("Errore durante l'ottenimento dello status code dell'URL: " + str(e))

if __name__ == "__main__":
    main()
