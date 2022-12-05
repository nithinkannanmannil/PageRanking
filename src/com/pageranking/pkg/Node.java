package com.pageranking.pkg;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Node {
	private String nodeUrl;
	private Set<Node> inboundNodes;
	private Set<Node> outboundNodes;
	private Map<Integer, Double> pageRankMap;

	public Node(String nodeUrl) {
		this.nodeUrl = nodeUrl;
		inboundNodes = new HashSet<Node>();
		outboundNodes = new HashSet<Node>();
		pageRankMap = new LinkedHashMap<Integer, Double>();
	}

	public String getNodeUrl() {
		return nodeUrl;
	}

	public void setNodeUrl(String nodeUrl) {
		this.nodeUrl = nodeUrl;
	}

	public Map<Integer, Double> getPageRankMap() {
		return pageRankMap;
	}

	public void setPageRankMap(Map<Integer, Double> pageRankMap) {
		this.pageRankMap = pageRankMap;
	}

	public Set<Node> getInboundNodes() {
		return inboundNodes;
	}

	public void setInboundNodes(Set<Node> inboundNodes) {
		this.inboundNodes = inboundNodes;
	}

	public Set<Node> getOutboundNodes() {
		return outboundNodes;
	}

	public void setOutboundNodes(Set<Node> outboundNodes) {
		this.outboundNodes = outboundNodes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeUrl == null) ? 0 : nodeUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (nodeUrl == null) {
			if (other.nodeUrl != null)
				return false;
		} else if (!nodeUrl.equals(other.nodeUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Node [nodeUrl=" + nodeUrl + "]";
	}

	public void removeInboundNode(String str) {
		Node removeNode = null;
		for (Node node : inboundNodes) {
			if (node.nodeUrl.equals(str)) {
				removeNode = node;
				break;
			}
		}
		if (removeNode != null) {
			this.inboundNodes.remove(removeNode);
		}
	}

	public void removeOutboundNode(String str) {
		Node removeNode = null;
		for (Node node : outboundNodes) {
			if (node.nodeUrl.equals(str)) {
				removeNode = node;
				break;
			}
		}
		if (removeNode != null) {
			this.outboundNodes.remove(removeNode);
		}
	}

}
