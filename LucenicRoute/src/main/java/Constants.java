import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constants {
    // File parsing constants
    public static final Charset ENCODING = StandardCharsets.UTF_8;
    public static final String QUERY_NUM_FIELD_PREFIX = "<num> Number: ";
    public static final String QUERY_TITLE_FIELD_PREFIX = "<title> ";
    public static final String QUERY_DESC_FIELD_PREFIX = "<desc> Description:";
    public static final String QUERY_NARR_FIELD_PREFIX = "<narr> Narrative:";
    public static final String END_OF_QUERY_PREFIX = "</top>";
}
