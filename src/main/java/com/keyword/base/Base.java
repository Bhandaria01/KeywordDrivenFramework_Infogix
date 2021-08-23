package com.keyword.base;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;



public class Base {


	public WebDriver driver;
	public Properties prop;
	
	// browser initiate function
	public WebDriver init_driver(String browserName) {
		if(browserName.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "/Users/Apoorva/eclipse-workspace/chromedriver/chromedriver.exe");
			if(prop.getProperty("headless").equalsIgnoreCase("yes")) {

				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless");
				driver = new ChromeDriver(options);

			}else {
				driver = new ChromeDriver();
				driver.manage().window().maximize();
				
				

			}
		}
		return driver;
	}
	
	// properties loading function
	public Properties init_properties() {
		prop = new Properties();
		try {
			FileInputStream ip = new FileInputStream("C:\\Users\\Apoorva\\eclipse-workspace\\KeyWordDrivenFrameworkInfogix1"+"\\src\\main\\java\\com\\keyword\\config\\config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return prop;

	}

}



