package cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging;

class NoOpLogger implements Logger
{
    private final String name;
    
    NoOpLogger(final String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
}
