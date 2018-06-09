package com.generation_p.hotel_demo.services;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.generation_p.hotel_demo.entity.Hotel;

public class RoomService {
    protected WebDriver driver;
    private WebDriverWait waitDriver;
    protected static final String BASE_URL = "http://localhost:8080";
    private HotelService service = ServiceProvider.getHotelService();

    public void click () throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        driver = new ChromeDriver();
        
        driver.get(BASE_URL);
        waitDriver = new WebDriverWait(driver, 5);
        
        String mainPage = driver.getWindowHandle();
        List<Hotel> allHotels = service.findAll();
        
        String rowPath = "//*[@id='HotelGrid']/div[3]/table/tbody/tr";
        List<WebElement> visibleRows = waitDriver.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(rowPath)));
        
        Actions action = new Actions(driver); 
        for (int i = 0; i < allHotels.size(); i++) {
            int currentRowNumber = i == allHotels.size() - 1 ? visibleRows.size() : i >= visibleRows.size() - 1 ? visibleRows.size() - 1 : i + 1;
            
            WebElement currentRow = driver.findElement(By.xpath(rowPath + "[" + currentRowNumber + "]"));
            String name = currentRow.findElement(By.xpath("./td[1]")).getText();
            
            if (!allHotels.get(i).getName().equals(name)) {
                i--;
                continue;
            }
            
            WebElement link = currentRow.findElement(By.tagName("a"));
            action.moveToElement(link).click();
            action.build().perform();
            
            String hotelPage = waitDriver.until(driver -> {
                Set<String> newWindowsSet = driver.getWindowHandles();
                newWindowsSet.remove(mainPage);
                return newWindowsSet.size() > 0 ? newWindowsSet.iterator().next() : null;
            });
            driver.switchTo().window(hotelPage);
            
            List<WebElement> rooms = driver.findElements(By.xpath("//td[@class='ftd roomType']/div/div/a"));
            if (rooms.isEmpty()) {
                goToPage(mainPage);
                continue;
            }
            
            StringBuilder newRoomTypes = new StringBuilder();
            Iterator<WebElement> iterator = rooms.iterator();
            while (iterator.hasNext()) {
                WebElement currentRoom = iterator.next();
                newRoomTypes.append(currentRoom.getText() + "=");
                
                action.moveToElement(currentRoom).click();
                action.build().perform();

                WebElement image;
                String roomPhoto = "";
                try {
                    image = waitDriver.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                            "//div[@class='hp_rt_lightbox_content']/div[@class='blocktoggle']/div/div[@class='hp-gallery']/div[@class='hp-gallery-slides slick-initialized slick-slider']/div[@class='slick-list draggable']/div[@class='slick-track']/div[@class='slick-slide']/img")));
                    roomPhoto = image.getAttribute("src");
                } catch (TimeoutException e) {
                    roomPhoto = "No Photo";
                }
                
                newRoomTypes.append(roomPhoto);
                if (iterator.hasNext()) newRoomTypes.append(",");
                
                waitDriver.until(ExpectedConditions.presenceOfElementLocated(By.className("lightbox_close_button"))).click();
            }
            
            Hotel currentHotel = allHotels.get(i);
            currentHotel.setRooms(newRoomTypes.toString());
            service.save(currentHotel);
            
            goToPage(mainPage);
        }
        
        driver.quit();
    }
    
    private void goToPage (String mainWindowHandle) {
        driver.close();
        driver.switchTo().window(mainWindowHandle);
    }
}
