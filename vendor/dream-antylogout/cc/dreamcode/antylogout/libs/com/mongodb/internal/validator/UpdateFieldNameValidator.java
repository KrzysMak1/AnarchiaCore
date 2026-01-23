package cc.dreamcode.antylogout.libs.com.mongodb.internal.validator;

import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;

public class UpdateFieldNameValidator implements FieldNameValidator
{
    private int numFields;
    
    public UpdateFieldNameValidator() {
        this.numFields = 0;
    }
    
    @Override
    public boolean validate(final String fieldName) {
        ++this.numFields;
        return fieldName.startsWith("$");
    }
    
    @Override
    public String getValidationErrorMessage(final String fieldName) {
        Assertions.assertFalse(fieldName.startsWith("$"));
        return String.format("All update operators must start with '$', but '%s' does not", new Object[] { fieldName });
    }
    
    @Override
    public FieldNameValidator getValidatorForField(final String fieldName) {
        return new NoOpFieldNameValidator();
    }
    
    @Override
    public void start() {
        this.numFields = 0;
    }
    
    @Override
    public void end() {
        if (this.numFields == 0) {
            throw new IllegalArgumentException("Invalid BSON document for an update. The document may not be empty.");
        }
    }
}
