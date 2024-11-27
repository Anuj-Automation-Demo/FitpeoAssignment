package org.fitpeo.assignment;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class FitpeoAssignment {

    public static void main(String[] args) throws Exception
    {
        // Initialize WebDriver (Chrome)
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Robot robot = new Robot();
        Actions actions = new Actions(driver);

        try
        {
            // Step 1: Navigate to FitPeo Homepage
            driver.get("https://www.fitpeo.com");
            driver.manage().window().maximize();
            System.out.println("Navigated to HomePage");

            // Step 2: Navigate to the Revenue Calculator Page
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Revenue Calculator")));
            WebElement revenueCalculatorLink = driver.findElement(By.linkText("Revenue Calculator"));
            revenueCalculatorLink.click();
            // Ensure the page is loaded
            wait.until(ExpectedConditions.urlContains("revenue-calculator"));
            System.out.println("Revenue Calculator Page");

            // Step 3: Scroll down to the slider section
            WebElement sliderSection = driver.findElement(By.xpath("//span[contains(@class,'MuiSlider-thumb')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", sliderSection);

            //validate slider section is visible
            Assert.assertTrue(sliderSection.isDisplayed(), "Slider section is not visible.");
            System.out.println("Slider is fully visible.");

            System.out.println("Scrolled to Slider Section");

            // Step 4: Adjust the slider to 820
            WebElement sliderInput = driver.findElement(By.cssSelector("input[type='range']"));
            actions.clickAndHold(sliderSection).moveByOffset( 93, 0).release().perform();

            //Adjust the slider to 820
            for(int i=1;i<=3;i++) {
                robot.keyPress(KeyEvent.VK_RIGHT);
                robot.keyRelease(KeyEvent.VK_RIGHT);
            }

            // Verify text field value updated to 820
            String sliderValue = sliderInput.getAttribute("value");
            if (sliderValue.equals("820")) {
                System.out.println("Slider successfully adjusted to value: " + sliderValue);
            } else {
                System.out.println("Failed to adjust slider. Current value: " + sliderValue);
            }

            //Locate slide Input field
            WebElement sliderTextField = driver.findElement(By.xpath("//div[contains(@class, 'MuiOutlinedInput-root')]//input"));

            // Step 5: Enter value 560 in the text field
            sliderTextField.clear();
            sliderTextField.click();

            //Clear the value again end enter the new value
            for(int i=1;i<=3;i++) {
                robot.keyPress(KeyEvent.VK_BACK_SPACE);
                robot.keyRelease(KeyEvent.VK_BACK_SPACE);
            }
            sliderTextField.sendKeys("560");

            //Step 6: verify slider updated value 560
            String updatedSliderValue = sliderTextField.getAttribute("value");
            if ("560".equals(updatedSliderValue))
            {
                System.out.println("Slider and text field updated correctly to 560.");
            } else
            {
                System.out.println("Failed to update slider and text field to 560.");
            }


            // Step 7: Select the required CPT codes checkboxes
            List<String> cptCodesToSelect = Arrays.asList("CPT-99091", "CPT-99453", "CPT-99454", "CPT-99474");
            for (String cptCode : cptCodesToSelect) {
                try {
                    //Locate the checkboxes
                    WebElement checkbox = driver.findElement(By.xpath(
                            "//p[text()='" + cptCode + "']/following::input[@type='checkbox'][1]"
                    ));

                    //scroll to view checkboxes are selected
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", checkbox);

                    //Validate Checkboxes are selected
                    if (!checkbox.isSelected()) {
                        checkbox.click();
                        System.out.println(cptCode + " checkbox selected.");
                    } else {
                        System.out.println(cptCode + " checkbox is already selected.");
                    }

                } catch (Exception e) {
                    System.out.println("Error selecting checkbox for CPT code: " + cptCode);
                    e.printStackTrace();
                }
            }

            // Step 8: Validate the Total Recurring Reimbursement
            WebElement reimbursementHeader = driver.findElement(By.xpath("//p[contains(text(),'Total Recurring Reimbursement for all Patients Per Month:')]//following-sibling::p"));
            String reimbursementText = reimbursementHeader.getText();

            //Reimbursement according to the 560 patients as updated recently
            if (reimbursementText.contains("$75600"))
            {
                System.out.println("Total Recurring Reimbursement is correct: " + reimbursementText);
            } else
            {
                System.out.println("Total Recurring Reimbursement is incorrect: " + reimbursementText);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Step 9: Close the browser after test execution
            driver.quit();
        }
    }
}
