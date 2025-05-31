package ru.kpfu.desktop.core.testcases;

import io.appium.java_client.windows.WindowsElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.interactions.Actions;
import ru.kpfu.desktop.core.AbstractWindowsAppTest;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.kpfu.desktop.core.helper.FileHelper.createFolder;
import static ru.kpfu.desktop.core.helper.NavigationHelper.getCurrentAbsolutePath;
import static ru.kpfu.desktop.core.helper.NavigationHelper.moveToDirectory;

public class NavigationTest extends AbstractWindowsAppTest {

    @Test
    @DisplayName("Когда нажимаем на папку два раза, мы в нее перемещаемся")
    public void folderName_WhenDoubleClick_ThenWeMustGetIntoIt() {
        // ARRANGE
        String folderName = FOLDER_NAMES[0];
        createFolder(folderName);
        WindowsElement folderElement = driver.findElementByName(folderName);
        String expectedAbsolutePath = getCurrentAbsolutePath() + File.separator + folderName;

        // ACT
        Actions actions = new Actions(driver);
        actions.doubleClick(folderElement).perform();

        // ASSERT
        String actualAbsolutePath = getCurrentAbsolutePath();
        assertThat(actualAbsolutePath).isEqualTo(expectedAbsolutePath);
    }

    @Test
    @DisplayName("Когда переходим в папку через поиск, мы в нее перемещаемся")
    public void folderName_WhenEnterFolderInSearch_ThenWeMustGetIntoIt() {
        // ARRANGE
        String folderName = FOLDER_NAMES[0];
        createFolder(folderName);
        String expectedAbsolutePath = getCurrentAbsolutePath() + File.separator + folderName;

        // ACT
        moveToDirectory(expectedAbsolutePath);

        // ASSERT
        String actualAbsolutePath = getCurrentAbsolutePath();
        assertThat(actualAbsolutePath).isEqualTo(expectedAbsolutePath);
    }

}
