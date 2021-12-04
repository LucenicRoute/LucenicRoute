package searchEngine;

/**
 * @author Tolga Arslan
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;

import parseQuery.QueryReader;
import util.Constants;

public class searchEngine {
	
	public static final String RESULT_DIRECTORY = "results";
	
	private void searchAndWrite(Query query, String queryID, IndexSearcher isrch, PrintWriter wr) throws IOException {
		
		//Our size is 528160
		//TopDocs res = iscr.search(query, 528160);
		// for the test purpose
		TopDocs res = isrch.search(query, 1000);
		
        ScoreDoc[] scr = res.scoreDocs;

        for (int i = 0; i < scr.length; i++) {
            //Document doc = iscr.doc(scr[i].doc);
			Document hitDoc = isrch.doc(scr[i].doc);
			//System.out.println(hitDoc.getFields());
            wr.println(queryID + " 0 " + hitDoc.get("DocumentId") + " " + i + " " + scr[i].score + " LucenicRoute");
        }
		wr.flush();
	}
	
	public void searching(final float customK1Value, final float customBValue, final Map<String, Float> boosts) throws IOException {
		
		QueryReader qr = new QueryReader(boosts);
		List<Query> queriesList= qr.retrieveQueries();
		List<String> queryIDs = qr.getQueryIDs();	
		IndexReader rdr = DirectoryReader.open(FSDirectory.open(Paths.get(Constants.INDEX_DIRECTORY)));
		PrintWriter wr = new PrintWriter(RESULT_DIRECTORY, "UTF-8");
        IndexSearcher isrch = new IndexSearcher(rdr);
        //step of setting similarity with custom values
		isrch.setSimilarity(new BM25Similarity(customK1Value, customBValue));
        
        for (int i=0; i<queriesList.size(); i++) {
        	//sends each query to the searchAndWrite function.
        	searchAndWrite(queriesList.get(i), queryIDs.get(i), isrch, wr);
		}
		
		wr.close();
        rdr.close();
	}
}
