package ru.kpfu.desktop.core.helper;

import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import static ru.kpfu.desktop.core.util.AccessibilityIdUtil.*;

public class NavigationHelper extends BaseHelper {

    public static String getCurrentAbsolutePath() {
        return driver.findElementByAccessibilityId(SEARCH_FIELD_ACCESSIBILITY_ID).getAttribute("Value.Value");
    }

    public static void moveToDirectory(String absolutePath) {
        WindowsElement searchField = driver.findElementByAccessibilityId(SEARCH_FIELD_ACCESSIBILITY_ID);
        perform(actions -> actions
                .click(searchField)
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .sendKeys(absolutePath)
                .sendKeys(Keys.ENTER)
                .sendKeys(Keys.ENTER)
                .perform()
        );
    }
}
