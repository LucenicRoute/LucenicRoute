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
		CreateIndex createIndex = new CreateIndex();
		try {
			createIndex.buildIndex();
		} catch (IOException e) {
			e.printStackTrace();
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
