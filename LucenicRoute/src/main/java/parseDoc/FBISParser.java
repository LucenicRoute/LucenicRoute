package parseDoc;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.CreateDocument;

public class FBISParser {
	
	public final static String INPUT_DIRECTORY = "Input/fbis/";
	
	public void getFBISDocs(IndexWriter indexWriter) throws IOException{
		
		List<Document> documentList = new ArrayList<>();
		//read each file from the given location
		try (Stream<Path> filePathStream=Files.walk(Paths.get(INPUT_DIRECTORY))) {
			filePathStream.forEach(filePath -> {
				Document doc = null;
				String documentNo = "";
				String title = "";
				String text = "";
				if (Files.isRegularFile(filePath)) {
					//get the file content
					File file = new File(filePath.toString());
					if (!file.getName().startsWith("read")) {
						try {
							//parse the document with JSOUP
							org.jsoup.nodes.Document d = Jsoup.parse(file, "UTF-8");
							Elements elements = d.select("DOC");
							for (Element element : elements) {
								doc = new Document();
								documentNo = element.select("DOCNO").text();
								title = element.select("HEADER").select("TI").text();
								text = element.select("TEXT").text();
								//create the document 
								doc = CreateDocument.createDocument(documentNo, title, text);
								//add document to document list
								indexWriter.addDocument(doc);
							}
						} catch (IOException e) {
							System.out.println("Exception occured while reading FBIS file");
						}
					}
				}
			});
		}
	}
}
