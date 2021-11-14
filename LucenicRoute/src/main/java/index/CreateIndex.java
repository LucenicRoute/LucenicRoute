package index;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import parseDoc.FBISParser;
import parseDoc.FR94Parser;
import parseDoc.FTParser;
import parseDoc.LATimesParser;
import util.Constants;

import javax.print.Doc;

/**
 * @author Anuradha Vishwakarma
 * class to create index 
 */
public class CreateIndex {

	public void buildIndex() throws IOException {
		//Connection to the path where index needs to be saved
		Directory directory = FSDirectory.open(Paths.get(Constants.INDEX_DIRECTORY));
		Analyzer analyzer = new StandardAnalyzer(EnglishAnalyzer.getDefaultStopSet());
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter iwriter = new IndexWriter(directory, config);
		FTParser ftParser = new FTParser();
		FBISParser fbisParser = new FBISParser();
		LATimesParser laTimesParser = new LATimesParser();
		FR94Parser fr94Parser = new FR94Parser();
		//add the list of documents to writer

		System.out.println("Beginning Indexing");
		long totalTimeStart = System.nanoTime();

		long startTime = System.nanoTime();
		ftParser.parseFT(iwriter);
		long endTime = System.nanoTime();
		long timeElapsed = endTime-startTime;
		System.out.print("FT Indexed:\tTime taken = " + timeElapsed / 1000000 + "\n");

		startTime = System.nanoTime();
		fbisParser.getFBISDocs(iwriter);
		endTime = System.nanoTime();
		timeElapsed = endTime-startTime;
		System.out.print("FBIS Indexed:\tTime taken = " + timeElapsed / 1000000 + "\n");

		startTime = System.nanoTime();
		laTimesParser.InitializeParsing(iwriter);
		endTime = System.nanoTime();
		timeElapsed = endTime-startTime;
		System.out.print("LATimes Indexed:\tTime taken = " + timeElapsed / 1000000 + "\n");

		startTime = System.nanoTime();
		fr94Parser.getFRDocs(iwriter);
		long totalTimeEnd  = endTime = System.nanoTime();
		timeElapsed = endTime-startTime;

		long totalTimeElapsed = totalTimeEnd-totalTimeStart;
		System.out.print("FR Indexed:\tTime taken = " + timeElapsed / 1000000 + "\n");

		// close the writer and directory obj
		iwriter.close();
		directory.close();
		System.out.println("Files indexed succefully.\tTotal time taken = " + totalTimeElapsed/1000000);
	}
}
