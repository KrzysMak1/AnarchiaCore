package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.impl;

import java.nio.Buffer;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLProtocolException;
import java.util.HashMap;
import javax.net.ssl.SNIServerName;
import java.util.Map;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public final class TlsExplorer
{
    public static final int RECORD_HEADER_SIZE = 5;
    
    private TlsExplorer() {
    }
    
    public static int getRequiredSize(final ByteBuffer source) {
        if (source.remaining() < 5) {
            throw new BufferUnderflowException();
        }
        ((Buffer)source).mark();
        try {
            final byte firstByte = source.get();
            source.get();
            final byte thirdByte = source.get();
            if ((firstByte & 0x80) != 0x0 && thirdByte == 1) {
                return 5;
            }
            return ((source.get() & 0xFF) << 8 | (source.get() & 0xFF)) + 5;
        }
        finally {
            ((Buffer)source).reset();
        }
    }
    
    public static Map<Integer, SNIServerName> explore(final ByteBuffer source) throws SSLProtocolException {
        if (source.remaining() < 5) {
            throw new BufferUnderflowException();
        }
        ((Buffer)source).mark();
        try {
            final byte firstByte = source.get();
            ignore(source, 1);
            final byte thirdByte = source.get();
            if ((firstByte & 0x80) != 0x0 && thirdByte == 1) {
                return (Map<Integer, SNIServerName>)new HashMap();
            }
            if (firstByte == 22) {
                return exploreTLSRecord(source, firstByte);
            }
            throw new SSLProtocolException("Not handshake record");
        }
        finally {
            ((Buffer)source).reset();
        }
    }
    
    private static Map<Integer, SNIServerName> exploreTLSRecord(final ByteBuffer input, final byte firstByte) throws SSLProtocolException {
        if (firstByte != 22) {
            throw new SSLProtocolException("Not handshake record");
        }
        final int recordLength = getInt16(input);
        if (recordLength > input.remaining()) {
            throw new BufferUnderflowException();
        }
        return exploreHandshake(input, recordLength);
    }
    
    private static Map<Integer, SNIServerName> exploreHandshake(final ByteBuffer input, final int recordLength) throws SSLProtocolException {
        final byte handshakeType = input.get();
        if (handshakeType != 1) {
            throw new SSLProtocolException("Not initial handshaking");
        }
        final int handshakeLength = getInt24(input);
        if (handshakeLength > recordLength - 4) {
            throw new SSLProtocolException("Handshake message spans multiple records");
        }
        ((Buffer)input).limit(handshakeLength + input.position());
        return exploreClientHello(input);
    }
    
    private static Map<Integer, SNIServerName> exploreClientHello(final ByteBuffer input) throws SSLProtocolException {
        ignore(input, 2);
        ignore(input, 32);
        ignoreByteVector8(input);
        ignoreByteVector16(input);
        ignoreByteVector8(input);
        if (input.remaining() > 0) {
            return exploreExtensions(input);
        }
        return (Map<Integer, SNIServerName>)new HashMap();
    }
    
    private static Map<Integer, SNIServerName> exploreExtensions(final ByteBuffer input) throws SSLProtocolException {
        int extLen;
        for (int length = getInt16(input); length > 0; length -= extLen + 4) {
            final int extType = getInt16(input);
            extLen = getInt16(input);
            if (extType == 0) {
                return exploreSNIExt(input, extLen);
            }
            ignore(input, extLen);
        }
        return (Map<Integer, SNIServerName>)new HashMap();
    }
    
    private static Map<Integer, SNIServerName> exploreSNIExt(final ByteBuffer input, final int extLen) throws SSLProtocolException {
        final Map<Integer, SNIServerName> sniMap = (Map<Integer, SNIServerName>)new HashMap();
        int remains = extLen;
        if (extLen >= 2) {
            final int listLen = getInt16(input);
            if (listLen == 0 || listLen + 2 != extLen) {
                throw new SSLProtocolException("Invalid server name indication extension");
            }
            byte[] encoded;
            for (remains -= 2; remains > 0; remains -= encoded.length + 3) {
                final int code = getInt8(input);
                final int snLen = getInt16(input);
                if (snLen > remains) {
                    throw new SSLProtocolException("Not enough data to fill declared vector size");
                }
                encoded = new byte[snLen];
                input.get(encoded);
                SNIServerName serverName;
                if (code == 0) {
                    if (encoded.length == 0) {
                        throw new SSLProtocolException("Empty HostName in server name indication");
                    }
                    serverName = (SNIServerName)new SNIHostName(encoded);
                }
                else {
                    serverName = new UnknownServerName(code, encoded);
                }
                if (sniMap.put((Object)serverName.getType(), (Object)serverName) != null) {
                    throw new SSLProtocolException("Duplicated server name of type " + serverName.getType());
                }
            }
        }
        else if (extLen == 0) {
            throw new SSLProtocolException("Not server name indication extension in client");
        }
        if (remains != 0) {
            throw new SSLProtocolException("Invalid server name indication extension");
        }
        return sniMap;
    }
    
    private static int getInt8(final ByteBuffer input) {
        return input.get();
    }
    
    private static int getInt16(final ByteBuffer input) {
        return (input.get() & 0xFF) << 8 | (input.get() & 0xFF);
    }
    
    private static int getInt24(final ByteBuffer input) {
        return (input.get() & 0xFF) << 16 | (input.get() & 0xFF) << 8 | (input.get() & 0xFF);
    }
    
    private static void ignoreByteVector8(final ByteBuffer input) {
        ignore(input, getInt8(input));
    }
    
    private static void ignoreByteVector16(final ByteBuffer input) {
        ignore(input, getInt16(input));
    }
    
    private static void ignore(final ByteBuffer input, final int length) {
        if (length != 0) {
            ((Buffer)input).position(input.position() + length);
        }
    }
    
    private static class UnknownServerName extends SNIServerName
    {
        UnknownServerName(final int code, final byte[] encoded) {
            super(code, encoded);
        }
    }
}
