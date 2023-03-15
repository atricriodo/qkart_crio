package QKART_SANITY_LOGIN.Module1;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    WebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app";

    public Home (WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Find and click on the Logout Button
            WebElement logout_button = driver.findElement(By.className("MuiButton-text"));
            logout_button.click();

            // Wait for Logout to Complete
            Thread.sleep(3000);

            return true;
        } catch (Exception e) {
            // Error while logout
            return false;
        }
    }

    /*
     * Returns Boolean if searching for the given product name occurs without any
     * errors
     */
    public Boolean searchForProduct(String product) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Clear the contents of the search box and Enter the product name in the search
            // box
            WebElement searchBox= this.driver.findElement(By.name("search"));
            searchBox.clear();
            searchBox.sendKeys(product);
            // Thread.sleep(1000);
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(1000);
            return true;
        } catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
            return false;
        }
    }

    /*
     * Returns Array of Web Elements that are search results and return the same
     */
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<WebElement>() {
        };
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Find all webelements corresponding to the card content section of each of
            // search results
            Thread.sleep(2000);
            // System.out.println("SEARCHING");
            searchResults= this.driver.findElements(By.xpath("//div/div/p[@class='MuiTypography-root MuiTypography-body1 css-yg30e6']"));
            // System.out.println(searchResults.size());
            System.out.println("Results are: ");
            for(WebElement re : searchResults){
                System.out.print('-'+re.getText()+'-');
            }
            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;

        }
    }

    /*
     * Returns Boolean based on if the "No products found" text is displayed
     */
    public Boolean isNoResultFound() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Check the presence of "No products found" text in the web page. Assign status
            // = true if the element is *displayed* else set status = false

            List<WebElement> re= getSearchResults();
            if(re.size()==0||re.get(0).getText()=="") return true;
            else return false;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if add product to cart is successful
     */
    public Boolean addProductToCart(String productName) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through each product on the page to find the WebElement corresponding
             * to the matching productName
             * 
             * Click on the "ADD TO CART" button for that element
             * 
             * Return true if these operations succeeds
             */
            // WebElement e = driver.findElement(By.xpath(String.format("//*[text()='%s']", productName)));
            List<WebElement> ex= getSearchResults();
            for (WebElement e:ex){
                SearchResult s= new SearchResult(e);
                System.out.println("\nHEY! MY NAME IS: "+ s.getTitleofResult()+" AND I AM COMPARED TO "+productName);
                if(s.getTitleofResult().equals(productName)){
                    System.out.println("HELLO");
                    WebElement atc= e.findElement(By.xpath("./../../div/button[@class='MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButton-fullWidth MuiButtonBase-root card-button css-54wre3']"));
                    System.out.println("HEllo from other side");
                    atc.click();
                    System.out.print("FOUND IT!");
                    return true;
                }
                else System.out.println("Hiiii");
            }
            {System.out.println("Unable to find the given product");
            return false;}  
        } catch (Exception e) {
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting the status of clicking on the checkout button
     */
    public Boolean clickCheckout() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find and click on the the Checkout button
            return status;
        } catch (Exception e) {
            System.out.println("Exception while clicking on Checkout: " + e.getMessage());
            return status;
        }
    }

    /*
     * Return Boolean denoting the status of change quantity of product in cart
     * operation
     */
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

            // Find the item on the cart with the matching productName

            // Increment or decrement the quantity of the matching product until the current
            // quantity is reached (Note: Keep a look out when then input quantity is 0,
            // here we need to remove the item completely from the cart)
            System.out.println("RUNNING");
            WebElement cartitem = driver.findElement(By.xpath("//*[@id='root']/div/div/div[3]/div[2]/div/div[1]/div/div[2]/div[1]"));
            if(cartitem.getText().equals(productName)){
                System.out.println("STILL RUNNING");
                if(quantity==0){
                    System.out.println("REALLY RUNNING");
                    WebElement minus= cartitem.findElement(By.xpath("./../div/div[1]/button[1]"));
                    while(Integer.parseInt(cartitem.findElement(By.xpath("./../div/div[1]/div")).getText())==1){
                        
                        System.out.println("PRESSED");
                        minus.click();
                        Thread.sleep(1000);
                    }
                    minus.click();
                    Thread.sleep(1000);
                    return true;
                }
                if(Integer.parseInt(cartitem.findElement(By.xpath("./../div/div[1]/div")).getText())==quantity){
                    System.out.println("REALLY RUNNING");
                    return true;
                }
                if(Integer.parseInt(cartitem.findElement(By.xpath("./../div/div[1]/div")).getText())>quantity){
                    System.out.println("REALLY RUNNING");
                    WebElement minus= cartitem.findElement(By.xpath("./../div/div[1]/button[1]"));
                    int count = (Integer.parseInt(cartitem.findElement(By.xpath("./../div/div[1]/div")).getText())-quantity);
                    for(int i=0; i<count; i++){
                        minus.click();
                        System.out.println("PRESSED");
                        Thread.sleep(1000);
                    }
                    return true;
                }
                if(Integer.parseInt(cartitem.findElement(By.xpath("./../div/div[1]/div")).getText())<quantity){
                    System.out.println("REALLY RUNNING");
                    WebElement plus= cartitem.findElement(By.xpath("./../div/div[1]/button[2]"));
                    int count=quantity-(Integer.parseInt(cartitem.findElement(By.xpath("./../div/div[1]/div")).getText()));
                    for(int i=0; i< count; i++){
                        plus.click();
                        System.out.println("PRESSED");
                        Thread.sleep(1000);
                    }
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            if (quantity == 0)
                return true;
            System.out.println("exception occurred when updating cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the cart contains items as expected
     */
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 07: MILESTONE 6

            // Get all the cart items as an array of webelements

            // Iterate through expectedCartContents and check if item with matching product
            // name is present in the cart
            

            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }
}
