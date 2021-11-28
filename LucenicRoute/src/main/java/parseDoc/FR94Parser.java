package parseDoc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.CreateDocument;
import util.DocumentUtil;

public class FR94Parser {

	public final static String INPUT_DIRECTORY = "Input/fr94/";

	public void getFRDocs(IndexWriter indexWriter) throws IOException{
		//read each file from the given location
		try (Stream<Path> filePathStream=Files.walk(Paths.get(INPUT_DIRECTORY))) {
		    filePathStream.forEach(filePath -> {
		    	Document doc = null;
				DocumentUtil docUtil =null;

				if (Files.isRegularFile(filePath)) {
		        	//get the file content
		        	File file = new File(filePath.toString());
		        	if(!file.getName().startsWith("read")) {
			        	try {
			        		//parse the document with JSOUP
				        	org.jsoup.nodes.Document d = Jsoup.parse(file, "UTF-8");
							Elements elements = d.select("DOC");
							for(Element element: elements) {
								doc = new Document();
								docUtil = new DocumentUtil();
								docUtil.setDocNo(element.select("DOCNO").text());
                        		docUtil.setHeadline(element.select("PARENT").text().replaceAll("[^a-zA-Z 0-9 ]", "".toLowerCase()));
                        		docUtil.setContent(element.select("TEXT").text().replaceAll("[^a-zA-Z 0-9 ]", "".toLowerCase())); 
								doc = CreateDocument.createDocument(docUtil);
								indexWriter.addDocument(doc);
							}
			        	} catch (IOException e) {
							System.out.println("Exception occured while reading FR file");
						}
		        	}
		        }
		    });

		}
	}

}
