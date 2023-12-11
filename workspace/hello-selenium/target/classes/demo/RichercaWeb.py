from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
# Inizializza il driver del browser (in questo caso, Chrome)
driver = webdriver.Chrome()

try:
    # Apri Google
    driver.get("https://www.google.com")

    # Clicca sul pulsante "Accetta tutto" se presente
    try:
        accetto_tutto_button = driver.find_element(By.XPATH, "//button[contains(., 'Accetta tutto')]")
        accetto_tutto_button.click()
    except Exception as e:
        print("Pulsante 'Accetta tutto' non trovato o errore durante il clic:", str(e))

    # Trova la barra di ricerca
    search_box = driver.find_element("name", "q")

    # Esegui una ricerca
    search_box.send_keys("culo")
    search_box.send_keys(Keys.RETURN)

    # Attendi fino a quando i risultati della ricerca sono presenti
    WebDriverWait(driver, 20).until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, '[jsname="UWckNb"]')))

    # Trova tutti i link nei risultati utilizzando l'attributo jsname
    links = driver.find_elements(By.CSS_SELECTOR, '[jsname="UWckNb"]')
     
    # Trova il primo link di testo (ignora i video di YouTube)
    first_text_link = None
    for link in links:
        href = link.get_attribute("href")
        if "youtube.com" not in href:
            first_text_link = link
            break

    # Fai clic sul primo link di testo
    if first_text_link:
        first_text_link.click()
        # Attendi fino a quando il pop-up dei cookie è presente
        WebDriverWait(driver, 20).until(EC.presence_of_element_located((By.ID, 'qc-cmp2-ui')))

        # Trova il pulsante per chiudere il pop-up dei cookie utilizzando XPath
        chiudi_cookie_button = driver.find_element(By.XPATH, '//*[@id="qc-cmp2-ui"]/div[1]/button')
        chiudi_cookie_button.click()
        # Attendi fino a quando la pagina del risultato è completamente caricata
        WebDriverWait(driver, 20).until(EC.presence_of_element_located((By.TAG_NAME, "body")))
      
        # Estrai il testo dalla pagina
        page_text = driver.find_element(By.TAG_NAME, "body").text

        # Salva le prime 200 parole in un file di testo
        words = page_text.split()[:200]
        with open("prime_200_parole.txt", "w", encoding="utf-8") as file:
            file.write(" ".join(words))
    else:
        print("Nessun link di testo trovato.")

finally:
    # Chiudi il browser alla fine dell'esecuzione
    driver.quit()
