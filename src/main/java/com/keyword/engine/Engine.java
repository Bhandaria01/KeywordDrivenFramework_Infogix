package com.keyword.engine;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Properties;
import java.util.Set;

import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.TakesScreenshot;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;    
import java.text.SimpleDateFormat;  
import java.lang.Math;   
import org.testng.asserts.SoftAssert;
import org.testng.Reporter;






import com.keyword.base.Base;


public class Engine {

	public WebDriver driver;
	public Properties prop;

	public static Workbook book;
	public static Sheet sheet;
	public Base base;
	
	public final String SCENERIO_SHEET_PATH = "C:\\Users\\Apoorva\\eclipse-workspace\\KeyWordDrivenFrameworkInfogix1\\" + "src\\main\\java\\com\\keyword\\scenarios\\Test_Navin.xlsx";
	SoftAssert softAssert = new SoftAssert();
	public void startExecution(String sheetName) {
		
		//create new file and import the testcase excel 
		FileInputStream file = null;
		
		try {
			file = new FileInputStream(SCENERIO_SHEET_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			book = WorkbookFactory.create(file);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// import the testcase sheet
		sheet = book.getSheet(sheetName);
		//column counter k
		int k = 0 ;
		//row counter i
		for(int i = 0; i<sheet.getLastRowNum();i++) {						//iterate over all rows and columns of sheet

			try {																						
				String LocatorName = sheet.getRow(i + 1).getCell(k + 1).toString().trim();	
				String LocatorValue = sheet.getRow(i + 1).getCell(k + 2).toString().trim();
				String action = sheet.getRow(i + 1).getCell(k + 3).toString().trim();
				String value = sheet.getRow(i + 1).getCell(k + 4).toString().trim();		

				// switch case for actions
				switch (action) {
				case "open browser":
					base = new Base();
					prop = base.init_properties();
					if(value.isEmpty() || value.equals("NA")) {
						base.init_driver(prop.getProperty("browser"));
					}else {

						driver = base.init_driver(value);
						takeSnapShot(driver, "test") ;
					}
					break;

				case "enter url":
					if(value.isEmpty() || value.equals("NA")) {
						driver.get(prop.getProperty("url"));
					}else {
						driver.get(value);
						takeSnapShot(driver, "test") ;
				        Reporter.log("The website is launched.", true);



					}
					break;
				case "verify url": 							// verifies testcase 2 - "regulatory compliance" link
				      ArrayList<String> newTb = new ArrayList<String>(driver.getWindowHandles());
				      driver.switchTo().window(newTb.get(1));
				      String act_curr_url = driver.getCurrentUrl();
				      driver.get(value);
				      String exp_curr_url = driver.getCurrentUrl();
				      softAssert.assertEquals(act_curr_url, exp_curr_url," Assert Failed - Link 'regulatory compliance' is not targetting to give URL.");
				      takeSnapShot(driver, "test") ;
				      Reporter.log("The website is verified.", true);
					
					break;

				case "close browser":
					driver.close();
					 Reporter.log("The browser is closed.", true);

					break;	

				default:
					break;
				}
				
				// actions will be performed as per the locator name and value given
				switch(LocatorName) {
				case "id":
					WebElement element  = driver.findElement(By.id(LocatorValue));
					if(action.equalsIgnoreCase("SendKeys")) {
						element.sendKeys(value);
						takeSnapShot(driver, "test") ;

					}else if(action.equalsIgnoreCase("click")){
						element.click();
						takeSnapShot(driver, "test") ;
					}
					LocatorName = null;
					break;
				case "xpath":

					if(action.equalsIgnoreCase("SendKeys")) {
						element  = driver.findElement(By.xpath(LocatorValue));
						element.sendKeys(value);
						takeSnapShot(driver, "test") ;
					}else if(action.equalsIgnoreCase("click")){
						element  = driver.findElement(By.xpath(LocatorValue));
						element.click();
						takeSnapShot(driver, "test") ;
						Thread.sleep(5000);
					}else if(action.equalsIgnoreCase("select")) {
						Select dropdown = new Select(driver.findElement(By.xpath(LocatorValue)));
						dropdown.selectByVisibleText(value);
						takeSnapShot(driver, "test") ;
					}else if(action.equalsIgnoreCase("validate")) { 			//validates thank you page 
						WebElement thankyou_page  = driver.findElement(By.xpath(LocatorValue));
						String actual = thankyou_page.getText();							
						softAssert.assertEquals(actual,value,"Thank you page is not displayed");
						takeSnapShot(driver, "test") ;
						 Reporter.log("Thank you page is displayed.", true);
						
					}else if(action.equalsIgnoreCase("press enter")) {
						element  = driver.findElement(By.xpath(LocatorValue));
						element.sendKeys(Keys.ENTER);
						takeSnapShot(driver, "test") ;
					}else if(action.equalsIgnoreCase("search")) {

						while(!driver.getPageSource().contains(value)) {
							driver.findElement(By.xpath("//a[contains(text(),'Next Page')]")).click();
							takeSnapShot(driver, "test") ;
						}
					}
					LocatorName = null;
					break;

				case "linktext":
					element  = driver.findElement(By.linkText(LocatorValue));
					if(action.equalsIgnoreCase("SendKeys")) {
						element.sendKeys(value);
						takeSnapShot(driver, "test") ;
					}else if(action.equalsIgnoreCase("click")){
						element.click();
						takeSnapShot(driver, "test") ;
					}
					LocatorName = null;
					break;

				default:
					break;
				}
			}
			catch(Exception e) {
			}



		}
	}
	
	// function for screenshots
	public static void takeSnapShot(WebDriver webdriver,String filename) throws Exception{
		//Convert web driver object to TakeScreenshot
		TakesScreenshot scrShot =((TakesScreenshot)webdriver);
		//Call getScreenshotAs method to create image file
		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
		//Move image file to new destination with random number
		File DestFile=new File("C:\\Users\\Apoorva\\eclipse-workspace\\KeyWordDrivenFrameworkInfogix1\\Screenshots"+Math.random()+".png");
		//Copy file at destination
		FileUtils.copyFile(SrcFile, DestFile);
		 
		
		}
	 
		
		
	
	
}




