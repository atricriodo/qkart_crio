package QKART_SANITY_LOGIN.Module1;

import java.util.List;
import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchResult {
    WebElement parentElement;

    public SearchResult(WebElement SearchResultElement) {
        this.parentElement = SearchResultElement;
    }

    /*
     * Return title of the parentElement denoting the card content section of a
     * search result
     */
    public String getTitleofResult() {
        String titleOfSearchResult = parentElement.getText();

        // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
        // Find the element containing the title (product name) of the search result and
        // assign the extract title text to titleOfSearchResult
        return titleOfSearchResult;
    }

    /*
     * Return Boolean denoting if the open size chart operation was successful
     */
    public Boolean openSizechart() {
        try {

            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            // Find the link of size chart in the parentElement and click on it
            WebElement button=parentElement.findElement(By.xpath("//*[@id='root']/div/div/div[3]/div/div[2]/div[1]/div/div[1]/button"));
            button.click();
            return true;
        } catch (Exception e) {
            System.out.println("Exception while opening Size chart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the close size chart operation was successful
     */
    public Boolean closeSizeChart(WebDriver driver) {
        try {
            // Thread.sleep(2000);
            Actions action = new Actions(driver);

            // Clicking on "ESC" key closes the size chart modal
            action.sendKeys(Keys.ESCAPE);
            action.perform();
            Thread.sleep(2000);
            return true;
        } catch (Exception e) {
            System.out.println("Exception while closing the size chart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean based on if the size chart exists
     */
    public Boolean verifySizeChartExists() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            /*
             * Check if the size chart element exists. If it exists, check if the text of
             * the element is "SIZE CHART". If the text "SIZE CHART" matches for the
             * element, set status = true , else set to false
             */
            Thread.sleep(2000);
            if(parentElement.findElement(By.xpath("//*[@id='root']/div/div/div[3]/div[1]/div[2]/div[2]/div/div[1]/button")).isDisplayed()){
                System.out.println("\n\nFound the chart!!!\n\n");
                return parentElement.findElement(By.xpath("//*[@id='root']/div/div/div[3]/div[1]/div[2]/div[1]/div/div[1]/button")).getText().equals("SIZE CHART");
            }
            else return false;
                // System.out.println("AAA");
            // parentElement.findElement(By.className("MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButtonBase-root  css-177pwqq")).click();
            // System.out.print("!!!!++++"+parentElement.findElement(By.className("MuiButton-root MuiButton-text MuiButton-textPrimary MuiButton-sizeMedium MuiButton-textSizeMedium MuiButtonBase-root  css-1urhf6j")).isDisplayed());
            // return parentElement.findElement(By.className("MuiButton-root MuiButton-text MuiButton-textPrimary MuiButton-sizeMedium MuiButton-textSizeMedium MuiButtonBase-root  css-1urhf6j")).isDisplayed();
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if the table headers and body of the size chart matches the
     * expected values
     */
    public Boolean validateSizeChartContents(List<String> expectedTableHeaders, List<List<String>> expectedTableBody,
            WebDriver driver) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            /*
             * Locate the table element when the size chart modal is open
             * 
             * Validate that the contents of expectedTableHeaders is present as the table
             * header in the same order
             * 
             * Validate that the contents of expectedTableBody are present in the table body
             * in the same order
             */
            
            
            WebElement tableContent = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div/table"));
            
            System.out.println("\n\n1!!!!\n\n");
            

            List<WebElement> tableHeader= tableContent.findElements(By.xpath("./thead/tr/th"));
            
            for(WebElement data : tableHeader){
                System.out.println(data.getText());
            }
            System .out.println("\n\n3!!!!\n\n");
            int i = 0;
            for(WebElement data : tableHeader){
                System.out.println("\nSo... COMPARING "+data.getText()+" and "+ expectedTableHeaders.get(i)+"\n");
                if(data.getText().equals(expectedTableHeaders.get(i))){
                    System.out.println("\nSo... "+data.getText()+" and "+ expectedTableHeaders.get(i)+" are equal\n");
                    i++;
                    // continue;
                }  
                else return false;
            }
            
            List<WebElement> tableRows= tableContent.findElements(By.xpath("./tbody/tr"));
            System.out.println("\n\n4!!!!\n\n");
            int j = 0;
            for(WebElement row : tableRows){
                List<WebElement> a= row.findElements(By.xpath("./td"));
                i=0;
                for(WebElement data : a){
                    if(data.getText().equals((expectedTableBody.get(j)).get(i)))  {
                        System.out.println("\nSo... "+data.getText()+" and "+ expectedTableBody.get(j).get(i)+" are equal\n");
                        i++;
                        continue;
                    }

                    else return false;
                    
                }
                j++;
            }
                                
                
            return true;

        } catch (Exception e) {
            System.out.println("Error while validating chart contents");
            return false;
        }
    }

    /*
     * Return Boolean based on if the Size drop down exists
     */
    public Boolean verifyExistenceofSizeDropdown(WebDriver driver) {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            // If the size dropdown exists and is displayed return true, else return false
            
            return(parentElement.findElement(By.xpath("//div/select")).isDisplayed());
        } catch (Exception e) {
            return status;
        }
    }
}