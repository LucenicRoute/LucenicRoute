package util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Anuradha Vishwakarma
 * Constants file 
 */
public final class Constants {

    //Arguments
    public final static String BUILD_INDEX_SHORT = "-ci";
    public final static String BUILD_INDEX_LONG = "--create-index";
	
	public final static Integer MAX_RESULTS = 1400;
	public final static String CONTENT = "Content";
	public final static String DOCUMENTID = "DocumentId";
	public final static String TITLE = "Title";
	public final static String AUTHOR = "Author";
	public static final String INDEX_DIRECTORY = "index/";

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

    public static final String TOPIC_FILEPATH = "Input/topics";
    public static final String NUM_FIELD_KEY = "num";
    public static final String TITLE_FIELD_KEY = "title";
    public static final String DESC_FIELD_KEY = "desc";
    public static final String NARR_FIELD_KEY = "narr";
	
}
