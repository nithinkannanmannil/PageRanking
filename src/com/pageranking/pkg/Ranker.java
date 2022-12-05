package com.pageranking.pkg;

import java.util.Map;

public class Ranker {

	public static void performRanking(Map<String, Node> webGraph) {
		final double DAMPENING_FACTOR = 0.85;
		int iterationCount = 0;
		double initialPageRank = 1.0 / webGraph.size();
		double avgDiff = 0.0;
		while (avgDiff < 99.999999) {
			avgDiff = 0.0;
			for (Map.Entry<String, Node> me : webGraph.entrySet()) {
				Node node = me.getValue();
				double totalPageRank = 0.0;
				for (Node inboundNode : node.getInboundNodes()) {
					int noOfOutboundNodes = inboundNode.getOutboundNodes().size();
					double nodePageRank;
					if (iterationCount == 0) {
						nodePageRank = initialPageRank;
					} else {
						nodePageRank = inboundNode.getPageRankMap().get(iterationCount);
					}
					nodePageRank = nodePageRank / noOfOutboundNodes;
					totalPageRank += nodePageRank;
				}
				totalPageRank = (1 - DAMPENING_FACTOR) + (DAMPENING_FACTOR * totalPageRank);
				if (iterationCount == 0) {
					node.getPageRankMap().put((iterationCount), initialPageRank);
				}
				node.getPageRankMap().put((iterationCount + 1), totalPageRank);

				double previousPageRank;
				if (iterationCount == 0) {
					previousPageRank = initialPageRank;
				} else {
					previousPageRank = node.getPageRankMap().get(iterationCount);
				}

				double diff;
				if (totalPageRank == previousPageRank) {
					diff = 0;
				} else {
					diff = Math.abs(totalPageRank - previousPageRank) / ((totalPageRank + previousPageRank) / 2);
				}
				diff = diff * 100;
				diff = 100 - diff;
				avgDiff += diff;
			}
			avgDiff = (avgDiff / webGraph.size());
			iterationCount++;
		}
	}
}
