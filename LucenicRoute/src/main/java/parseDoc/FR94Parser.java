package parseDoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.CreateDocument;

public class FR94Parser {

	public final static String INPUT_DIRECTORY = "Input/fr94/";

	public List<Document> getFRDocs(IndexWriter indexWriter) throws IOException{
		
		List<Document> documentList = new ArrayList<>();
		//read each file from the given location
		try (Stream<Path> filePathStream=Files.walk(Paths.get(INPUT_DIRECTORY))) {
		    filePathStream.forEach(filePath -> {
		    	Document doc = null;
		    	String documentNo = "";
		    	String parentNo = "";
				String text = "";
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
								documentNo = element.select("DOCNO").text();
								parentNo = element.select("PARENT").text();
								text = element.select("TEXT").text();
								//create the document 
								doc = CreateDocument.createDocument(documentNo, parentNo, text);
								//add document to document list
								indexWriter.addDocument(doc);
							}
			        	} catch (IOException e) {
							System.out.println("Exception occured while reading FR file");
						}
		        	}
		        }
		    }
			);
		}
		
		return documentList;
	}


	public List<Document> parseFR(String filePath) throws Exception {
		//each DOC is a new lucene document, although it might only contain part of an article
		List<Document> docList = new ArrayList<Document>();

		int docCount = 0;
		/*
		String[] stopWords = new String[] { "hyph", "blank", "sect", "para", "cir", "rsquo", "mu", "times", "bull",
		        "ge", "reg", "cent", "amp", "gt", "lt", "acirc", "ncirc", "atilde", "ntilde", "otilde", "utilde",
		        "aacute", "cacute", "eacute", "Eacute", "Gacute", "iacute", "lacute", "nacute", "oacute", "pacute",
		        "racute", "sacute", "uacute", "ocirc", "auml", "euml", "Euml", "iuml", "Iuml", "Kuml", "Ouml", "ouml",
		        "uuml", "Ccedil", "ccedil", "agrave", "Agrave", "egrave", "Egrave", "igrave", "Ograve", "ograve",
		        "ugrave" };
				*/
		int fileCount = 0;

		//for loops cycle through the files if we start in fr94
		File monthDirs[] = new File(filePath).listFiles();
		for (File monthDir : monthDirs) {
		    if (monthDir.isDirectory()) {
		        File files[] = monthDir.listFiles();
		        for (File file : files) {
		            //System.out.println("num: " + fileCount);
		            fileCount++;
		            //System.out.println(monthDir.getName() + file.getName() + "\n");
		            BufferedReader br = new BufferedReader(new FileReader(file));

		            String line = null;
		            StringBuilder sb = new StringBuilder();
		            StringBuilder contentsb = new StringBuilder();
		            StringBuilder titlesb = new StringBuilder();
		            String currDate = "";
		            Document doc = new Document();
		            String prevParent = "";
		            String currParent = "";
		            String currDoc = "";
		            String prevDoc = "";

		            FieldType ft = new FieldType(TextField.TYPE_STORED);

		            try {

		                while ((line = br.readLine()) != null) {
		                    if (line.startsWith("<DOC>")) {
		                        docCount++;
		                    } 
		                    else if (line.startsWith("<DOCNO>")) {
		                        // the old variables are added to the previous document and then the new document is creaed with this ID
		                        doc.add(new Field("Contents", contentsb.toString(), ft));
		                        doc.add(new Field("Contents", currDate, ft));

		                        doc.add(new Field("Parent ID", currParent, ft));

		                        contentsb.delete(0, contentsb.length());

		                        docList.add(doc);

		                        prevDoc = currDoc;

		                        currDoc = line.replaceAll("<\\w*>", " ");//removing <[tags]>
		                        currDoc = currDoc.replaceAll("<.*>", " ");
//		                        currDoc = currDoc.strip();

		                        doc = new Document();
		                        doc.add(new Field("Document ID", currDoc, ft));
		                    } 
		                    else if (line.startsWith("<PARENT>")) {
		                        // save parent ID
		                        prevParent = currParent;
		                        currParent = line.replaceAll("<\\w*>", " ");
		                        currParent = currParent.replaceAll("<.*>", " ");
//		                        currParent = currParent.strip();
		                    } 
		                    else if (line.startsWith("<!-- PJG ITAG l=56 g=1 f=1 -->")
		                            || line.startsWith("<!-- PJG ITAG l=52 g=1 f=1 -->")) {
		                        //these are the tags that correspond to titles in the file
		                        //line = br.readLine();
		                        //while (line.startsWith("<") == false) {
		                        //    line = line.replaceAll("_", " ");
		                        //    titlesb.append(" ");
		                        //    titlesb.append(line);
		                        //    line = br.readLine();
		                        //}
		                        doc.add(new Field("Title", titlesb.toString(), ft));
		                        titlesb.delete(0, titlesb.length());

		                    } 
		                    else if (line.startsWith("<!-- PJG ITAG l=02 g=1 f=1 -->") && currDate.equals("")) {
		                        // the date is found after this tag in the first doc of a file eg "Wednesday, October 12, 1994"
		                        line = br.readLine();
		                        currDate = line;
		                    } 
		                    else if (line.startsWith("<!--")) {
		                        //ignore the other tags
		                    } 
		                    else if (line.startsWith("<") == false) {
		                        //the rest of the content is read in here
		                        String currCont = line.replaceAll("\\(\\w\\)", " ");

		                        //for (int i = 0; i < stopWords.length; i++) {
		                            // removing the stopwords eg &blank representing a blank space
		                        //    String replaceString = "&" + stopWords[i];
		                        //    currCont = currCont.replaceAll(replaceString, " ");
		                        //}
		                        
		                        contentsb.append(currCont);
		                        contentsb.append(" ");
		                    }
		                }

		                doc.add(new Field("Contents", contentsb.toString(), ft));
		                doc.add(new Field("Parent ID", currParent, ft));
		                docList.add(doc);
		                //once the end of the file is reached we add the final document

		            } catch (Exception ex) {
		                ex.printStackTrace();
		            } finally {
		                br.close();
		            }
		        }
		    }
		}
		return docList;
	}
	    
	public static List<Document> parseFRarticles(String filePath) throws Exception {
		//DOCs with the same PARENT are gathered into one lucene document
		List<Document> docList = new ArrayList<Document>();

		String[] stopWords = new String[] { "hyph", "blank", "sect", "para", "cir", "rsquo", "mu", "times", "bull",
		        "ge", "reg", "cent", "amp", "gt", "lt", "acirc", "ncirc", "atilde", "ntilde", "otilde", "utilde",
		        "aacute", "cacute", "eacute", "Eacute", "Gacute", "iacute", "lacute", "nacute", "oacute", "pacute",
		        "racute", "sacute", "uacute", "ocirc", "auml", "euml", "Euml", "iuml", "Iuml", "Kuml", "Ouml", "ouml",
		        "uuml", "Ccedil", "ccedil", "agrave", "Agrave", "egrave", "Egrave", "igrave", "Ograve", "ograve",
		        "ugrave" };
		int fileCount = 0;

		// starting in directory fr94
		File monthDirs[] = new File(filePath).listFiles();
		for (File monthDir : monthDirs) {
		    if (monthDir.isDirectory()) {
		        File files[] = monthDir.listFiles();
		        for (File file : files) {
		            //System.out.println("num: " + fileCount);
		            fileCount++;
		            //System.out.println(monthDir.getName() + file.getName() + "\n");
		            BufferedReader br = new BufferedReader(new FileReader(file));

		            String currDate = "";
		            String line = null;
		            StringBuilder sb = new StringBuilder();
		            StringBuilder contentsb = new StringBuilder();
		            StringBuilder titlesb = new StringBuilder();
		            //String[] elementNames = new String[] { "FOOTCITE", "h" };
		            Document doc = new Document();
		            String prevParent = "";
		            String currParent = "";
		            String currDoc = "";
		            String prevDoc = "";
		            int docCount = 0;

		            FieldType ft = new FieldType(TextField.TYPE_STORED);

		            try {
		                while ((line = br.readLine()) != null) {
		                    if (line.startsWith("<DOC>")) {
		                        docCount++;
		                    }
		                    else if (line.startsWith("<DOCNO>")) {
		                        // we hold onto new DOCNO but wait until PARENT to see if we make a new document
		                        prevDoc = currDoc;
		                        currDoc = line.replaceAll("<\\w*>", " ");
		                        currDoc = currDoc.replaceAll("<.*>", " ");
//		                        currDoc = currDoc.strip();
		                    } 
		                    else if (line.startsWith("<PARENT>")) {
		                        prevParent = currParent;
		                        currParent = line.replaceAll("<\\w*>", " ");
		                        currParent = currParent.replaceAll("<.*>", " ");
//		                        currParent = currParent.strip();
		                        if (currParent.equals(prevParent) == false) {
		                            //if twe have a new parent document we push the previous docuemnt to list and create a new one
		                            doc.add(new Field("Contents", contentsb.toString(), ft));
		                            doc.add(new Field("Contents", currDate, ft));
		                            contentsb.delete(0, contentsb.length());
		                            docList.add(doc);
		                            doc = new Document();
		                            doc.add(new Field("Parent ID", currParent, ft));
		                        }
		                        doc.add(new Field("Document ID", currDoc, ft));
		                    } 
		                    else if (line.startsWith("<!-- PJG ITAG l=56 g=1 f=1 -->")
		                            || line.startsWith("<!-- PJG ITAG l=52 g=1 f=1 -->")) {
		                                //these are the tags that correspond to titles in the file
		                        line = br.readLine();
		                        while (line.startsWith("<") == false) {
		                            titlesb.append(" ");
		                            line = line.replaceAll("_", " ");
		                            titlesb.append(line);
		                            line = br.readLine();
		                        }
		                        doc.add(new Field("Title", titlesb.toString(), ft));
		                        titlesb.delete(0, titlesb.length());
		                    } 
		                    else if (line.startsWith("<!-- PJG ITAG l=02 g=1 f=1 -->") && currDate.equals("")) {
		                        // the date is found after this tag in the first doc of a file eg "Wednesday, October 12, 1994"
		                        line = br.readLine();
		                        currDate = line;
		                    } 
		                    else if (line.startsWith("<!--")) {
		                        //ignore the other tags
		                    } 
		                    else if (line.startsWith("<") == false) {
		                        //the rest of the content is read in here
		                        String currCont = line.replaceAll("\\(\\w\\)", " ");
		                        for (int i = 0; i < stopWords.length; i++) {
		                            String replaceString = "&" + stopWords[i];
		                            currCont = currCont.replaceAll(replaceString, " ");
		                        }
		                        contentsb.append(currCont);
		                        contentsb.append(" ");
		                    }
		                }

		                doc.add(new Field("Contents", contentsb.toString(), ft));
		                doc.add(new Field("Parent ID", currParent, ft));
		                //once the end of the file is reached we add the final document
		                docList.add(doc);

		            } catch (Exception ex) {
		                ex.printStackTrace();
		            } finally {
		                br.close();

		            }
		        }
		    }
		}
		return docList;
	}
}
