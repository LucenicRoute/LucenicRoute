package parseDoc;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.CreateDocument;

public class FTParser {

    private static String INPUT_DIR = "Input/ft/"; //directory containing Financial Times

    public List<org.apache.lucene.document.Document> parseFT(IndexWriter indexWriter) throws IOException {
        List<org.apache.lucene.document.Document> documentList = new ArrayList<org.apache.lucene.document.Document>();
        List<Path> directoryPaths = listDirPaths();
        List<Path> filePaths = new ArrayList<Path>();

        for (Path path : directoryPaths) {
            try (Stream<Path> fileWalk = Files.walk(path)) {
                fileWalk.filter(Files::isRegularFile)
                        .forEach(file -> {
                    File currFile = new File(file.toString());
                    String docID = null;
                    String title = null;
                    String content = null;
                    if (!currFile.getName().startsWith("read")) {
                        try {
                            Document currDoc = Jsoup.parse(currFile, "UTF-8");
                            //System.out.println(currDoc);
                            Elements elements = currDoc.select("doc");
                            for (Element element : elements) {
                                docID = element.select("docno").text();
                                title = element.select("headline").text();
                                content = element.select("text").text();
                                org.apache.lucene.document.Document finalDoc = CreateDocument.createDocument(docID, title, content);
                                indexWriter.addDocument(finalDoc);
                                //System.out.println(currLucDoc);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        return documentList;
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

//    public org.apache.lucene.document.Document createDocument(String docid, String title, String content)
//    {
//        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
//        doc.add(new TextField("id", docid, Field.Store.YES));
//        doc.add(new TextField("title", title, Field.Store.YES));
//        doc.add(new TextField("content", content, Field.Store.YES));
//
//        return doc;
//    }


}
