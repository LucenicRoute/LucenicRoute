package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;

import index.CreateIndex;
import parseDoc.FBISParser;
import parseDoc.FR94Parser;
import parseDoc.FTParser;
import parseDoc.LATimesParser;
import searchEngine.searchEngine;
import util.Constants;

public class Main {

	public static void main(String[] args) {
		FBISParser fbis = new FBISParser();
		FTParser ft = new FTParser();
		LATimesParser lat = new LATimesParser();
		FR94Parser fr94 = new FR94Parser();
		List<Document> documentList = new ArrayList<Document>();
		Path directory = Paths.get(Constants.INDEX_DIRECTORY);
		
		if(!Files.exists(directory)) {
//			try {
//				System.out.println("Parsing FT...");
//				long startTime = System.nanoTime();
//				documentList.addAll(ft.parseFT());
//				long endTime = System.nanoTime();
//				long timeElapsed = endTime - startTime;
//				System.out.print("FT Complete:\tTime taken = " + timeElapsed / 1000000 + "ms\tTotal Corpus Size = "
//						+ (documentList.size()) + "\n");
//			} catch (IOException e) {
//				System.out.println("Failed to parse FT");
//				System.out.println(e.getMessage());
//			}
	
			try {
				System.out.println("Parsing FBIS...");
				long startTime = System.nanoTime();
				documentList.addAll(fbis.getFBISDocs());
				long endTime = System.nanoTime();
				long timeElapsed = endTime - startTime;
				System.out.print("FBIS Complete:\tTime taken = " + timeElapsed / 1000000 + "ms\tTotal Corpus Size = "
						+ (documentList.size()) + "\n");
			} catch (IOException e) {
				System.out.println("Failed to parse FBIS");
				System.out.println(e.getMessage());
			}
	
			try {
				System.out.println("Parsing LAT...");
				long startTime = System.nanoTime();
				documentList.addAll(lat.InitializeParsing());
				long endTime = System.nanoTime();
				long timeElapsed = endTime - startTime;
				System.out.print("LAT Complete:\tTime taken = " + timeElapsed / 1000000 + "ms\tTotal Corpus Size = "
						+ (documentList.size()) + "\n");
			} catch (IOException e) {
				System.out.println("Failed to parse LAT");
				System.out.println(e.getMessage());
			}
	
			try {
				System.out.println("Parsing FR...");
				long startTime = System.nanoTime();
				documentList.addAll(fr94.getFRDocs("Input/fr94"));
				long endTime = System.nanoTime();
				long timeElapsed = endTime - startTime;
				System.out.println(timeElapsed);
				System.out.print("FR Complete:\tTime taken = " + timeElapsed / 1000000 + "ms\tTotal Corpus Size = "
						+ (documentList.size()) + "\n");
			} catch (Exception e) {
				System.out.println("Failed to parse FR");
				System.out.println(e.getMessage());
			}
			
			System.out.println("The size of the Document List: " + documentList.size());
			
			//index all the document
			CreateIndex index = new CreateIndex();
			try {
				index.buildIndex(documentList);
			} catch (IOException e) {
				System.out.println("Failed to index documents.");
				System.out.println(e.getMessage());
			}
			System.out.println("Document indexed successfully.");
		}

		searchEngine se = new searchEngine();
		try {
			se.searching();
			System.out.println("Searching completed successfully.");
		} catch (IOException e) {
			System.out.println("Error while searching documents.");
			System.out.println(e.getMessage());
		}

	}
}
