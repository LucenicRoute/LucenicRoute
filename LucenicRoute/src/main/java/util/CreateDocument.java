package util;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

public class CreateDocument {

    public static Document createDocument(DocumentUtil docUtil) {
        Document doc = new Document();
        doc.add(new TextField(Constants.DOCUMENTID, docUtil.getDocNo() , Field.Store.YES));
        doc.add(new TextField(Constants.TITLE, docUtil.getHeadline(), Field.Store.YES));
        doc.add(new TextField(Constants.CONTENT, docUtil.getContent(), Field.Store.YES));
        if(docUtil.getDate() != null && docUtil.getDate().length()>0) {
        	 doc.add(new TextField(Constants.DATE, docUtil.getDate(), Field.Store.YES));
        }
        if(docUtil.getPublication()!=null && docUtil.getPublication().length()>0) {
        	 doc.add(new TextField(Constants.PUBLICATION, docUtil.getPublication(), Field.Store.YES));
        }

        return doc;
    }
}
