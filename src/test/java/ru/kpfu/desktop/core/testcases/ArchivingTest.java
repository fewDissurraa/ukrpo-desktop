package ru.kpfu.desktop.core.testcases;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.kpfu.desktop.core.AbstractWindowsAppTest;
import ru.kpfu.desktop.setup.model.ArchivingParameters;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;
import static ru.kpfu.desktop.core.helper.ArchiveHelper.makeArchive;
import static ru.kpfu.desktop.core.helper.FileHelper.*;

public class ArchivingTest extends AbstractWindowsAppTest {

    @Test
    @DisplayName("При создании архива без файлов получаем окно с ожидаемым сообщением")
    public void emptyFilesList_WhenMakeArchive_ThenMustAppearWindowWithExpectedMessage() {
        // ARRANGE
        String expectedMessage = "Не выбраны файлы. Нужно отметить файлы для добавления.";
        String archiveName = "archive.rar";
        ArchivingParameters archivingParameters = ArchivingParameters.withDefaults(archiveName);

        // ACT
        makeArchive(archivingParameters);

        // ASSERT
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(expectedMessage)));
        driver.findElementByAccessibilityId("1").click();
    }

    @Test
    @DisplayName("При создания архива с определенным именем должен появится архив с этим же именем")
    public void archiveName_WhenMakeArchive_ThenMustCreateArchiveWithSpecifiedName() throws IOException {
        // ARRANGE
        String archiveName = "archive.rar";
        String fileName = FILE_NAMES[0];
        createFile(fileName);
        ArchivingParameters archivingParameters = ArchivingParameters.withDefaults(archiveName);

        // ACT
        makeArchive(archivingParameters, fileName);

        // ASSERT
        assertThat(existsByName(archiveName)).isTrue();
    }

    @Test
    @DisplayName("При создании архива с файлом файл должен быть внутри")
    public void file_WhenMakeArchive_ThenMustCreateArchiveWithSpecifiedFile() throws IOException {
        // ARRANGE
        String archiveName = "archive.rar";
        String fileName = FILE_NAMES[0];
        createFile(fileName);
        ArchivingParameters archivingParameters = ArchivingParameters.withDefaults(archiveName);

        // ACT
        makeArchive(archivingParameters, fileName);

        // ASSERT
        Actions actions = new Actions(driver);
        actions.doubleClick(driver.findElementByName(archiveName)).perform();
        assertThatCode(() -> driver.findElementByName(fileName)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("При создании архива с файлом файл должен быть внутри с тем же содержанием")
    public void fileWithContent_WhenMakeArchive_ThenMustCreateArchiveWithSpecifiedContent() throws IOException {
        // ARRANGE
        String archiveName = "archive.rar";
        String fileName = FILE_NAMES[0];
        String expectedContent = "This is text!!!!";
        createFile(fileName, expectedContent);
        ArchivingParameters archivingParameters = ArchivingParameters.withDefaults(archiveName);

        // ACT
        makeArchive(archivingParameters, fileName);

        // ASSERT
        String archiveAbsolutePath = WORK_DIRECTORY + File.separator + archiveName;
        String archiveContent = getContent(
                Paths.get(archiveAbsolutePath), arr -> new String(arr, StandardCharsets.UTF_8));
        Assertions.assertThat(archiveContent).contains(expectedContent);
    }

    @Test
    @DisplayName("При создании архива с файлом и с паролем файл должен запрашивать пароль")
    public void fileAndPassword_WhenMakeArchive_ThenMustCreateArchiveWithSpecifiedPassword() throws IOException {
        // ARRANGE
        String archiveName = "archive.rar";
        String fileName = FILE_NAMES[0];
        String password = "password";
        String expectedWindowName = "Ввод пароля";
        createFile(fileName);
        ArchivingParameters archivingParameters = ArchivingParameters.builder()
                .name(archiveName)
                .password(password)
                .build();

        // ACT
        makeArchive(archivingParameters, fileName);

        // ASSERT
        perform(actions -> {
            actions.doubleClick(driver.findElementByName(archiveName)).perform();
            actions.doubleClick(driver.findElementByName(fileName + " *")).perform();
            assertDoesNotThrow(
                    () -> driver.findElementByName(expectedWindowName), () -> actions.sendKeys(Keys.ESCAPE).perform());
        });
    }

    @Test
    @DisplayName("При вводе неправильного пароля получаем сообщение об ошибке")
    public void invalidPassword_WhenEnterInvalidPassword_ThenErrorMessageMustAppear() throws IOException {
        // ARRANGE
        String archiveName = "archive.rar";
        String fileName = FILE_NAMES[0];
        String password = "password";
        String invalidPassword = "Курлык";
        String expectedMessage = "Неверный пароль для " + fileName;
        createFile(fileName);
        ArchivingParameters archivingParameters = ArchivingParameters.builder()
                .name(archiveName)
                .password(password)
                .build();

        // ACT
        makeArchive(archivingParameters, fileName);

        // ASSERT
        perform(actions -> {
            actions.doubleClick(driver.findElementByName(archiveName)).perform();
            actions.doubleClick(driver.findElementByName(fileName + " *")).perform();
            actions.sendKeys(invalidPassword).sendKeys(Keys.ENTER).perform();
            assertDoesNotThrow(
                    () -> driver.findElementByName(expectedMessage),
                    () -> actions.sendKeys(Keys.ENTER).sendKeys(Keys.ESCAPE).perform()
            );
        });
    }

    @Test
    @DisplayName("Когда создаем архив с параметром удаления файла, файлы должны быть удалены")
    public void deleteFilesAfterArchivingIsTrue_WhenMakeArchive_ThenMustDeleteFiles() throws IOException {
        String archiveName = "archive.rar";
        for (String fileName : FILE_NAMES) {
            createFile(fileName);
        }
        ArchivingParameters archivingParameters = ArchivingParameters.builder()
                .name(archiveName)
                .deleteFilesAfterArchiving(true)
                .build();

        // ACT
        makeArchive(archivingParameters, FILE_NAMES);

        // ASSERT
        for (String fileName : FILE_NAMES) {
            Assertions.assertThat(existsByName(fileName)).isFalse();
        }
    }

    @Test
    @DisplayName("При создании архива с паролем и с файлом содержание должно быть зашифровано")
    public void passwordAndFileWithContent_WhenMakeArchive_ThenContentMustBeEncrypted() throws IOException {
        // ARRANGE
        String archiveName = "archive.rar";
        String fileName = FILE_NAMES[0];
        String password = "password";
        String expectedContent = "This is text!!!!";
        createFile(fileName, expectedContent);
        ArchivingParameters archivingParameters = ArchivingParameters.builder()
                .name(archiveName)
                .password(password)
                .build();

        // ACT
        makeArchive(archivingParameters, fileName);

        // ASSERT
        String archiveAbsolutePath = WORK_DIRECTORY + File.separator + archiveName;
        String archiveContent = getContent(
                Paths.get(archiveAbsolutePath), arr -> new String(arr, StandardCharsets.UTF_8));
        System.out.println(archiveContent);
        Assertions.assertThat(archiveContent).doesNotContain(expectedContent);
    }

}
