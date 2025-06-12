package lt.code.samples.maven.common.validation.annotation.fieldsstringcompare.impl;

import java.lang.reflect.Field;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lt.code.samples.maven.common.validation.annotation.fieldsstringcompare.FieldsStringCompare;


public class FieldsStringCompareValidator implements ConstraintValidator<FieldsStringCompare, Object> {

    private String firstField;
    private String secondField;

    @Override
    public void initialize(FieldsStringCompare constraintAnnotation) {
        this.firstField = constraintAnnotation.firstField();
        this.secondField = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            final Class<?> clazz = obj.getClass();
            final String firstFieldValue = getFieldValue(obj, this.firstField, clazz);
            final String secondFieldValue = getFieldValue(obj, this.secondField, clazz);

            return Objects.nonNull(firstFieldValue) &&
                    Objects.nonNull(secondFieldValue) &&
                    firstFieldValue.equals(secondFieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //
        }

        return false;
    }

    private String getFieldValue(final Object obj, final String fieldName, final Class<?> clazz) throws NoSuchFieldException, IllegalAccessException {
        final Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (String) field.get(obj);
    }
}