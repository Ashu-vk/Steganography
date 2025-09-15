package com.stego;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonStructureAnalyzer {

	public static void main(String[] args) {
		String jsonFilePath = "D:\\steganography\\Xero_Contacts_Prod.json"; // Change this to your JSON file path

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

			// If the root is an array, analyze all records
			if (rootNode.isArray()) {
				Map<String, JsonNode> mergedStructure = new LinkedHashMap<>();
				for (JsonNode record : rootNode) {
					analyzeJsonStructure(record, "", mergedStructure);
				}

				// Print the complete object structure
				System.out.println("🔍 **Complete JSON Structure**\n");
				visualizeJsonStructure(mergedStructure, 0);
			} else {
				System.out.println("Error: JSON root is not an array.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void analyzeJsonStructure(JsonNode node, String parentKey, Map<String, JsonNode> mergedStructure) {
		if (node.isObject()) {
			node.fieldNames().forEachRemaining(fieldName -> {
				String fullKey = parentKey.isEmpty() ? fieldName : parentKey + "." + fieldName;
				JsonNode childNode = node.get(fieldName);

				// Merge structures by keeping track of the most detailed format
				mergedStructure.putIfAbsent(fullKey, childNode);

				if (childNode.isObject() || childNode.isArray()) {
					analyzeJsonStructure(childNode, fullKey, mergedStructure);
				}
			});
		} else if (node.isArray()) {
			for (JsonNode element : node) {
				analyzeJsonStructure(element, parentKey, mergedStructure);
			}
		}
	}

	private static void visualizeJsonStructure(Map<String, JsonNode> mergedStructure, int level) {
		for (Map.Entry<String, JsonNode> entry : mergedStructure.entrySet()) {
			String key = entry.getKey();
			JsonNode value = entry.getValue();
			printIndented(key + ": " + getType(value), level);
		}
	}

	private static void printIndented(String text, int level) {
		System.out.println("  ".repeat(level) + "• " + text);
	}

	private static String getType(JsonNode node) {
		if (node.isObject())
			return "Object";
		if (node.isArray())
			return "Array";
		if (node.isTextual())
			return "String";
		if (node.isNumber())
			return "Number";
		if (node.isBoolean())
			return "Boolean";
		return "Unknown";
	}
}
