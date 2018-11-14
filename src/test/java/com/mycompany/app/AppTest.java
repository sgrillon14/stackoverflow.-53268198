package com.mycompany.app;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.noraui.utils.Utilities.OperatingSystem;
import com.github.noraui.utils.Utilities.SystemArchitecture;

/**
 * Unit test for
 * https://stackoverflow.com/questions/53268198/how-to-make-webapp-run-on-travis-ci.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {

	/**
	 * Specific logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(AppTest.class);

	@LocalServerPort
	private int port;

	private WebDriver webDriver;

	@Before
	public void init() {
		final OperatingSystem currentOperatingSystem = OperatingSystem.getCurrentOperatingSystem();
		String pathWebdriver = String.format("src/test/resources/drivers/%s/googlechrome/%s/chromedriver%s",
				currentOperatingSystem.getOperatingSystemDir(),
				SystemArchitecture.getCurrentSystemArchitecture().getSystemArchitectureName(),
				currentOperatingSystem.getSuffixBinary());
		if (!new File(pathWebdriver).setExecutable(true)) {
			logger.error("ERROR when change setExecutable on " + pathWebdriver);
		}
		System.setProperty("webdriver.chrome.driver", pathWebdriver);
	}

	@After
	public void quit() {
		this.webDriver.quit();
	}

	@Test
	public void read() {
		this.webDriver = new ChromeDriver();
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		webDriver.get("http://localhost:" + port + "/app/login");
		logger.info(webDriver.getPageSource());
		assertThat(webDriver.getPageSource()).isEqualTo("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head></head><body>Hello stackoverflow.com questions 53268198</body></html>");
	}
}
