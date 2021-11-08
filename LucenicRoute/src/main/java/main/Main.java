package main;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;

import parseDoc.FBISParser;
import parseDoc.FTParser;

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
	}
}
