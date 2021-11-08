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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LATimesParser {

	String directory = "./Input/latimes";

	List<LATimesDocument> latDocsList = new ArrayList<LATimesDocument>();
	List<String> filesPaths = new ArrayList<String>();

	public List<LATimesDocument> InitializeParsing() throws IOException {

		try (Stream<Path> docPath = Files.walk(Paths.get(directory))) {
			docPath.forEach(filePath -> filesPaths.add(filePath.toString()));
		}

		String[] removePaths = { ".\\Input\\latimes", ".\\Input\\latimes\\readchg.txt", ".\\Input\\latimes\\readmela.txt" };
		
		for (String path : removePaths) {
			filesPaths.remove(path);
		}

		System.out.println("The files are stored for Los Angeles Times. Completed...");

		return parseLATimes(filesPaths);
	}

	public List<LATimesDocument> parseLATimes(List<String> docsList) throws IOException {

		for (String docPath : docsList) {
			System.out.println("Parsing is starting for " + docPath);
			try (InputStream stream = Files.newInputStream(Paths.get(docPath))) {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(stream, StandardCharsets.UTF_8));

				String validRow = bufferedReader.readLine();
				while (validRow != null) {

					String wholeContent = "";
					while (!validRow.equals("</DOC>")) {
						wholeContent += validRow;
						validRow = bufferedReader.readLine();
					}
					wholeContent += validRow;
					validRow = bufferedReader.readLine();

					Document document = Jsoup.parse(wholeContent);
					getFeaturesFromParsedDoc(document);
				}
				System.out.println("Completed...");
			}
		}

		return latDocsList;
	}

	private void getFeaturesFromParsedDoc(Document doc) {

		LATimesDocument laDoc = new LATimesDocument();

		laDoc.setDOC_NO(doc.select("DOCNO").text());
		laDoc.setDOC_ID(doc.select("DOCID").text());
		laDoc.setDATE(doc.select("DATE").text());
		laDoc.setSECTION(doc.select("SECTION").text());
		laDoc.setLENGTH(doc.select("LENGTH").text());
		laDoc.setHEADLINE(doc.select("HEADLINE").text());
		laDoc.setBYLINE(doc.select("BYLINE").text());
		laDoc.setTEXT(doc.select("TEXT").text());
		laDoc.setGRAPHIC(doc.select("GRAPHIC").text());
		laDoc.setTYPE(doc.select("TYPE").text());

		latDocsList.add(laDoc);
	}

}