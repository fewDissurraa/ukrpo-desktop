package ru.kpfu.desktop.core.helper;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.kpfu.desktop.setup.WindowsAppDriverManager;

import java.util.function.Consumer;

public abstract class BaseHelper {

    protected static final WindowsDriver<WindowsElement> driver = WindowsAppDriverManager.getDriverInstance();

    protected static final WebDriverWait wait = WindowsAppDriverManager.getWaitInstance();

    protected static void perform(Consumer<Actions> actionBuilder) {
        actionBuilder.accept(new Actions(driver));
    }
}
