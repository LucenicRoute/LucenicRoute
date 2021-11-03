import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.search.Query;

public class QueryReader {

    public static List<Query> getQueries(final String filepath) throws IOException {
        final ArrayList<Query> returnQueries = new ArrayList<Query>();

    }

    public static List<HashMap<String,String>> parseQueries(final String filepath) throws IOException {
        final Path path = Paths.get(filepath);
        final Scanner scanner =  new Scanner(path, Constants.ENCODING.name());
        final ArrayList<HashMap<String,String>> rawQueries = new ArrayList<HashMap<String,String>>();

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String,String> currentTopic = new HashMap<String,String>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (line.startsWith(Constants.QUERY_NUM_FIELD_PREFIX)) {
                currentTopic.put(Constants.QUERY_NUM_FIELD_PREFIX, line.substring(Constants.QUERY_NUM_FIELD_PREFIX.length()));
            } else if (line.startsWith(Constants.QUERY_TITLE_FIELD_PREFIX)) {
                currentTopic.put(Constants.QUERY_TITLE_FIELD_PREFIX, line.substring(Constants.QUERY_TITLE_FIELD_PREFIX.length()));
            } else if (line.startsWith(Constants.QUERY_DESC_FIELD_PREFIX)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(line.substring(Constants.QUERY_DESC_FIELD_PREFIX.length()));
            } else if (line.startsWith(Constants.QUERY_NARR_FIELD_PREFIX)) {
                currentTopic.put(Constants.QUERY_DESC_FIELD_PREFIX, stringBuilder.toString());
                stringBuilder = new StringBuilder();
                stringBuilder.append(line.substring(Constants.QUERY_NARR_FIELD_PREFIX.length()));
            } else if (line.startsWith(Constants.END_OF_QUERY_PREFIX)) {
                currentTopic.put(Constants.QUERY_NARR_FIELD_PREFIX, stringBuilder.toString());
                stringBuilder = new StringBuilder();
            } else {
                stringBuilder.append(line).append(" ");
            }
        }

        scanner.close();
        return rawQueries;
    }

    public static List<String> processQueries(final List<HashMap<String,String>> rawQueries) {

    }
}
