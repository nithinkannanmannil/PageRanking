package com.pageranking.pkg;

import java.text.DecimalFormat;

public class Util {
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().isEmpty() || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	public static double roundBy2DecimalPlaces(double val) {
		DecimalFormat f = new DecimalFormat("##.000000");
		return Double.parseDouble(f.format(val));
	}

	public static Node[] bubbleSort(Node[] nodeArray) {
		int n = nodeArray.length;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				Node node1 = nodeArray[j];
				Node node2 = nodeArray[j + 1];

				double pageRank1 = node1.getPageRankMap().get(node1.getPageRankMap().size() - 1);
				double pageRank2 = node2.getPageRankMap().get(node2.getPageRankMap().size() - 1);

				if (pageRank1 > pageRank2) {
					Node temp = nodeArray[j];
					nodeArray[j] = nodeArray[j + 1];
					nodeArray[j + 1] = temp;
				}
			}
		}
		return nodeArray;
	}
}
