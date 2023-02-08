package org.example.ejb.service.impls;

import net.sf.junidecode.Junidecode;
import org.example.common.cdi.annotation.PropertiesSource;
import org.example.ejb.service.TranslitConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.Properties;

@ApplicationScoped
public class JUniDecodeTranslitConverterImpl implements TranslitConverter {

    private Properties properties;

    @Inject
    public void setProperties(@PropertiesSource(resourceFileName = "classpath:translit.properties") Properties properties) {
        this.properties = extendTranslitPropertiesWithUpperLetters(properties);
    }

    private Properties extendTranslitPropertiesWithUpperLetters(final Properties properties) {
        Properties propertiesResult = (Properties) properties.clone();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            propertiesResult.setProperty(
                    key.toUpperCase(),
                    value.length() <= 1
                            ? value.toUpperCase()
                            : Character.toUpperCase(value.charAt(0)) + value.substring(1));
        }
        return propertiesResult;
    }

    @Override
    public String translit(String text) {
        String result = customTranslit(text);
        return Junidecode.unidecode(result);
    }

    private String customTranslit(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String charAt = String.valueOf(text.charAt(i));
            stringBuilder.append(properties.getProperty(charAt, charAt));
        }
        return stringBuilder.toString();
    }
}
