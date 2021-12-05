package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
//import java.io.ParseException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.FlattenGraphFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.util.CharsRef;
import java.io.BufferedReader;
import java.io.FileReader;



public class QueryAnalyzer extends Analyzer{

    @Override
    protected TokenStreamComponents createComponents(final String fieldName) {
        final StandardTokenizer tokenizer = new StandardTokenizer();
        TokenStream tokenStream = new LowerCaseFilter(tokenizer);

        CharArraySet stopwordSet = processStopwords(Constants.STOPWORDS_FILEPATH);
        tokenStream = new StopFilter(tokenStream, stopwordSet);

        tokenStream = new EnglishPossessiveFilter(tokenStream);

        tokenStream = new SnowballFilter(tokenStream, "English");

        try{
            SynonymMap map = makeSynonymMap(Constants.SYNONYM_FILEPATH);
            tokenStream = new SynonymGraphFilter(tokenStream, map, true);
            tokenStream = new FlattenGraphFilter(tokenStream);
        } catch (IOException e){
            e.printStackTrace();
        }
        return new TokenStreamComponents(tokenizer, tokenStream);
    }

    /*
    *   Stopword list taken from https://sraf.nd.edu/textual-analysis/resources/#StopWords. Accessed 24th November 2021.
    */
    public static CharArraySet processStopwords(final String filepath) {
        final Path queryFilePath = Paths.get(filepath);
        final Scanner scanner;
        try {
            scanner =  new Scanner(queryFilePath, Constants.ENCODING.name());
        } catch (IOException e) {
            return null;
        }
        CharArraySet stopwordSet = new CharArraySet(Constants.INITIAL_STOPWORD_SET_SIZE, true);

        while (scanner.hasNextLine()) {
            stopwordSet.add(scanner.nextLine());
        }

        scanner.close();
        return stopwordSet;
    }

    public static SynonymMap makeSynonymMap(final String filepath) throws IOException{
        WordnetSynonymParser wordnetParser = new WordnetSynonymParser(true, true, new StandardAnalyzer(CharArraySet.EMPTY_SET));
        try{
            wordnetParser.parse(new FileReader(filepath));
        } catch (ParseException e){
            e.printStackTrace();
        }
        return wordnetParser.build();
    }
}
