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

import util.Constants;

/**
 * @author Anuradha Vishwakarma
 * class to create index 
 */
public class CreateIndex {

	public void buildIndex(List<Document> documentList) throws IOException {
		//Connection to the path where index needs to be saved
		Directory directory = FSDirectory.open(Paths.get(Constants.INDEX_DIRECTORY));
		Analyzer analyzer = new StandardAnalyzer(EnglishAnalyzer.getDefaultStopSet());
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter iwriter = new IndexWriter(directory, config);
		
		//add the list of documents to writer
		iwriter.addDocuments(documentList);
		// close the writer and directory obj
		iwriter.close();
		directory.close();
		System.out.println("File indexed succefully.");
	}
}
