package ru.kpfu.desktop.core.helper;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import javax.annotation.Nullable;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.kpfu.desktop.core.helper.NavigationHelper.getCurrentAbsolutePath;

public class FileHelper extends BaseHelper {

    public static void createFolder(String folderName) {
        String folderPath = getCurrentAbsolutePath() + File.separator + folderName;
        File folder = new File(folderPath);

        folder.mkdir();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(folderName)));
    }

    /**
     * Удаляет все файлы из текущей директории
     */
    public static void deleteAllFiles() {
        List<String> fileNamesInCurrentDirectory = getFileNamesInCurrentDirectory();
        if (fileNamesInCurrentDirectory.isEmpty()) {
            return;
        }

        perform(actions -> {
            actions.keyDown(Keys.CONTROL);
            selectFiles(fileNamesInCurrentDirectory.toArray(new String[] {}));
            actions
                    .keyUp(Keys.CONTROL)
                    .sendKeys(Keys.DELETE)
                    .perform();
        });

        fileNamesInCurrentDirectory
                .forEach(fileName -> wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(fileName))));
    }

    /**
     * Удаляет файл из текущей директории
     *
     * @param fileName имя файла
     * @throws IllegalArgumentException если файл в текущей директории не существует
     */
    public static void deleteFile(String fileName) {
        if (!existsByName(fileName)) {
            throw new IllegalArgumentException("File does not exist: " + fileName);
        }

        perform(actions -> actions
                .click(driver.findElementByName(fileName))
                .sendKeys(Keys.DELETE)
                .perform());
    }

    /**
     * @return список имен файлов в текущей директории
     */
    public static List<String> getFileNamesInCurrentDirectory() {
        String absolutePath = getCurrentAbsolutePath();
        System.out.println("absolutePath: " + absolutePath);
        File folder = new File(absolutePath);
        return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .map(File::getName)
                .collect(Collectors.toList());
    }

    /**
     * Создаёт пустой файл в текущей директории
     *
     * @param name Имя файла
     */
    public static void createFile(String name) throws IOException {
        createFile(name, (byte []) null);
    }

    /**
     * Создаёт файл в текущей директории с указанным текстом
     *
     * @param name Имя файла
     * @param content Содержимое файла
     */
    public static void createFile(String name, String content) throws IOException {
        createFile(name, content.getBytes(StandardCharsets.UTF_8));
    }

    public static void createFile(String name, @Nullable byte[] content) throws IOException {
        String absolutePath = getCurrentAbsolutePath();
        Path absoluteFilePath = Paths.get(absolutePath + File.separator + name);
        File file = absoluteFilePath.toFile();

        if (existsByName(name)) {
            deleteFile(name);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(name)));
        }

        file.createNewFile();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));

        if (content != null) {
            try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                outputStream.write(content);
            }
        }
    }

    public static byte[] getContent(String fileName) throws IOException {
        return getContent(fileName, arr -> arr);
    }

    public static <T> T getContent(String fileName, Function<byte[], T> function) throws IOException {
        return getContent(Paths.get(getCurrentAbsolutePath() + File.separator + fileName), function);
    }

    public static <T> T getContent(Path path, Function<byte[], T> function) throws IOException {
        return function.apply(Files.readAllBytes(path));
    }

    public static boolean existsByName(String name) {
        String absolutePath = getCurrentAbsolutePath();
        Path absoluteFilePath = Paths.get(absolutePath + File.separator + name);

        return absoluteFilePath.toFile().exists();
    }

    public static void selectFiles(String... fileNames) {
        perform(actions -> {
            actions.doubleClick(driver.findElementByAccessibilityId("11")).perform();

            actions.keyDown(Keys.CONTROL);
            Arrays.stream(fileNames)
                    .map(driver::findElementByName)
                    .forEach(actions::click);
            actions.keyUp(Keys.CONTROL)
                    .perform();
        });
    }
}
