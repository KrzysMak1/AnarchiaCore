package cc.dreamcode.antylogout.libs.com.mongodb;

import java.util.List;

interface DBObjectFactory
{
    DBObject getInstance();
    
    DBObject getInstance(final List<String> p0);
}
