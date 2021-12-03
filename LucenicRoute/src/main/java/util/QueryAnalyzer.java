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
        try{
            SynonymMap map = makeSynonymMap(Constants.SYNONYM_FILEPATH);
            tokenStream = new FlattenGraphFilter(new SynonymGraphFilter(tokenStream, map, true));
            //tokenStream = new SynonymGraphFilter(tokenStream, map, true);
        } catch (IOException e){
            System.out.println("Error at SynonymGraphFilter");
        }
        
        CharArraySet stopwordSet = processStopwords(Constants.STOPWORDS_FILEPATH);

        tokenStream = new EnglishPossessiveFilter(tokenStream);

        //tokenStream = new SynonymGraphFilter(tokenStream, synonyms, ignoreCase)


        System.out.println("\nBefore Synonyms: \n");
        System.out.println(tokenStream.toString());
        

        tokenStream = new StopFilter(tokenStream, stopwordSet);

        //SynonymMap map = new SynonymMap(new FileInputStream("/home/oscar/Desktop/eng_work/ir/prolog/wn_s.pl"));
        //SynonymMap map2 = new SynSystem.out.println("\nBefore Synonyms: \n");
        //System.out.println(tokenStream.toString());onymMap(new FileInputStream("/home/oscar/Desktop/eng_work/ir/prolog/wn_s.pl"), words, 5);
        //try{}
        //BufferedReader wnReader = new BufferedReader(new FileReader("/home/oscar/Desktop/eng_work/ir/prolog/wn_s.pl"));

        //WordnetSynonymParser wnParser = new WordnetSynonymParser(true, true, this);
        //try{
            //tokenStream = new SynonymGraphFilter(tokenStream, builder.build(), true);
        //} catch (IOException e) {
        //    System.out.println("ERROR WITH SYNONYMS");
        //}

        tokenStream = new SnowballFilter(tokenStream, "English");
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
        WordnetSynonymParser wordnetParser = new WordnetSynonymParser(true, true, new WhitespaceAnalyzer());
        try{
            wordnetParser.parse(new FileReader(filepath));
        } catch (ParseException e){
            return null;
        }
        return wordnetParser.build();
    }
}
