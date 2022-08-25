import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Utilities {

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

    public WebDriver EstablishWebDriver(){
        //Establish the WebDriver
        System.setProperty("webdriver.chrome.driver","C:\\Users\\etern\\IdeaProjects\\libgen_Automation\\src\\main\\resources\\chromedriver_win32\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless", "--window-size=2736,1824"); - Headless just hides the browser - no real need for that here
        options.addArguments("--window-size=2736,1824"); // May need to adjust resolution for your own screen
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        return driver;
    }


    public void writeInfo(String file, String info){


        /**
         * I made a version of this in libgen but I added some of the processing to it as I wasn't expecting
         * to add more processes that might use it. As I am now adding Project GutenBerg, I will start generalizing
         * utilities in the event I continue on an do more like this.
         */

        try {
            FileWriter usefulData = new FileWriter(file, true);
            usefulData.write(info);
            usefulData.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




}






}
