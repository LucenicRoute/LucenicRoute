package parseDoc;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.CreateDocument;
import util.DocumentUtil;

public class FTParser {

    private static String INPUT_DIR = "Input/ft/"; //directory containing Financial Times

    public void parseFT(IndexWriter indexWriter) throws IOException {
        List<Path> directoryPaths = listDirPaths();
        
        for (Path path : directoryPaths) {
            try (Stream<Path> fileWalk = Files.walk(path)) {
                fileWalk.filter(Files::isRegularFile)
                        .forEach(file -> {
                	DocumentUtil docUtil = null;
                    File currFile = new File(file.toString());
                    if (!currFile.getName().startsWith("read")) {
                        try {
                            Document currDoc = Jsoup.parse(currFile, "UTF-8");
                            Elements elements = currDoc.select("DOC");
                            for (Element element : elements) {
                            	docUtil = new DocumentUtil();
                        		docUtil.setDocNo(element.select("DOCNO").text());
                        		docUtil.setHeadline(element.select("HEADLINE").text().replaceAll("[^a-zA-Z 0-9 ]", "".toLowerCase()));
                        		docUtil.setContent(element.select("TEXT").text().replaceAll("[^a-zA-Z 0-9 ]", "".toLowerCase())); 
                        		docUtil.setDate(element.select("DATE").text().replaceAll("[^a-zA-Z 0-9 ]", "".toLowerCase()));
                        		docUtil.setPublication(element.select("PUB").text().replaceAll("[^a-zA-Z 0-9 ]", "".toLowerCase()));
                                org.apache.lucene.document.Document finalDoc = CreateDocument.createDocument(docUtil);
                                indexWriter.addDocument(finalDoc);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }


    public static List<Path> listDirPaths() throws IOException {
        List<Path> filePaths;
        Path input_dir = Paths.get(INPUT_DIR);
        try (Stream<Path> walk = Files.walk(input_dir,1)) {
            filePaths = walk.filter(Files::isDirectory)
                    .collect(Collectors.toList());
        }
        filePaths.remove(0);
        return filePaths;

    }
}
