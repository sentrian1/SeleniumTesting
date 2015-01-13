//package com.example.tests;
package mainPackage;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.thoughtworks.selenium.SeleneseTestCase;

import java.io.File;
import java.io.FileInputStream;
import java.lang.Thread;

import mainPackage.Actions;


public class Application extends SeleneseTestCase{
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	
	public int xRows_TC,xCols_TC,xRows_TS,xCols_TS;
	public String[][] xlDataTS; // Test Step excel data
	public String[][] xlDataTC; // Test Case excel data
	public String vTCID,vIP1,vIP2;
	

	// Library/Class of the selenium actions
	//   - login, adding new user, ...
	public Actions actions;


	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		// WebDriver driver;
		driver = new ChromeDriver();
		// driver = new FirefoxDriver();
		baseUrl = "https://staging.jointlyhealth.com";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		actions = new Actions(driver);
		
		// Set up the Excel spreadsheet to an array
		Object[] resultsTC = new Object[3];
		Object[] resultsTS = new Object[3];
		resultsTC = xlReadSheet("automationPlan2.xls",1);
		resultsTS = xlReadSheet("automationPlan2.xls",2);
		
		xlDataTC = (String[][]) resultsTC[0];
		xRows_TC = (int) resultsTC[1];
		xCols_TC = (int) resultsTC[2];
		
		xlDataTS = (String[][]) resultsTS[0];
		xRows_TS = (int) resultsTS[1];
		xCols_TS = (int) resultsTS[2];
	
		
		//xlDataTC = xlReadSheet("automationPlan.xls",1);
		//xlDataTS = xlReadSheet("automationPlan.xls",2);
	
	}

	@Test
	public void testSuperUserLogin() throws Exception {
		driver.get(baseUrl + "/login/");
		
		// Go through the excel spreadsheets
		for(int i=1;i<xRows_TC;i++){

			if(xlDataTC[i][3].equals("N")){
				continue; // skip over this test case
			}
			vTCID = xlDataTC[i][0]; // save the test case number for later

			for (int j = 1; j < xRows_TS; j++) {

				if (xlDataTS[j][0].equals(vTCID)) {

					// This is where the automation should be run from
					// Gather the important information
					String vKeyword = xlDataTS[j][4];
					// Need a way to extract data from cell
					// ie get the username in 'name=username'
					String elementType = "";
					if (xlDataTS[j][5].startsWith("//input")
							|| xlDataTS[j][5].startsWith("//select")) {
						int index1 = xlDataTS[j][5].indexOf("'");
						int index2 = xlDataTS[j][5].lastIndexOf("'");
						vIP1 = xlDataTS[j][5].substring(index1 + 1, index2);
					}
					else if (xlDataTS[j][5].startsWith("name=")
							|| xlDataTS[j][5].startsWith("link=")
							|| xlDataTS[j][5].startsWith("id=")) {
						int len = xlDataTS[j][5].length();
						int index = xlDataTS[j][5].indexOf("=");
						elementType = xlDataTS[j][5].substring(0,4);
						vIP1 = xlDataTS[j][5].substring(index + 1, len);
						
					}
					else if(xlDataTS[j][5].startsWith("v")){
						// This means we will be getting the data from another sheet if inputs
						vIP1 = xlDataTS[j][5];
					}
					else{
						vIP1 = xlDataTS[j][5];
					}
					vIP2 = xlDataTS[j][6];
					System.out.println(vIP1+" "+vIP2);
					

					// Maybe have an excel file which maps the keys to the
					// actual functions
					if (vKeyword.equals("sLogin")) {
						actions.login(vIP1, vIP2);
					}
					if (vKeyword.equals("sSelect")) {
						new Select(driver.findElement(By.id(vIP1))).selectByVisibleText(vIP2);
					}
					if (vKeyword.equals("sType")) {
						if (elementType.equals("name")){
							driver.findElement(By.name(vIP1)).sendKeys(vIP2);
						}
						else if (elementType.equals("link")){
							driver.findElement(By.linkText(vIP1)).sendKeys(vIP2);
						}
						else{
							driver.findElement(By.id(vIP1)).sendKeys(vIP2);
						}
					}
					if (vKeyword.equals("sClickAndWait")){
						if (elementType.equals("link")){
							driver.findElement(By.linkText(vIP1)).click();
						}
						else if (elementType.equals("name")){
							driver.findElement(By.name(vIP1)).click();
						}
						else{
							driver.findElement(By.name(vIP1)).click();
						}
					}
					if (vKeyword.equals("sAddSelection")){
						new Select(driver.findElement(By.id(vIP1))).selectByVisibleText(vIP2);
						driver.findElement(By.id("id_groups_add_link")).click();
					}

				}

			}

			
			
		}
		
		
		
		// Logging in
		//actions.login("jointly","J0in7lyH5alth");
		
		// Adding a new user to the system
		//actions.newUser("testuser","Jeremy123","Jeremy","Cooper","email@email.com","Modelers");
		
		// Adding a new patient to the system (and into a carepool with a new device)
		//actions.newPatient("test", "12345", "MRN123", "email@email.com", "Diabetes Model", "[other] Fora D40d", "00:00:00:12:34:56","carepool123");
	
		
		Thread.sleep(10000); // wait 10000 milliseconds
	
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
	
	public Object[] xlReadSheet(String sPath,int sheetNum) throws Exception {
		File myFile = new File(sPath);
		FileInputStream myStream = new FileInputStream(myFile);
		HSSFWorkbook mywb = new HSSFWorkbook(myStream);
		HSSFSheet mySheet = mywb.getSheetAt(sheetNum);

		int nRows = mySheet.getLastRowNum() + 1;
		int nCols = mySheet.getRow(0).getLastCellNum();
		String[][] data = new String[nRows][nCols];

		for (int i = 0; i < nRows; i++) {
			HSSFRow myRow = mySheet.getRow(i);
			for (int j = 0; j < nCols; j++) {
				HSSFCell myCell = myRow.getCell(j);
				String value = cellToString(myCell);
				data[i][j] = value;
			}
		}
		Object[] result = {data,nRows,nCols};
		return result;

	}
	
	public String cellToString(HSSFCell myCell) {

		int type = myCell.getCellType();

		Object results = 0;

		switch (type) {
		case 0:
			results = myCell.getNumericCellValue();
			break;
		case 1:
			results = myCell.getStringCellValue();
			break;
		case 3:
			results = "-";
			break;
		}

		return results.toString();

	}
	
	@SuppressWarnings("finally")
	public boolean isInString(String str1, String str2) throws Exception{
		try{
			str1.indexOf(str2);
		}catch (Exception e){
			return false;
		}finally{
			return true;
		}
	}
	
}
