import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Most of the books on OpenLibrary should be public domain and they are freely downloadable.
 * This does not mean that rampant automation is a good idea. Keep in mind this is just a demo!
 */

public class OpenLibrary {


    String temp = "";

    String query = "";

    Utilities ut = new Utilities();

    public void search_OpenLibrary(String search) {

        this.query = search;
        WebDriver driver = ut.EstablishWebDriver();


        try {



            String baseUrl = "https://openlibrary.org/search?q=title%3A+\"" + search + "\"&mode=everything";
            driver.get(baseUrl);

            Set<String> pgLinks = new LinkedHashSet<>();

            try {

                //---- get all the book links and nest the loop

                List<WebElement> allPages = driver.findElements(By.cssSelector("a.results"));
                System.out.println("Initial result list size: " + pgLinks.size());

                /*
                Traversing through the list and extracting link addresses
                We add them to the LinkedHashSet to maintain order and strip out the duplicates
                 */
                for(WebElement link:allPages){
                    //Limit to works

                    if(link.getAttribute("href").contains("works")){
                        pgLinks.add(link.getAttribute("href"));
                        System.out.println(link.getAttribute("href")); //debugging
                    }

                }
                System.out.println("List of relevant pgLinks size: " + pgLinks.size());

            } catch (Exception e) {
                e.printStackTrace();

            }
            /**
             * This will be a fairly similar process to what was done to automate Project Gutenberg
             * but there are no tables, so things will be fairly straight forward.
             */

            for(String pgLink: pgLinks){
                try{
                    System.out.println(pgLink);
                    //Loop through each of the remaining pages
                    Thread.sleep(5000);
                    driver.get(pgLink);

                    linkEater(driver);

                    driver.navigate().back();
                    driver.navigate().back();

                } catch (NoSuchElementException e){
                    //Page did not contain what we are looking for - head back and try again.
                    driver.navigate().back();
                    driver.navigate().back();
                }

            }

            System.out.println("Shutting down the web driver!");
            driver.close();


        } catch (InterruptedException e) {

        }

    }


    public void linkEater(WebDriver driver){

        /**
         * The information for each book is scattered through different through page elements...
         */
        WebElement Title = driver.findElement(By.cssSelector("div.desktop-book-header h1.work-title"));
        System.out.println(Title.getText());

        WebElement Author = driver.findElement(By.cssSelector("div.desktop-book-header h2.edition-byline a"));
        System.out.println(Author.getText());

        List<WebElement> details = driver.findElements(By.cssSelector("div.edition-omniline div.edition-omniline-item"));

        //Cleaning up the details with some regex and string manipulation so everything fits nice in a text document
        String publishDate = "";
        String publisher = "";
        String language = "";
        String pages = "";

        for(WebElement detail:details){

            System.out.println(detail.getText().replace("\n", " "));

            final Matcher m = Pattern.compile("(Publish Date|Publisher|Language|Pages)").matcher(detail.getText().replace("\n", " "));



            if (m.find())
                switch (m.group()) {
                    case "Publish Date": publishDate = detail.getText().replace("Publish Date", ""); break;
                    case "Publisher": publisher = detail.getText().replace("Publisher", ""); break;
                    case "Language": language = detail.getText().replace("Language", ""); break;
                    case "Pages": pages = detail.getText().replace("Pages", ""); break;
                }
        }

        //Write all this out to a file specified for Open Library
        ut.writeInfo("Open_Library_History.txt", Title.getText() + "|" + Author.getText() + "|" + publishDate.trim() + "|" + publisher.trim() + "|" + language.trim() + "|" + pages.trim() + "\n");

        /**
         * Downloading...
         */

        WebElement dlOptions = driver.findElement(By.cssSelector("div.cta-section ul.ebook-download-options"));
        List<WebElement> elements = dlOptions.findElements(By.cssSelector("li a"));

        Set<String> dlLinks = new LinkedHashSet<>();

        //Some pages don't have the necessary elements...


        try {


            /**
             * In the Project Gutenberg automation, Plain Text was an attractive option.
             * Unfortunately, the first few Plain Text links I clicked on from Open Library
             * were, for lack of a better term... horrific. Likely done by an old optical character recognition
             * (OCR) program. Oh, well. I will explore OCR options via Tesseract another time.
             *
             * When I feed these through my NLP research engine, I will just set up a discriminating process
             * to weed out the ones that were done well from the ones that are mostly unintelligible.
             */

            for(WebElement nestedLink:elements){

                dlLinks.add(nestedLink.getAttribute("href"));
                System.out.println(nestedLink.getAttribute("href")); //debugging
                System.out.println(nestedLink.getText()); //debugging


                if (nestedLink.getText().contains("Plain text")){
                    nestedLink.click();
                    Thread.sleep(5000);
                    String fileName = driver.getCurrentUrl().substring(driver.getCurrentUrl().lastIndexOf("/"));
                    fileName = fileName.replace("/", "Open_Library/");

                    System.out.println(fileName);
                    ut.writeInfo(fileName, driver.getPageSource());
                    driver.navigate().back();
                    driver.navigate().back();
                }

                Thread.sleep(2000);



            }
            Thread.sleep(6000);



        } catch (Exception e) {

        }



        //We have written the full data for this book now, so append a new line to the text file.
        //   writeBookInfo("\n");


    }


}
