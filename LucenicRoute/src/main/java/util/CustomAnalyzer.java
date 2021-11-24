package util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class CustomAnalyzer extends Analyzer{

    @Override
    protected TokenStreamComponents createComponents(final String fieldName) {
        final StandardTokenizer tokenizer = new StandardTokenizer();
        TokenStream tokenStream = new LowerCaseFilter(tokenizer);
        CharArraySet stopwordSet = processStopwords(Constants.STOPWORDS_FILEPATH);
        tokenStream = new StopFilter(tokenStream, stopwordSet);
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
}
