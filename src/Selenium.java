import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

@RunWith(Parameterized.class)
public class Selenium {
  private StringBuffer verificationErrors = new StringBuffer();

  private WebDriver driver;
  private String baseUrl;
  private String number;
  private String password;
  private String excepted;

  public Selenium(String number, String password, String excepted){
    this.number = number;
    this.password = password;
    this.excepted = excepted;
  }

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://121.193.130.195:8080/";
    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
  }

  @Test
  public void testSelenium() throws Exception {
    driver.get(baseUrl);
    driver.findElement(By.id("name")).clear();
    driver.findElement(By.id("name")).sendKeys(this.number);
    driver.findElement(By.id("pwd")).clear();
    driver.findElement(By.id("pwd")).sendKeys(this.password);
    driver.findElement(By.id("submit")).click();

    String result = driver.findElement(By.id("resultString")).getAttribute("innerHTML").trim();
    assertEquals(this.excepted, result);
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  @Parameterized.Parameters
  public static Collection<Object[]> testData() throws FileNotFoundException, IOException{
    String inputFilePath = new File(System.getProperty("user.dir")).getParent();
    File inputFile = new File(inputFilePath + "/inputgit.csv");
    Collection<Object[]> list = new ArrayList<Object[]>();

    if(inputFile.exists()){
      System.out.println("ok");
      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "GBK"));
      reader.readLine();

      while(true){
        String str = reader.readLine();
        if(str == null) break;

        String[] strList = str.split(",");
        String number = strList[0];
        String password = number.substring(4);
        strList[0] = strList[1];
        strList[1] = number;
        String expected = String.join(",", strList);
        list.add(new String[]{number, password, expected});
      }
    }
    return list;
  }
}
