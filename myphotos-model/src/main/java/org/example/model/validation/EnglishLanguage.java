package org.example.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {EnglishLanguageConstraintValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface EnglishLanguage {

    String message() default "{javax.validation.constraints.EnglishLanguage.message}";

    boolean withNumbers() default true;

    boolean withPunctuation() default true;

    boolean withSpecialSymbols() default true;

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
}
