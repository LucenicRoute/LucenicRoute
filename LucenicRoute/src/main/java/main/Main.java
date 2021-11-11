package main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;

import parseDoc.FBISParser;
import parseDoc.FR94Parser;
import parseDoc.FTParser;
import parseDoc.LATimesParser;

public class Main {

	public static void main(String[] args) {
		FBISParser fbis = new FBISParser();
		FTParser ft = new FTParser();
		LATimesParser lat = new LATimesParser();
		FR94Parser fr94 = new FR94Parser();
		List<Document>  documentList = new ArrayList<Document>();
		
		try {
			System.out.println("Parsing FT...");
			long startTime = System.nanoTime();
			documentList.addAll(FTParser.parseFT());
			long endTime = System.nanoTime();
			long timeElapsed = endTime-startTime;
			System.out.print("FT Complete:\tTime taken = " + timeElapsed / 1000000 + "ms\tTotal Corpus Size = " + (documentList.size()) + "\n");
		} catch (IOException e) {
			System.out.println("Failed to parse FT");
			System.out.println(e.getMessage());
		}
		
		try {
			System.out.println("Parsing FBIS...");
			long startTime = System.nanoTime();
			documentList.addAll(fbis.getFBISDocs());
			long endTime = System.nanoTime();
			long timeElapsed = endTime-startTime;
			System.out.print("FBIS Complete:\tTime taken = " + timeElapsed / 1000000 + "ms\tTotal Corpus Size = " + (documentList.size()) + "\n");
		} catch (IOException e) {
			System.out.println("Failed to parse FBIS");
			System.out.println(e.getMessage());
		}
	
		try {
			System.out.println("Parsing LAT...");
			long startTime = System.nanoTime();
			documentList.addAll(lat.InitializeParsing());
			long endTime = System.nanoTime();
			long timeElapsed = endTime-startTime;
			System.out.println("The documents were parsed for the Los Angeles Times. Completed...");
			System.out.print("LAT Complete:\tTime taken = " + timeElapsed / 1000000 + "ms\tTotal Corpus Size = " + (documentList.size()) + "\n");
		} catch (IOException e) {
			System.out.println("Failed to parse LAT");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		try {
			System.out.println("Parsing FR...");
			long startTime = System.nanoTime();
			documentList.addAll(fr94.getFRDocs("Input/fr94"));
			long endTime = System.nanoTime();
			long timeElapsed = endTime-startTime;
			System.out.println(timeElapsed);
			System.out.print("FR Complete:\tTime taken = " + timeElapsed / 1000000 + "ms\tTotal Corpus Size = " + (documentList.size()) + "\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to parse FR");
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	
	}
}
