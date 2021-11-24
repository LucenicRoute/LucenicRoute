package parseQuery;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;

import util.Constants;
import util.CustomAnalyzer;

public class QueryReader {
    public List<Query> queries;
    public List<String> queryIDs;

    public QueryReader() {
        try {
            constructQueries(Constants.TOPIC_FILEPATH);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public List<Query> retrieveQueries() {
        return this.queries;
    }

    public List<String> getQueryIDs() {
        return this.queryIDs;
    }

    public void constructQueries(final String filepath) throws IOException, ParseException {
        final ArrayList<Query> returnQueries = new ArrayList<Query>();
        final ArrayList<String> returnQueryIDs = new ArrayList<String>();

        final List<HashMap<String, String>> rawQueries = parseQueries(filepath);
        for (HashMap<String,String> rawQuery : rawQueries) {
            returnQueryIDs.add(rawQuery.get(Constants.NUM_FIELD_KEY));
        }

        final List<String> processedQueries = processQueries(rawQueries);
        
        final String[] queryFields = new String[] {Constants.TITLE_QUERY_FIELD, Constants.CONTENT_QUERY_FIELD, Constants.AUTHOR_QUERY_FIELD};
        final MultiFieldQueryParser parser = new MultiFieldQueryParser(queryFields, new CustomAnalyzer());
        for (String processedQuery : processedQueries) {
            processedQuery = MultiFieldQueryParser.escape(processedQuery);
            final Query query = parser.parse(processedQuery);
            returnQueries.add(query);
        }
        this.queries = returnQueries;
        this.queryIDs = returnQueryIDs;
    }

    public static List<HashMap<String, String>> parseQueries(final String filepath) throws IOException {
        final Path path = Paths.get(filepath);
        final Scanner scanner =  new Scanner(path, Constants.ENCODING.name());
        final List<HashMap<String, String>> rawQueries = new ArrayList<HashMap<String,String>>();

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String,String> currentTopic = new HashMap<String,String>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (line.startsWith(Constants.QUERY_NUM_FIELD_PREFIX)) {
                currentTopic.put(Constants.NUM_FIELD_KEY, line.substring(Constants.QUERY_NUM_FIELD_PREFIX.length()));
            } else if (line.startsWith(Constants.QUERY_TITLE_FIELD_PREFIX)) {
                currentTopic.put(Constants.TITLE_FIELD_KEY, line.substring(Constants.QUERY_TITLE_FIELD_PREFIX.length()));
            } else if (line.startsWith(Constants.QUERY_DESC_FIELD_PREFIX)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(line.substring(Constants.QUERY_DESC_FIELD_PREFIX.length()));
            } else if (line.startsWith(Constants.QUERY_NARR_FIELD_PREFIX)) {
                currentTopic.put(Constants.DESC_FIELD_KEY, stringBuilder.toString());
                stringBuilder = new StringBuilder();
                stringBuilder.append(line.substring(Constants.QUERY_NARR_FIELD_PREFIX.length()));
            } else if (line.startsWith(Constants.END_OF_QUERY_PREFIX)) {
                currentTopic.put(Constants.NARR_FIELD_KEY, stringBuilder.toString());
                rawQueries.add(currentTopic);
                currentTopic = new HashMap<String,String>();
                stringBuilder = new StringBuilder();
            } else {
                stringBuilder.append(line).append(" ");
            }
        }

        scanner.close();
        return rawQueries;
    }

    /*
    *  The title, description and narrative fields are all processed seperately. They are converted to lowercase and stopwords are removed.
    *  The query string is then formed from the concatenation of these three processed strings.
    *
    *  For example, for the first topic the resulting query string will be: "foreign minorities germany what language cultural differences 
    *  impede integration foreign minorities germany relevant document focus causes lack integration significant way mere mention immigration 
    *  difficulties relevant documents discuss immigration problems unrelated germany also relevant "
    */
    public static List<String> processQueries(final List<HashMap<String,String>> rawQueries) throws IOException {
        final ArrayList<String> processedQueries = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (final HashMap<String,String> rawQuery : rawQueries) {
            final String processedTitle = removeStopWords(rawQuery.get(Constants.TITLE_FIELD_KEY));
            final String processedDesc = removeStopWords(rawQuery.get(Constants.DESC_FIELD_KEY));
            final String processedNarr = removeStopWords(rawQuery.get(Constants.NARR_FIELD_KEY));
            stringBuilder.append(processedTitle);
            stringBuilder.append(processedDesc);
            stringBuilder.append(processedNarr);
            processedQueries.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
        return processedQueries;
    }

    // Removes stop words from a given string
    // Converts string to lowercase
    // Removes punctuation
    public static String removeStopWords(final String text) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        final Analyzer analyzer = new CustomAnalyzer();
        final TokenStream tokenStream = analyzer.tokenStream("CONTENTS", new StringReader(text));
        final CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while(tokenStream.incrementToken()) {
            stringBuilder.append(term.toString()).append(" ");
        }
        tokenStream.end();
        tokenStream.close();
        analyzer.close();
        return stringBuilder.toString();
    }
}
