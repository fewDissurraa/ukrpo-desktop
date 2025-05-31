package ru.kpfu.desktop.core.helper;

import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.Keys;
import ru.kpfu.desktop.setup.model.ArchivingParameters;

import javax.annotation.Nullable;

import static ru.kpfu.desktop.core.helper.FileHelper.selectFiles;

public class ArchiveHelper extends BaseHelper {

    public static void makeArchive(ArchivingParameters archivingParameters, String... fileNames) {
        selectFiles(fileNames);

        driver.findElementByName("Добавить").click();
        setParameters(archivingParameters);
        driver.findElementByName("ОК").click();
    }

    private static void setParameters(ArchivingParameters archivingParameters) {
        setArchiveName(archivingParameters.name());
        setDeleteFilesAfterArchivingFlag(archivingParameters.deleteFilesAfterArchiving());
        setPassword(archivingParameters.password());
    }

    private static void setDeleteFilesAfterArchivingFlag(boolean flag) {
        if (flag) {
            driver.findElementByName("Удалить файлы после архивации").click();
        }
    }

    private static void setPassword(@Nullable String password) {
        if (password == null) {
            return;
        }

        perform(actions -> actions
                .click(driver.findElementByName("Установить пароль..."))
                .sendKeys(password)
                .sendKeys(Keys.TAB)
                .sendKeys(password)
                .sendKeys(Keys.ENTER)
                .perform());
    }

    private static void setArchiveName(String archiveName) {
        WindowsElement nameField = driver.findElementByAccessibilityId("1001");
        nameField.clear();

        perform(actions -> actions
                .click(nameField)
                .sendKeys(archiveName)
                .perform());
    }
}
