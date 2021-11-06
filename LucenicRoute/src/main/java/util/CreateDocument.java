package util;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

public class CreateDocument {

    public static Document createDocument(String docid, String title, String content) {
        Document doc = new Document();
        doc.add(new TextField(Constants.DOCUMENTID, docid, Field.Store.YES));
        doc.add(new TextField(Constants.TITLE, title, Field.Store.YES));
        doc.add(new TextField(Constants.CONTENT, content, Field.Store.YES));

        return doc;
    }
}
