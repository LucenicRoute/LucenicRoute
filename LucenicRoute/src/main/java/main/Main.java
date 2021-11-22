package main;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import index.CreateIndex;
import searchEngine.searchEngine;
import util.Constants;

public class Main {

	public static void main(String[] args) {

		boolean forceBuildIndex = false;
		float customK1Value = Constants.DEFAULT_BM25_K1_VALUE;
		float customBValue = Constants.DEFAULT_BM25_B_VALUE;

		// Check arguments
		final Options options = new Options();
		options.addOption(Option.builder(Constants.BUILD_INDEX_SHORT)
								.longOpt(Constants.BUILD_INDEX_LONG)
								.desc("Forcefully create a new index.")
								.build());
		options.addOption(Option.builder(Constants.CUSTOM_BM25_K1_VALUE)
								.hasArg()
								.desc("Custom k1 value for BM25 similarity.")
								.build());
		options.addOption(Option.builder(Constants.CUSTOM_BM25_B_VALUE)
								.hasArg()
								.desc("Custom b value for BM25 similarity.")
								.build());
		try {
			final CommandLineParser parser = new DefaultParser();
			final CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption(Constants.BUILD_INDEX_SHORT)) {
				forceBuildIndex = true;
			}
			if (cmd.hasOption(Constants.CUSTOM_BM25_K1_VALUE)) {
				customK1Value = Float.parseFloat(cmd.getOptionValue(Constants.CUSTOM_BM25_K1_VALUE));
			}
			if (cmd.hasOption(Constants.CUSTOM_BM25_B_VALUE)) {
				customBValue = Float.parseFloat(cmd.getOptionValue(Constants.CUSTOM_BM25_B_VALUE));
			}
		} catch (final ParseException e) {
			System.out.println("Invalid argument passed.");
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(" ", options, true);
			return;
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
			System.out.printf("Using k1 value <%s> and b value <%s>.\n", customK1Value, customBValue);
			se.searching(customK1Value, customBValue);
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
