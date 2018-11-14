package com.mycompany.app;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.noraui.utils.Utilities.OperatingSystem;
import com.github.noraui.utils.Utilities.SystemArchitecture;

/**
 * Unit test for https://stackoverflow.com/questions/53268198/how-to-make-webapp-run-on-travis-ci.
 */
public class AppTest {
	
	/**
     * Specific logger
     */
    private static final Logger logger = LoggerFactory.getLogger(AppTest.class);
	
	private WebDriver webDriver;

	@Before
	public void init() {
		final OperatingSystem currentOperatingSystem = OperatingSystem.getCurrentOperatingSystem();
        String pathWebdriver = String.format("src/test/resources/drivers/%s/googlechrome/%s/chromedriver%s", currentOperatingSystem.getOperatingSystemDir(),
                SystemArchitecture.getCurrentSystemArchitecture().getSystemArchitectureName(), currentOperatingSystem.getSuffixBinary());

        if (!new File(pathWebdriver).setExecutable(true)) {
            logger.error("ERROR when change setExecutable on " + pathWebdriver);
        }
		System.setProperty("webdriver.chrome.driver", pathWebdriver);
		this.webDriver = new ChromeDriver();
	}
	
	@After
	public void quit() {
		this.webDriver.quit();
	}
	
	@Test
	public void read() {
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		webDriver.get("http://localhost:8080/landigger/api/search?mac=cc:f8:e9:aa:29:04");
		logger.info(webDriver.getPageSource());
	}
}
