package main;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;

import parseDoc.FBISParser;
import parseDoc.FTParser;
import parseDoc.LATimesParser;

public class Main {

	public static void main(String[] args) {
		FBISParser fbis = new FBISParser();
		FTParser ft = new FTParser();
		try {
			List<Document>  documentList = FTParser.parseFT();
			documentList.addAll(fbis.getFBISDocs());
			documentList.get(1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		LATimesParser lat = new LATimesParser();
		
		try {
			lat.InitializeParsing();
			System.out.println("The documents were parsed for the Los Angeles Times. Completed...");	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
