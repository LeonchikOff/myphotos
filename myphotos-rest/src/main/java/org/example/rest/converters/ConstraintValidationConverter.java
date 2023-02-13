package org.example.rest.converters;

import org.example.common.model.util.ListMap;
import org.example.rest.model.ValidationItemRest;
import org.example.rest.model.ValidationResultRest;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class ConstraintValidationConverter {

    public <T> ValidationResultRest convert(Set<ConstraintViolation<T>> violations) {
        ListMap<String, String> listMap = new ListMap<>();
        violations.forEach(constraintViolation
                -> constraintViolation.getPropertyPath().forEach(node
                -> listMap.add(node.getName(), constraintViolation.getMessage())));
        return createValidationResult(listMap);
    }

    private ValidationResultRest createValidationResult(ListMap<String, String> listMap) {
        List<ValidationItemRest> itemRestList = new ArrayList<>();
        Map<String, List<String>> mapKeyAndListValues = listMap.toMapKeyAndListValues();
        mapKeyAndListValues.forEach((field, listViolationMsg)
                -> itemRestList.add(new ValidationItemRest(field, listViolationMsg)));
        return new ValidationResultRest(itemRestList);
    }
}
