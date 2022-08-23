
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class LibGen {

    FileWriter historyFile = null;
    int previous = 1;

    int charCount = 0; //Will use these variables to help keep results clean.
    String temp = "";

    String query = "";

    public void search_Libgen(String search) {

        this.query = search;



        try {

            //Establish the WebDriver
            System.setProperty("webdriver.chrome.driver","C:\\Users\\etern\\IdeaProjects\\libgen_Automation\\src\\main\\resources\\chromedriver_win32\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            //options.addArguments("--headless", "--window-size=2736,1824"); - Headless just hides the browser - no real need for that here
            options.addArguments("--window-size=2736,1824"); // May need to adjust resolution for your own screen
            WebDriver driver = new ChromeDriver(options);

            driver.manage().window().maximize();
            String baseUrl = "https://libgen.fun/search.php?req=" + search;
            driver.get(baseUrl);

            Set<String> pgLinks = new LinkedHashSet<>();

            try {

                //---- get all the paginator links and nest the loop

                List<WebElement> allPages = driver.findElements(By.cssSelector("div.paginator table a"));

                /*
                Traversing through the list and extracting link addresses
                We add them to the LinkedHashSet to maintain order and strip out the duplicates
                 */
                for(WebElement link:allPages){
                    pgLinks.add(link.getAttribute("href"));
                }

                //Without time to load, we will get a stale element issue
                Thread.sleep(4000);



            } catch (Exception e) {

            }


            //Process the initial landing page
            paginator(driver);


            for(String pgLink: pgLinks){
                System.out.println(pgLink);
                //Loop through each of the remaining pages
                driver.get(pgLink);

                paginator(driver);

            }






            System.out.println("Shutting down the web driver!");
            driver.close();


        } catch (Exception e) {
            System.out.println("An error occurred: " + e);
        }

    }


    public static String switchTab(WebDriver driver, int tab) {

        /*
        We need the window handle and the tab number to effectively transition between the tabs
        without interrupting the loop.
         */
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        String handleName = tabs.get(tab);
        driver.switchTo().window(handleName);
        System.setProperty("current.window.handle", handleName);
        return handleName;
    }


   public void table2(WebDriver driver) {
           /*
            Document the details of the book into the text file.
             */
       try {
           historyFile = new FileWriter("Book_History.txt", true);
       } catch (IOException e) {
           e.printStackTrace();
       }


       List<WebElement> elements = driver.findElements(By.tagName("tr"));
       String temp = "";

       for (WebElement row : elements) {


           List<WebElement> cells = row.findElements(By.tagName("td"));

           for (WebElement cell : cells) {
               temp = cell.getText().trim().replaceAll("\n", "|");

               if (temp.length() == 0) {
                   //This is a blank field
               } else if (temp.length() >  1){
                   //Document the text of each element. It's in a table, so parsing from the txt later by Col[x] will be easy.

                   //System.out.println("Comparing : " + temp + " to " + "(Title|Author\\(s\\)|Publisher|Pages|ISBN|Download|Size)");

                   if (temp.matches("(Title|Author\\(s\\)|Publisher|Pages|ISBN|Download|Size)\\:")) {
                       previous++;
                       System.out.println(previous);

                   } else{
                       writeBookInfo(temp.concat("|"));
                       System.out.println("Book Data 1: " + cell.getText().concat("|"));
                   }


               }

           }

       }
           //We have a full data for this book now, so append a new line to the text file.
           writeBookInfo("\n");



       try {
           historyFile.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   public void paginator(WebDriver driver){
           /*
            We will have to iterate through the table and pull individual elements
             */
       List<WebElement> elements = driver.findElements(By.tagName("tr"));

       try {

           for (WebElement row : elements) {
               List<WebElement> cells = row.findElements(By.tagName("td"));
               if (row.getText().toLowerCase().contains(query.toLowerCase()) && !row.getText().contains("files")){
                   for (WebElement cell : cells) {
                       temp = cell.getText().trim().replaceAll("\n", " - ");

                       if (cell.getText().trim().length() == 0){

                           //One of the data field is blank, so a placeholder will have to do
                           writeBookInfo(temp.concat("No Data|"));
                           //  System.out.println("Book Data 0: " + cell.getText().concat("|"));
                           charCount += cell.getText().length();
                       } else {

                           Thread.sleep(2000);
                           try{
                               if(cell.getText().toLowerCase().contains(query)){
                                   //We found the search key in the book list, so we open this in a new tab.
                                   String clicklnk = Keys.chord(Keys.CONTROL, Keys.ENTER);
                                   cell.findElement(By.cssSelector("a.simplebooktitle")).sendKeys(clicklnk);

                                   //Switch to the new tab
                                   switchTab(driver, 1);
                                   Thread.sleep(5000);
                                   //Go through this new table to get book links.
                                   table2(driver);

                                   //Now we click the next link to get the download page
                                   driver.findElement(By.xpath("//div[@class='serverblock1']/a")).click();
                                   Thread.sleep(5000);
                                   //The file DL is a bit weird, it saves to a buffer, so we fetch the result.
                                   driver.findElement(By.id("download-button")).click();
                                   var waitForDl = new WebDriverWait(driver, Duration.ofMinutes(5));
                                   var clickableElement = waitForDl.until(ExpectedConditions.elementToBeClickable(By.id("save-file")));

                                   clickableElement.click();

                                   //We got what we came for, let's close that tab and get the next one in the loop!
                                   driver.close();
                                   switchTab(driver, 0);

                               }
                           } catch(Exception e){

                           }

                           //Document the text of each element. It's in a table, so parsing from the txt later by Col[x] will be easy.
                           // writeBookInfo(temp.concat("|"));
                           //  System.out.println("Book Data 1: " + cell.getText().concat("|"));
                           charCount += cell.getText().length();
                       }

                   }
               }

               if (charCount > 20){
                   charCount = 0;
               }

           }


       } catch (Exception e) {
           e.getStackTrace();
       }

   }

    public void writeBookInfo(String info){


        //Minor cleanup before writing to the text file
        if (info.contains("torrent")){
            info = info.substring(0, info.length() -2);
            info = info.substring(0, info.lastIndexOf("|"));
        }


                if (previous > 1 || info.equals("\n")) {
                    try {
                        historyFile.write(info);
                        previous = 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
