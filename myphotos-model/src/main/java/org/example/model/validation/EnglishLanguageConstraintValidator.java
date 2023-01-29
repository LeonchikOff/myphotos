package org.example.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnglishLanguageConstraintValidator implements ConstraintValidator<EnglishLanguage, String> {

    private boolean withNumbers;
    private boolean withPunctuation;
    private boolean withSpecialSymbols;

    @Override
    public void initialize(EnglishLanguage constraintAnnotation) {
        this.withNumbers = constraintAnnotation.withNumbers();
        this.withPunctuation = constraintAnnotation.withPunctuation();
        this.withSpecialSymbols = constraintAnnotation.withSpecialSymbols();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String validationTemplate = getValidationTemplate();
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (validationTemplate.indexOf(ch) == -1) {
                return false;
            }
        }
        return true;
    }

    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS = "0123456789";
    public static final String PUNCTUATIONS = ".,?!-:()'\"[]{}; \t\n";
    public static final String SPECIAL_SYMBOL = "#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    private String getValidationTemplate() {
        StringBuilder template = new StringBuilder(LETTERS);
        if (withNumbers) template.append(NUMBERS);
        if (withPunctuation) template.append(PUNCTUATIONS);
        if (withSpecialSymbols) template.append(SPECIAL_SYMBOL);
        return template.toString();
    }
}
