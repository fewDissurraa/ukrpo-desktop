package ru.kpfu.desktop.core;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThatCode;
import static ru.kpfu.desktop.core.helper.FileHelper.*;
import static ru.kpfu.desktop.core.helper.NavigationHelper.moveToDirectory;
import static ru.kpfu.desktop.setup.WindowsAppDriverManager.getDriverInstance;
import static ru.kpfu.desktop.setup.WindowsAppDriverManager.getWaitInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractWindowsAppTest {

    protected static final String WORK_DIRECTORY = "D:\\Temp";

    protected static final String[] FILE_NAMES = { "a.txt", "b.txt", "c.txt" };

    protected static final String[] FOLDER_NAMES = { "a", "b", "c" };

    protected static WindowsDriver<WindowsElement> driver;

    protected static WebDriverWait wait;

    @BeforeAll
    public static void beforeAll() {
        driver = getDriverInstance();
        wait = getWaitInstance();
    }

    @BeforeEach
    public void setUp() throws IOException {
        moveToDirectory(WORK_DIRECTORY);
    }

    @AfterEach
    public void clearUp() throws IOException {
        moveToDirectory(WORK_DIRECTORY);
        deleteAllFiles();
    }

    protected static void assertDoesNotThrow(ThrowableAssert.ThrowingCallable throwingCallable) {
        assertDoesNotThrow(throwingCallable, () -> { });
    }

    protected static void assertDoesNotThrow(
            ThrowableAssert.ThrowingCallable throwingCallable, Runnable onFinally) {

        try {
            assertThatCode(throwingCallable).doesNotThrowAnyException();
        } finally {
            onFinally.run();
        }
    }

    protected static void perform(Consumer<Actions> actionBuilder) {
        actionBuilder.accept(new Actions(driver));
    }

}
