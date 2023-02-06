package org.example.web.utils;

import org.example.model.exception.ValidationException;

public class UrlExtractorUtil {
    private UrlExtractorUtil() {
    }

    public static String getPathVariableValue(String url, String prefix, String suffix) {
        if(url.length() > prefix.length() + suffix.length() && url.startsWith(prefix) && url.endsWith(suffix)) {
                    //   cut out the middle
            return url.substring(prefix.length(), url.length() - suffix.length());
        } else {
            throw new ValidationException(
                    String.format("Can't extract path variable from url=%s with prefix=%s and suffix=%s", url, prefix, suffix));
        }
    }

}
