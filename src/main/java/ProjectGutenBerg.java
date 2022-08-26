/**
 * Project Gutenberg is all public domain and you can download indiscriminately from here.
 * That said, automation is often frowned upon and you still need to pay attention to the
 * robots.txt and the terms of the site. This code is intended only for educational & demonstration purposes.
 * Use with discernment at your own risk and try not to strain the servers too much!
 */


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ProjectGutenBerg {


    String temp = "";

    String query = "";

    Utilities ut = new Utilities();

    public void search_ProjectGutenberg(String search) {

        this.query = search;



        try {

            WebDriver driver = ut.EstablishWebDriver();

            String baseUrl = "https://www.gutenberg.org/ebooks/search/?query=" + search + "&submit_search=Go%21";
            driver.get(baseUrl);

            Set<String> pgLinks = new LinkedHashSet<>();

            try {

                //---- get all the book links and nest the loop

                List<WebElement> allPages = driver.findElements(By.cssSelector("li.booklink a"));
                System.out.println("List of links size: " + allPages.size());

                /*
                Traversing through the list and extracting link addresses
                We add them to the LinkedHashSet to maintain order and strip out the duplicates
                 */
                for(WebElement link:allPages){
                    pgLinks.add(link.getAttribute("href"));
                    System.out.println(link.getAttribute("href")); //debugging

                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            /**
             * Instead of working with tabs on this one, we will just use the original tab and leverage the back button.
             */

            for(String pgLink: pgLinks){
                System.out.println(pgLink);
                //Loop through each of the remaining pages
                driver.get(pgLink);
                linkEater(driver);
                driver.navigate().back();
                driver.navigate().back();
            }

            System.out.println("Shutting down the web driver!");
            driver.close();


        } catch (Exception e) {
            System.out.println("An error occurred: " + e);
        }

    }


    public void linkEater(WebDriver driver){

        /**
         * There are two tables on this page and we need to process one for links and the other for information
         * so we will have to process them individually.
         */

        try {

            //Get the data first, then download the file...
            table2(driver);
            table1(driver);


            Thread.sleep(5000);


        } catch (Exception e) {

        }



        //We have written the full data for this book now, so append a new line to the text file.
     //   writeBookInfo("\n");


    }

    public void table1(WebDriver driver){

        WebElement tbl = driver.findElement(By.cssSelector("table.files"));

        List<WebElement> elements = tbl.findElements(By.tagName("tr"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (WebElement row : elements) {


            List<WebElement> cells = row.findElements(By.tagName("td"));

            for (WebElement cell : cells) {
                temp = cell.getText();

                    if (temp.contains("Plain Text")) {
                        System.out.println("Found the file link " + cell.findElement(By.tagName("a")).getText());
                        cell.findElement(By.tagName("a")).click();
                        try {
                            //Give it a chance to download before moving on
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        String fileName = driver.getCurrentUrl().substring(driver.getCurrentUrl().lastIndexOf("/"));
                        fileName = fileName.replace("/", "Project_Gutenberg/");

                        System.out.println(fileName);
                        ut.writeInfo(fileName, driver.getPageSource());

                    }

            }

        }
    }

    public void table2(WebDriver driver){

        WebElement tbl = driver.findElement(By.cssSelector("table.bibrec"));

        List<WebElement> elements = tbl.findElements(By.tagName("tr"));

        try {

           String temp = "";

        for (WebElement row : elements) {

                    if (row.getText().contains("Title") || row.getText().contains("Author")) {

                        System.out.println("Book Data1: " + row.getText().concat("|"));
                        temp = temp + (row.getText().replace("\n", " ~ ").replace
                                ("Title ", "").replace("Author ", "").concat("|"));
                    }

                }

                if (!temp.isBlank()){
                    ut.writeInfo("Project_Gutenberg_History.txt", temp + "\n");
                    temp = "";
                }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
