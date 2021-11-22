package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import index.CreateIndex;
import parseDoc.FBISParser;
import parseDoc.FR94Parser;
import parseDoc.FTParser;
import parseDoc.LATimesParser;
import searchEngine.searchEngine;
import util.Constants;

public class Main {

	public static void main(String[] args) {

		boolean forceBuildIndex = false;

		// Check arguments
		if (args.length > 0) {
			if (Constants.BUILD_INDEX_SHORT.equals(args[0]) || Constants.BUILD_INDEX_LONG.equals(args[0])) {
				forceBuildIndex = true;
			} else {
				System.out.printf("Invalid option. To forcefully create a new index use %s or %s.\n", Constants.BUILD_INDEX_SHORT, Constants.BUILD_INDEX_LONG);
			}
		}

		try {
			if (forceBuildIndex || !indexExists()) {
				CreateIndex createIndex = new CreateIndex();
				createIndex.buildIndex();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		searchEngine se = new searchEngine();
		try {
			se.searching(Constants.DEFAULT_BM25_K1_VALUE, Constants.DEFAULT_BM25_B_VALUE);
			System.out.println("Searching completed successfully.");
		} catch (final IOException e) {
			System.out.printf("Error while searching documents.\n%s\n",e.getMessage());
		}
	}

	public static boolean indexExists() throws IOException {
		final Directory directory = FSDirectory.open(Paths.get(Constants.INDEX_DIRECTORY));
		return DirectoryReader.indexExists(directory);
	}
}
