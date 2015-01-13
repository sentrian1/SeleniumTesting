package mainPackage;

import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.lang.Thread;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Actions {
	WebDriver driver;
	
	public Actions(WebDriver driver){
		this.driver = driver;
	}
	
	// Logging into the server
	// Takes inputs of username and password
	// Example:
	//    login("jointly","J0in7lyH5alth);
	public void login(String username, String password) {
		
		driver.findElement(By.id("login-username-inputEl")).clear();
		driver.findElement(By.id("login-username-inputEl")).sendKeys(username); // username
		driver.findElement(By.id("login-password-inputEl")).clear();
		driver.findElement(By.id("login-password-inputEl")).sendKeys(password); // password
		driver.findElement(By.id("login-password-inputEl")).sendKeys(Keys.ENTER);
	}
	
	// Add a new user to the system
	// Takes inputs of username, password, firstName, lastName, email, group
	// Example:
	//    newUser("jeremy123","Jeremy123","Jeremy","Cooper","email@email.com","Modelers");
	public void newUser(String username, String password, String firstName, String lastName, String email, String group){
		
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if (isElementPresent(By.cssSelector("tr.model-user > th")))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}
		
		driver.findElement(By.linkText("Users")).click();
		driver.findElement(By.linkText("Add user")).click();
		driver.findElement(By.id("id_username")).clear();
		driver.findElement(By.id("id_username")).sendKeys(username);// user username
		driver.findElement(By.id("id_password1")).clear();
		driver.findElement(By.id("id_password1")).sendKeys(password);// user password
		driver.findElement(By.id("id_password2")).clear();
		driver.findElement(By.id("id_password2")).sendKeys(password);// user password confirm
		driver.findElement(By.id("id_first_name")).clear();
		driver.findElement(By.id("id_first_name")).sendKeys(firstName);// user first name
		driver.findElement(By.id("id_last_name")).clear();
		driver.findElement(By.id("id_last_name")).sendKeys(lastName);// user last name
		driver.findElement(By.id("id_email")).clear();
		driver.findElement(By.id("id_email")).sendKeys(email);// user email
		new Select(driver.findElement(By.id("id_client"))).selectByVisibleText("Caremore");
		driver.findElement(By.id("id_cellNumber")).clear();
		driver.findElement(By.id("id_cellNumber")).sendKeys("800 555 5555");
		driver.findElement(By.id("id_homeNumber")).clear();
		driver.findElement(By.id("id_homeNumber")).sendKeys("800 555 5555");
		driver.findElement(By.name("_save")).click();
		
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if (isElementPresent(By.cssSelector("div.selector-available > h2")))
					break;
			} catch (Exception e) {
			}
			//Thread.sleep(1000);
		}
		new Select(driver.findElement(By.id("id_groups_from"))).selectByVisibleText(group);
		// ERROR: Caught exception [ERROR: Unsupported command [addSelection |
		// id=id_groups_from | label=Modelers]]
		driver.findElement(By.id("id_groups_add_link")).click();

		driver.findElement(By.name("_save")).click();
		
		driver.findElement(By.linkText("Home")).click();
	}
	
	// Create a new device
	// Takes input: deviceClass, deviceAddress, hubID
	// Example:
	//   newDevice("[other] Fora D40d","00:00:00:09:99:00","QA");
	public void newDevice(String deviceClass, String deviceAddress, String hubID){
		
		driver.findElement(By.linkText("Devices")).click();
	    driver.findElement(By.linkText("Add device")).click();
	    new Select(driver.findElement(By.id("id_deviceClass"))).selectByVisibleText(deviceClass);
	    driver.findElement(By.id("id_address")).clear();
	    driver.findElement(By.id("id_address")).sendKeys(deviceAddress);
	    driver.findElement(By.id("id_hubId")).clear();
	    driver.findElement(By.id("id_hubId")).sendKeys(hubID);
	    driver.findElement(By.id("id_enabled")).click();
	    driver.findElement(By.name("_save")).click();
	    driver.findElement(By.linkText("Home")).click();
	}
	
	// Create a new patient
	// Takes input: firstName, lastName, mrn, email, dieaseModel, deviceClass, deviceAddress
	// Example:
	//   newPatient("test", "12345", "MRN123", "email@email.com", "Diabetes Model", "[other] Fora D40d", "00:00:00:12:34:56");
	public void newPatient(String firstName, String lastName, String mrn, String email, String diseaseModel, String deviceClass, String deviceAddress, String carepool){
		
		newDevice(deviceClass,deviceAddress, "QA");
		
		// Extract today's date to use for the device from and device to
		DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = dateformat.format(date);
		
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		//String today = String.format("%d-%d-%d",year,month,day);
		String date1 = String.format("%d-%d-%d",day,month,year-1);
		String date2 = String.format("%d-%d-%d",day,month,year+1);
		
		
	    driver.findElement(By.linkText("Patients")).click();
	    driver.findElement(By.linkText("Add patient")).click();
	    driver.findElement(By.id("id_firstName")).clear();
	    driver.findElement(By.id("id_firstName")).sendKeys(firstName);
	    driver.findElement(By.id("id_lastName")).clear();
	    driver.findElement(By.id("id_lastName")).sendKeys(lastName);
	    driver.findElement(By.linkText("Today")).click();
	    new Select(driver.findElement(By.id("id_gender"))).selectByVisibleText("Male");
	    driver.findElement(By.id("id_planId")).clear();
	    driver.findElement(By.id("id_planId")).sendKeys(mrn);
	    driver.findElement(By.id("id_cellNumber")).clear();
	    driver.findElement(By.id("id_cellNumber")).sendKeys("800 555 5555");
	    driver.findElement(By.id("id_homeNumber")).clear();
	    driver.findElement(By.id("id_homeNumber")).sendKeys("800 555 5555");
	    new Select(driver.findElement(By.id("id_phoneOpt"))).selectByVisibleText("Cell phone");
	    driver.findElement(By.id("id_emailAddr")).clear();
	    driver.findElement(By.id("id_emailAddr")).sendKeys(email);
	    new Select(driver.findElement(By.id("id_client"))).selectByVisibleText("Caremore");
	    new Select(driver.findElement(By.id("id_disease_model"))).selectByVisibleText(diseaseModel);
	    driver.findElement(By.linkText("Add another Device Assignment")).click();
	    new Select(driver.findElement(By.id("id_deviceassignment_set-0-device"))).selectByVisibleText(deviceClass+": "+deviceAddress);
	    driver.findElement(By.id("id_deviceassignment_set-0-prov_from_0")).clear();
	    driver.findElement(By.id("id_deviceassignment_set-0-prov_from_0")).sendKeys(date1);
	    driver.findElement(By.id("id_deviceassignment_set-0-prov_to_0")).clear();
	    driver.findElement(By.id("id_deviceassignment_set-0-prov_to_0")).sendKeys(date2);
	    driver.findElement(By.linkText("Now")).click();
	    driver.findElement(By.xpath("(//a[contains(text(),'Now')])[2]")).click();
	    driver.findElement(By.linkText("Now")).click();
	    driver.findElement(By.cssSelector("div.form-row.field-prov_to > div > p.datetime > span.datetimeshortcuts > a")).click();
	    driver.findElement(By.xpath("(//a[contains(text(),'Now')])[2]")).click();
	    driver.findElement(By.name("_save")).click();
	    driver.findElement(By.linkText("Home")).click();
	    
	    addToCarepool(carepool,firstName,lastName,today,"as849");
	    
	}
	
	// Create a carepool and add recent patient
	public void addToCarepool(String carepoolName,String firstName, String lastName, String dateToday, String caseManager){
		
		driver.findElement(By.linkText("Care pools")).click();
	    driver.findElement(By.linkText("Add care pool")).click();
	    driver.findElement(By.id("id_name")).clear();
	    driver.findElement(By.id("id_name")).sendKeys(carepoolName);
	    
	    String patient = String.format("%s, %s (%s)",lastName,firstName,dateToday);
	    new Select(driver.findElement(By.id("id_mm_patients_from"))).selectByVisibleText(patient);
	    driver.findElement(By.id("id_mm_patients_add_link")).click();
	  
	    new Select(driver.findElement(By.id("id_mm_users_from"))).selectByVisibleText(caseManager);
	    driver.findElement(By.id("id_mm_users_add_link")).click();
	    driver.findElement(By.name("_save")).click();
	    
	    driver.findElement(By.linkText("Home")).click();
	}
	
	
	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
}
