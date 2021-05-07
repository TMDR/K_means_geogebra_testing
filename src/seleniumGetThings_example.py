import os
from selenium import webdriver  
from selenium.webdriver.common.keys import Keys  
from selenium.webdriver.chrome.options import Options
import sys

if len(sys.argv) == 3:
    chrome_options = Options()  
    chrome_options.add_argument("--headless") #headless means it opens no chrome window
    if sys.argv[2] == "w":
        driver = webdriver.Chrome(executable_path=os.path.abspath("./chromedriver_"+sys.argv[2]+".exe"),   options=chrome_options) #chrome driver should be in the working directory
    else:
        driver = webdriver.Chrome(executable_path=os.path.abspath("./chromedriver_"+sys.argv[2]),   options=chrome_options) #chrome driver should be in the working directory
    driver.get(sys.argv[1]) #opening the link
    #https://www.geogebra.org/calculator/jzccagrm #this is an example
    cont = True
    while cont: #while bcz the link takes time to load the element so while the element is not loaded
        try:
            element = driver.find_element_by_xpath('//*[@id="ggbApplet"]/main/div[2]/div[2]/div/div[3]/div/div[2]/div[1]/div/div/div')
            #finding the element having the points in it
            cont = False
        except:
            cont = True
    text_file = open("points_element_in_page.txt", "w", encoding="utf8")
    n = text_file.write(element.get_attribute('innerHTML'))
    text_file.close() #writing that html content of that div into a file for java to extract points from