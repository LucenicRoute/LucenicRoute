package parseDoc;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.CreateDocument;

public class FTParser {

    private static String path = "Input/ft"; //directory containing Financial Times


    public static List<org.apache.lucene.document.Document> parseFT() throws IOException {
        File directoryPath = new File(path); //give path to directory
        // create filter to open only directories, don't want to open the README
        FileFilter textFilefilter = new FileFilter(){
            public boolean accept(File file) {
                boolean isFile = file.isDirectory();
                if (isFile) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        List<org.apache.lucene.document.Document> documentList= new ArrayList<org.apache.lucene.document.Document>();

        //create array of all the Financial Times directories.
        File directoryFolders[] = directoryPath.listFiles(textFilefilter);
        for(File directory:directoryFolders) {
            File currListFiles[] = directory.listFiles(); //for each directory get all the files
            for(File file:currListFiles) {
                Document doc = Jsoup.parse(file, "UTF-8"); //parse each file with jsoup
//                System.out.println(doc);
                Elements documents = doc.select("doc"); //extract each doc in each file
                for (Element docs : documents) { //for each doc in a file

                    String docID = docs.select("docno").text(); //find docno of current doc
                    String title = docs.select("headline").text(); //find the title of the current doc
                    String content = docs.select("text").text(); //find content of current doc.
                    org.apache.lucene.document.Document finalDoc = CreateDocument.createDocument(docID, title, content);
                    documentList.add(finalDoc);
//                    System.out.println(docno);
//                    System.out.println(title);
//                    System.out.println(content);
//                    System.out.println(finalDoc);
                }
            }
        }
        return documentList;
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
