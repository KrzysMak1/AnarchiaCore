package cc.dreamcode.antylogout.libs.com.mongodb.internal.validator;

import java.util.Map;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;

public class MappedFieldNameValidator implements FieldNameValidator
{
    private final FieldNameValidator defaultValidator;
    private final Map<String, FieldNameValidator> fieldNameToValidatorMap;
    
    public MappedFieldNameValidator(final FieldNameValidator defaultValidator, final Map<String, FieldNameValidator> fieldNameToValidatorMap) {
        this.defaultValidator = defaultValidator;
        this.fieldNameToValidatorMap = fieldNameToValidatorMap;
    }
    
    @Override
    public boolean validate(final String fieldName) {
        return this.defaultValidator.validate(fieldName);
    }
    
    @Override
    public String getValidationErrorMessage(final String fieldName) {
        return this.defaultValidator.getValidationErrorMessage(fieldName);
    }
    
    @Override
    public FieldNameValidator getValidatorForField(final String fieldName) {
        if (this.fieldNameToValidatorMap.containsKey((Object)fieldName)) {
            return (FieldNameValidator)this.fieldNameToValidatorMap.get((Object)fieldName);
        }
        return this.defaultValidator;
    }
}
