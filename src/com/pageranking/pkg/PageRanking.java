package com.pageranking.pkg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PageRanking {

	private static final Logger logger = LogManager.getLogger(PageRanking.class);

	public static void main(String[] args) throws IOException {

		final String BASE_URL = args[0].trim();

		logger.debug("Started Crawling..........");

		Map<String, Node> allNodeWebGraph = Crawler.crawl(BASE_URL);

		logger.debug("Completed Crawling..........");

		logger.debug("Started PageRank..........");

		Ranker.performRanking(allNodeWebGraph);

		Node[] nodeArr = Util.bubbleSort(
				Arrays.copyOf(allNodeWebGraph.values().toArray(), allNodeWebGraph.values().size(), Node[].class));
		logger.debug("Completed PageRank..........");

		int iterationCount = 0;

		while (iterationCount < nodeArr[0].getPageRankMap().size() - 1) {
			double totalPageRank = 0.0;
			for (Map.Entry<String, Node> me : allNodeWebGraph.entrySet()) {
				Node node = me.getValue();
				totalPageRank += (node.getPageRankMap().get(iterationCount));
			}
			System.out.println(totalPageRank);
			iterationCount++;
		}

		logger.debug("PageRank Results..........");

		BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"));

		for (int i = (nodeArr.length - 1); i >= (nodeArr.length - 5); i--) {
			Node node = nodeArr[i];
			logger.debug(node.getNodeUrl());
			writer.write(node.getNodeUrl());
			writer.newLine();
		}

		writer.close();

	}
}
