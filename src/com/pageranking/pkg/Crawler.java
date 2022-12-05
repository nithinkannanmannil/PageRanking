package com.pageranking.pkg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Crawler {
	private static final Logger logger = LogManager.getLogger(Crawler.class);
	private static final Logger exceptionLogger = LogManager.getLogger("exceptions.pageranking");
	private static Set<String> allLinks = new HashSet<String>();
	private static Map<String, Node> allNodeMap = new HashMap<String, Node>();
	private static String baseUrl = null;

	public static Map<String, Node> crawl(final String baseUrl) {
		logger.debug("Base URL: " + baseUrl);
		allLinks.add(baseUrl);
		allNodeMap.put(baseUrl, new Node(baseUrl));

		Crawler.baseUrl = baseUrl;

		traversePages(baseUrl);

		return allNodeMap;
	}

	private static Node getNode(String url) {
		return allNodeMap.get(url);
	}

	private static void traversePages(String parentLink) {
		Node parentNode = getNode(parentLink);

		if (parentNode == null) {
			parentNode = new Node(parentLink);
		}

		for (String childLink : runSelenium(parentLink)) {
			if (!Util.isEmpty(childLink)) {
				childLink = childLink.trim();
				if (childLink.equals(parentLink) || childLink.substring(0, (childLink.length() - 1)).equals(parentLink)
						|| childLink.substring(0, (childLink.length() - "#main-content".length())).equals(parentLink)
						|| childLink.substring(0, (childLink.length() - "#main-navigation".length()))
								.equals(parentLink)) {
					continue;
				}
			} else {
				continue;
			}

			final String pattern1 = "^" + baseUrl + "[a-zA-Z#].+";

			if (childLink.matches(pattern1)) {
				Node childNode;

				if (!allLinks.contains(childLink)) {
					if (childLink.matches(".+#$")) {
						String str = childLink.substring(0, (childLink.length() - 1));
						if (allLinks.contains(str)) {
							childNode = getNode(str);
							childNode.getInboundNodes().add(parentNode);
							parentNode.getOutboundNodes().add(childNode);
							continue;
						} else {
							childLink = str;
						}
					}

					if (childLink.matches(".+#main-content$")) {
						String str = childLink.substring(0, (childLink.length() - "#main-content".length()));
						if (allLinks.contains(str)) {
							childNode = getNode(str);
							childNode.getInboundNodes().add(parentNode);
							parentNode.getOutboundNodes().add(childNode);
							continue;
						} else {
							childLink = str;
						}
					}

					if (childLink.matches(".+#main-navigation$")) {
						String str = childLink.substring(0, (childLink.length() - "#main-navigation".length()));
						if (allLinks.contains(str)) {
							childNode = getNode(str);
							childNode.getInboundNodes().add(parentNode);
							parentNode.getOutboundNodes().add(childNode);
							continue;
						} else {
							childLink = str;
						}
					}

					allLinks.add(childLink);
					childNode = new Node(childLink);
					childNode.getInboundNodes().add(parentNode);
					parentNode.getOutboundNodes().add(childNode);
					allNodeMap.put(childLink, childNode);
					traversePages(childLink);
				} else {
					childNode = getNode(childLink);
					childNode.getInboundNodes().add(parentNode);
					parentNode.getOutboundNodes().add(childNode);
				}
			}
		}
	}

	private static Set<String> runSelenium(String url) {
		logger.debug("Traversing.........." + url);
		System.out.println("Traversing.........." + url);

		Set<String> linkSet = new HashSet<String>();

		// Setting up Proxy for chrome
		ChromeOptions opts = new ChromeOptions();
		List<String> optsArgs = new ArrayList<String>();
		optsArgs.add("':authority: www.gatech.edu'");
		optsArgs.add("':method: GET'");
		optsArgs.add("':scheme: https'");
		optsArgs.add(
				"'accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9'");
		optsArgs.add("'accept-encoding: gzip, deflate, br'");
		optsArgs.add("'accept-language: en-US,en;q=0.9'");
		optsArgs.add("'cache-control: max-age=0'");
		optsArgs.add("'if-modified-since: Sun, 20 Nov 2022 22:18:08 GMT'");
		optsArgs.add("'if-none-match: \"1668982688\"'");
		optsArgs.add("'sec-ch-ua: \"Google Chrome\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"'");
		optsArgs.add("'sec-ch-ua-mobile: ?0'");
		optsArgs.add("'sec-ch-ua-platform: \"Windows\"'");
		optsArgs.add("'sec-fetch-dest: document'");
		optsArgs.add("'sec-fetch-mode: navigate'");
		optsArgs.add("'sec-fetch-site: none'");
		optsArgs.add("'sec-fetch-user: ?1'");
		optsArgs.add("'upgrade-insecure-requests: 1'");
		optsArgs.add(
				"'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36'");
		optsArgs.add("--no-sandbox");
		optsArgs.add("--headless");
		optsArgs.add("--disable-gpu");
		optsArgs.add("--allow-running-insecure-content");

		opts.addArguments(optsArgs);

		System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
		WebDriver driver = new ChromeDriver(opts);
		try {

			driver.get(url);
			List<WebElement> links = driver.findElements(By.tagName("a"));

			for (WebElement link : links) {
				linkSet.add(link.getAttribute("href"));
			}
			logger.debug("Outgoing links of " + url);
			System.out.println("Outgoing links of " + url);
			logger.debug(linkSet);
			System.out.println(linkSet);

		} catch (Exception ex) {
			ex.printStackTrace();
			exceptionLogger.error(linkSet);
			System.err.println(linkSet);
		} finally {
			if (driver != null) {
				driver.quit();
			}
		}

		return linkSet;

	}

}
