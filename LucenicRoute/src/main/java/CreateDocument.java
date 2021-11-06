import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

public class CreateDocument {

    public static Document createDocument(String docid, String title, String content) {
        Document doc = new Document();
        doc.add(new TextField("id", docid, Field.Store.YES));
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("content", content, Field.Store.YES));

        return doc;
    }
}
