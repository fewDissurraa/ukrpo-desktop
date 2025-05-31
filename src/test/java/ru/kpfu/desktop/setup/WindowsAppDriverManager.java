package ru.kpfu.desktop.setup;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

public class WindowsAppDriverManager {

    private static final int port = 4723;
    private static final String URL = "http://localhost:" + port;
    private static final String APP_PATH = "C:/WinRar_Cracked/WinRAR.exe";
    private static final String WIN_APP_DRIVER_PATH = "D:\\WinAppDriver\\WinAppDriver.exe";

    private static final DesiredCapabilities capabilities = new DesiredCapabilities();

    static {
        capabilities.setCapability("app", APP_PATH);
        capabilities.setCapability("deviceName", "WindowsPC");
        capabilities.setPlatform(Platform.WINDOWS);
    }

    private static final WindowsDriver<WindowsElement> driverInstance;

    static {
        try {
            startServer();

            driverInstance = new WindowsDriver<>(URI.create(URL).toURL(), capabilities);
            driverInstance.manage().window().maximize();

            Runtime.getRuntime().addShutdownHook(new Thread(driverInstance::quit));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final WebDriverWait waitInstance = new WebDriverWait(driverInstance, 3);

    public static WindowsDriver<WindowsElement> getDriverInstance() {
        return driverInstance;
    }

    public static WebDriverWait getWaitInstance() {
        return waitInstance;
    }

    private static void startServer() throws IOException {
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", WIN_APP_DRIVER_PATH)
                .redirectOutput(ProcessBuilder.Redirect.DISCARD);

        Process process = builder.start();
        Runtime.getRuntime().addShutdownHook(new Thread(process::destroy));
    }
}