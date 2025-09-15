package com.stego;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class XeroContactJsonToExcelConverter {
	public static void main(String[] args) {
		String jsonFilePath = "D:\\steganography\\Xero_Contacts_Prod.json"; // Path to your JSON file
		String excelFilePath = "D:\\steganography\\Xero_Contacts_Prod.xlsx"; // Output Excel file
//		String jsonFilePath = "credit_notes.json"; // Path to your JSON file
//		String excelFilePath = "credit_notes.xlsx"; // Output Excel file

		writeExcel(jsonFilePath, excelFilePath);
	}

	private static void writeExcel(String jsonFilePath, String excelFilePath) {
		try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {

//			Sheet sheet = workbook.createSheet("CreditNotes");
			Sheet sheetPayments = workbook.createSheet("BankTransactions");

			// Create date cell style
			CellStyle dateCellStyle = workbook.createCellStyle();
			CreationHelper createHelper = workbook.getCreationHelper();
			dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode contacts = objectMapper.readTree(new File(jsonFilePath));
//			JsonNode creditNotes = objectMapper.readTree(new File(jsonFilePath));

			int rowNum = 0;

			// Header row
			Row headerRow = sheetPayments.createRow(rowNum++);
			String[] headers = { "ContactID","Name", "ContactStatus"
//					"Balances.AccountsReceivable.Outstanding", "Balances.AccountsReceivable.Overdue", "Balances.AccountsPayable.Outstanding",
//					"Balances.AccountsPayable.Overdue"
					};
//			String[] creditNoteHeaders = { "CreditNoteID", "CreditNoteNumber", "HasErrors", "CurrencyRate", "Type", "Reference",
//					"RemainingCredit", "Date", "Status", "Total", "SubTotal", "TotalTax", "CurrencyCode", "Allocations",
//					"Payments", "Contact", "InvoiceAddresses", "LineItems" };

			for (int i = 0; i < headers.length; i++) {
				headerRow.createCell(i).setCellValue(headers[i]);
			}

			// Iterate through JSON array
			for (JsonNode contact : contacts) {
				Row row = sheetPayments.createRow(rowNum++);

				row.createCell(0).setCellValue(getJsonValue(contact, "ContactID"));
				row.createCell(1).setCellValue(getJsonValue(contact, "Name"));
				row.createCell(2).setCellValue(getJsonValue(contact, "ContactStatus"));
//				row.createCell(6).setCellValue(getJsonNodeValue(contact.get("Contact").get("AccountsReceivable"), "Outstanding"));
//				row.createCell(7).setCellValue(getJsonNodeValue(contact.get("Contact").get("AccountsReceivable"), "Overdue"));
//				row.createCell(8).setCellValue(getJsonNodeValue(contact.get("Contact").get("AccountsPayable"), "Outstanding"));
//				row.createCell(9).setCellValue(getJsonNodeValue(contact.get("Contact").get("AccountsPayable"), "Overdue"));

			}

//			for (JsonNode creditNote : creditNotes) {
//				Row row = sheet.createRow(rowNum++);
//				
//				row.createCell(0).setCellValue(getJsonValue(creditNote, "CreditNoteID"));
//				row.createCell(1).setCellValue(getJsonValue(creditNote, "CreditNoteNumber"));
//				row.createCell(2).setCellValue(getJsonValue(creditNote, "HasErrors"));
//				row.createCell(3).setCellValue(getJsonValue(creditNote, "CurrencyRate"));
//				row.createCell(4).setCellValue(getJsonValue(creditNote, "Type"));
//				row.createCell(5).setCellValue(getJsonValue(creditNote, "Reference"));
//				row.createCell(6).setCellValue(getJsonValue(creditNote, "RemainingCredit"));
//				Cell dateCell = row.createCell(7);
//				dateCell.setCellValue(getJsonValue(creditNote, "DateString"));
//				dateCell.setCellStyle(dateCellStyle);
//				
//				row.createCell(8).setCellValue(getJsonValue(creditNote, "Status"));
//				row.createCell(9).setCellValue(getJsonValue(creditNote, "Total"));
//				row.createCell(10).setCellValue(getJsonValue(creditNote, "SubTotal"));
//				row.createCell(11).setCellValue(getJsonValue(creditNote, "TotalTax"));
//				row.createCell(12).setCellValue(getJsonValue(creditNote, "CurrencyCode"));
//				
//				// Convert nested objects to JSON strings
//				row.createCell(13).setCellValue(getInvoiceNumbers(creditNote.get("Allocations")));
//				
//				row.createCell(14).setCellValue(safeJson(creditNote.get("Payments")));
//				row.createCell(15).setCellValue(getContactName(creditNote.get("Contact")));
//				row.createCell(16).setCellValue(safeJson(creditNote.get("InvoiceAddresses")));
//				row.createCell(17).setCellValue(safeJson(creditNote.get("LineItems")));
//			}

			workbook.write(fileOut);
			System.out.println("✅ Excel file created successfully!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getJsonNodeValue(JsonNode jsNode, String key) {
		if (jsNode == null) {
			return "";
		}

		if (jsNode != null) {
			return Objects.nonNull(jsNode.get(key)) ? jsNode.get(key).asText() : null;
		}

		return "";
	}

	private static String getInvoiceContactName(JsonNode invoiceNode) {
		if (invoiceNode == null) {
			return "";
		}

		if (invoiceNode != null && !invoiceNode.get("Contact").isNull()) {
			return getJsonNodeValue(invoiceNode.get("Contact"), "Name");
		}

		return "";
	}

	private static String getContactName(JsonNode contactNode) {
		if (contactNode == null) {
			return "";
		}

		if (contactNode != null && !contactNode.get("Name").isNull()) {
			return contactNode.get("Name").asText();
		}

		return "";
	}

	// Extract InvoiceNumber from Allocations and return as a comma-separated string
	private static String getFromLineItem(JsonNode lineItem, String key) {
		if (lineItem == null || !lineItem.isArray()) {
			return ""; // Return empty if Allocations is missing or not an array
		}

		StringBuilder values = new StringBuilder();
		for (JsonNode item : lineItem) {
				String value = getJsonValue(item, key);
				if(values.isEmpty()) {
					
					values.append(value);
				} else {
					values.append(", ").append(value);	
				}
		}
		return values.toString();
	}

	// Extract InvoiceID and InvoiceNumber from Allocations and return as a
	// comma-separated string
	private static String getInvoiceDetails(JsonNode allocationsNode) {
		if (allocationsNode == null || !allocationsNode.isArray()) {
			return ""; // Return empty if Allocations is missing or not an array
		}

		StringBuilder invoiceDetails = new StringBuilder();
		for (JsonNode allocation : allocationsNode) {
			JsonNode invoiceNode = allocation.get("Invoice");
			if (invoiceNode != null) {
				String invoiceId = invoiceNode.has("InvoiceID") ? invoiceNode.get("InvoiceID").asText() : "N/A";
				String invoiceNumber = invoiceNode.has("InvoiceNumber") ? invoiceNode.get("InvoiceNumber").asText()
						: "N/A";

				if (invoiceDetails.length() > 0) {
					invoiceDetails.append(", "); // Add comma separator between entries
				}
				invoiceDetails.append(invoiceId).append(":").append(invoiceNumber);
			}
		}
		return invoiceDetails.toString();
	}

	// Helper method to get value from JSON safely
	private static String getJsonValue(JsonNode node, String fieldName) {
		return node.has(fieldName) && !node.get(fieldName).isNull() ? node.get(fieldName).asText() : "";
	}

	// Convert JsonNode to String safely and truncate if too long
	private static String safeJson(JsonNode node) {
		try {
			String json = node != null ? node.toString() : "{}";
			return json;
		} catch (Exception e) {
			return "{}";
		}
	}
}
