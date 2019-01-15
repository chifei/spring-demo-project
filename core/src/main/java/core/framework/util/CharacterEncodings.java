package core.framework.util;

import java.nio.charset.Charset;

/**
 * @author neo
 */
public class CharacterEncodings {
    public static final String UTF_8 = "UTF-8";

    public static final Charset CHARSET_UTF_8 = Charset.forName(UTF_8);

    public static final String ISO_8859_1 = "ISO-8859-1";   // used in http url

    public static final Charset CHARSET_ISO_8859_1 = Charset.forName("ISO-8859-1");
}
