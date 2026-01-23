package cc.dreamcode.antylogout.libs.com.mongodb.internal.validator;

import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.List;
import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;

public class ReplacingDocumentFieldNameValidator implements FieldNameValidator
{
    private static final NoOpFieldNameValidator NO_OP_FIELD_NAME_VALIDATOR;
    private static final List<String> EXCEPTIONS;
    
    @Override
    public boolean validate(final String fieldName) {
        return !fieldName.startsWith("$") || ReplacingDocumentFieldNameValidator.EXCEPTIONS.contains((Object)fieldName);
    }
    
    @Override
    public String getValidationErrorMessage(final String fieldName) {
        Assertions.assertFalse(this.validate(fieldName));
        return String.format("Field names in a replacement document can not start with '$' but '%s' does", new Object[] { fieldName });
    }
    
    @Override
    public FieldNameValidator getValidatorForField(final String fieldName) {
        return ReplacingDocumentFieldNameValidator.NO_OP_FIELD_NAME_VALIDATOR;
    }
    
    static {
        NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
        EXCEPTIONS = Arrays.asList((Object[])new String[] { "$db", "$ref", "$id" });
    }
}
