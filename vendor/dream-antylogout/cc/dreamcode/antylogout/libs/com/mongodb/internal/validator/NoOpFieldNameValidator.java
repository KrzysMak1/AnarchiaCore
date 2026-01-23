package cc.dreamcode.antylogout.libs.com.mongodb.internal.validator;

import cc.dreamcode.antylogout.libs.org.bson.FieldNameValidator;

public class NoOpFieldNameValidator implements FieldNameValidator
{
    @Override
    public boolean validate(final String fieldName) {
        return true;
    }
    
    @Override
    public FieldNameValidator getValidatorForField(final String fieldName) {
        return this;
    }
}
