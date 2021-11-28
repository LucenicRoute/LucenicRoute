package parseDoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import util.CreateDocument;
import util.DocumentUtil;

public class LATimesParser {

	String directory = "./Input/latimes";

	List<String> filesPaths = new ArrayList<String>();

	public void InitializeParsing(IndexWriter indexWriter) throws IOException {

		try (Stream<Path> docPath = Files.walk(Paths.get(directory))) {
			docPath.forEach(filePath -> filesPaths.add(filePath.toString()));
		}
		
		filesPaths.remove(732);
		filesPaths.remove(731);
		filesPaths.remove(0);
		parseLATimes(filesPaths, indexWriter);
	}

	public void parseLATimes(List<String> docsList, IndexWriter indexWriter) throws IOException {

		for (String docPath : docsList) {
			try (InputStream stream = Files.newInputStream(Paths.get(docPath))) {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(stream, StandardCharsets.UTF_8));

				String validRow = bufferedReader.readLine();
				while (validRow != null) {
					String wholeContent = "";
					while (!validRow.equals("</DOC>")) {
						wholeContent += validRow;
						validRow = bufferedReader.readLine();
						if (validRow==null) {
							break;
						}
					}
					wholeContent += validRow;
					validRow = bufferedReader.readLine();
					Document document = Jsoup.parse(wholeContent);
					getFeaturesFromParsedDoc(document, indexWriter);
				}
			}
		}
	}

	private void getFeaturesFromParsedDoc(Document doc, IndexWriter indexWriter) {
		DocumentUtil docUtil = new DocumentUtil();
		docUtil.setDocNo(doc.select("DOCNO").text());
		docUtil.setHeadline(doc.select("HEADLINE").text().replaceAll("[^a-zA-Z 0-9 ]", "".toLowerCase()));
		docUtil.setContent(doc.select("TEXT").text().replaceAll("[^a-zA-Z 0-9 ]", "".toLowerCase())); 
		org.apache.lucene.document.Document midDoc = CreateDocument.createDocument(docUtil);
		try {
			indexWriter.addDocument(midDoc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}