import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constants {
    // Query constants
    public static final String TITLE_QUERY_FIELD = "Title";
    public static final String CONTENT_QUERY_FIELD = "Content";
    public static final String AUTHOR_QUERY_FIELD = "Author";

    // File parsing constants
    public static final Charset ENCODING = StandardCharsets.UTF_8;
    public static final String QUERY_NUM_FIELD_PREFIX = "<num> Number: ";
    public static final String QUERY_TITLE_FIELD_PREFIX = "<title> ";
    public static final String QUERY_DESC_FIELD_PREFIX = "<desc> Description:";
    public static final String QUERY_NARR_FIELD_PREFIX = "<narr> Narrative:";
    public static final String END_OF_QUERY_PREFIX = "</top>";

    // TODO: update to relative path
    public static final String TOPIC_FILEPATH = "/Users/chrisnixon/Documents/yr5/IR/LucenicRoute/LucenicRoute/LucenicRoute/src/main/resources/topics";
    public static final String NUM_FIELD_KEY = "num";
    public static final String TITLE_FIELD_KEY = "title";
    public static final String DESC_FIELD_KEY = "desc";
    public static final String NARR_FIELD_KEY = "narr";
}
