//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Admin\Desktop\Minecraft-Deobfuscator3000-1.2.2\1.12 stable mappings"!

//Decompiled by Procyon!

package okio;

import java.nio.charset.*;
import java.io.*;

final class RealBufferedSource implements BufferedSource
{
    public final Buffer buffer;
    public final Source source;
    private boolean closed;
    
    public RealBufferedSource(final Source source, final Buffer buffer) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        this.buffer = buffer;
        this.source = source;
    }
    
    public RealBufferedSource(final Source source) {
        this(source, new Buffer());
    }
    
    public Buffer buffer() {
        return this.buffer;
    }
    
    public long read(final Buffer sink, final long byteCount) throws IOException {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        if (byteCount < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        }
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (this.buffer.size == 0L) {
            final long read = this.source.read(this.buffer, 2048L);
            if (read == -1L) {
                return -1L;
            }
        }
        final long toRead = Math.min(byteCount, this.buffer.size);
        return this.buffer.read(sink, toRead);
    }
    
    public boolean exhausted() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        return this.buffer.exhausted() && this.source.read(this.buffer, 2048L) == -1L;
    }
    
    public void require(final long byteCount) throws IOException {
        if (!this.request(byteCount)) {
            throw new EOFException();
        }
    }
    
    public boolean request(final long byteCount) throws IOException {
        if (byteCount < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        }
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (this.buffer.size < byteCount) {
            if (this.source.read(this.buffer, 2048L) == -1L) {
                return false;
            }
        }
        return true;
    }
    
    public byte readByte() throws IOException {
        this.require(1L);
        return this.buffer.readByte();
    }
    
    public ByteString readByteString() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteString();
    }
    
    public ByteString readByteString(final long byteCount) throws IOException {
        this.require(byteCount);
        return this.buffer.readByteString(byteCount);
    }
    
    public byte[] readByteArray() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteArray();
    }
    
    public byte[] readByteArray(final long byteCount) throws IOException {
        this.require(byteCount);
        return this.buffer.readByteArray(byteCount);
    }
    
    public int read(final byte[] sink) throws IOException {
        return this.read(sink, 0, sink.length);
    }
    
    public void readFully(final byte[] sink) throws IOException {
        try {
            this.require(sink.length);
        }
        catch (EOFException e) {
            int offset = 0;
            while (this.buffer.size > 0L) {
                final int read = this.buffer.read(sink, offset, (int)this.buffer.size);
                if (read == -1) {
                    throw new AssertionError();
                }
                offset += read;
            }
            throw e;
        }
        this.buffer.readFully(sink);
    }
    
    public int read(final byte[] sink, final int offset, final int byteCount) throws IOException {
        Util.checkOffsetAndCount(sink.length, offset, byteCount);
        if (this.buffer.size == 0L) {
            final long read = this.source.read(this.buffer, 2048L);
            if (read == -1L) {
                return -1;
            }
        }
        final int toRead = (int)Math.min(byteCount, this.buffer.size);
        return this.buffer.read(sink, offset, toRead);
    }
    
    public void readFully(final Buffer sink, final long byteCount) throws IOException {
        try {
            this.require(byteCount);
        }
        catch (EOFException e) {
            sink.writeAll((Source)this.buffer);
            throw e;
        }
        this.buffer.readFully(sink, byteCount);
    }
    
    public long readAll(final Sink sink) throws IOException {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        long totalBytesWritten = 0L;
        while (this.source.read(this.buffer, 2048L) != -1L) {
            final long emitByteCount = this.buffer.completeSegmentByteCount();
            if (emitByteCount > 0L) {
                totalBytesWritten += emitByteCount;
                sink.write(this.buffer, emitByteCount);
            }
        }
        if (this.buffer.size() > 0L) {
            totalBytesWritten += this.buffer.size();
            sink.write(this.buffer, this.buffer.size());
        }
        return totalBytesWritten;
    }
    
    public String readUtf8() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readUtf8();
    }
    
    public String readUtf8(final long byteCount) throws IOException {
        this.require(byteCount);
        return this.buffer.readUtf8(byteCount);
    }
    
    public String readString(final Charset charset) throws IOException {
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        this.buffer.writeAll(this.source);
        return this.buffer.readString(charset);
    }
    
    public String readString(final long byteCount, final Charset charset) throws IOException {
        this.require(byteCount);
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        return this.buffer.readString(byteCount, charset);
    }
    
    public String readUtf8Line() throws IOException {
        final long newline = this.indexOf((byte)10);
        if (newline == -1L) {
            return (this.buffer.size != 0L) ? this.readUtf8(this.buffer.size) : null;
        }
        return this.buffer.readUtf8Line(newline);
    }
    
    public String readUtf8LineStrict() throws IOException {
        final long newline = this.indexOf((byte)10);
        if (newline == -1L) {
            final Buffer data = new Buffer();
            this.buffer.copyTo(data, 0L, Math.min(32L, this.buffer.size()));
            throw new EOFException("\\n not found: size=" + this.buffer.size() + " content=" + data.readByteString().hex() + "...");
        }
        return this.buffer.readUtf8Line(newline);
    }
    
    public int readUtf8CodePoint() throws IOException {
        this.require(1L);
        final byte b0 = this.buffer.getByte(0L);
        if ((b0 & 0xE0) == 0xC0) {
            this.require(2L);
        }
        else if ((b0 & 0xF0) == 0xE0) {
            this.require(3L);
        }
        else if ((b0 & 0xF8) == 0xF0) {
            this.require(4L);
        }
        return this.buffer.readUtf8CodePoint();
    }
    
    public short readShort() throws IOException {
        this.require(2L);
        return this.buffer.readShort();
    }
    
    public short readShortLe() throws IOException {
        this.require(2L);
        return this.buffer.readShortLe();
    }
    
    public int readInt() throws IOException {
        this.require(4L);
        return this.buffer.readInt();
    }
    
    public int readIntLe() throws IOException {
        this.require(4L);
        return this.buffer.readIntLe();
    }
    
    public long readLong() throws IOException {
        this.require(8L);
        return this.buffer.readLong();
    }
    
    public long readLongLe() throws IOException {
        this.require(8L);
        return this.buffer.readLongLe();
    }
    
    public long readDecimalLong() throws IOException {
        this.require(1L);
        int pos = 0;
        while (this.request(pos + 1)) {
            final byte b = this.buffer.getByte((long)pos);
            if ((b < 48 || b > 57) && (pos != 0 || b != 45)) {
                if (pos == 0) {
                    throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", b));
                }
                break;
            }
            else {
                ++pos;
            }
        }
        return this.buffer.readDecimalLong();
    }
    
    public long readHexadecimalUnsignedLong() throws IOException {
        this.require(1L);
        int pos = 0;
        while (this.request(pos + 1)) {
            final byte b = this.buffer.getByte((long)pos);
            if ((b < 48 || b > 57) && (b < 97 || b > 102) && (b < 65 || b > 70)) {
                if (pos == 0) {
                    throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", b));
                }
                break;
            }
            else {
                ++pos;
            }
        }
        return this.buffer.readHexadecimalUnsignedLong();
    }
    
    public void skip(long byteCount) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (byteCount > 0L) {
            if (this.buffer.size == 0L && this.source.read(this.buffer, 2048L) == -1L) {
                throw new EOFException();
            }
            final long toSkip = Math.min(byteCount, this.buffer.size());
            this.buffer.skip(toSkip);
            byteCount -= toSkip;
        }
    }
    
    public long indexOf(final byte b) throws IOException {
        return this.indexOf(b, 0L);
    }
    
    public long indexOf(final byte b, long fromIndex) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (fromIndex >= this.buffer.size) {
            if (this.source.read(this.buffer, 2048L) == -1L) {
                return -1L;
            }
        }
        long index;
        while ((index = this.buffer.indexOf(b, fromIndex)) == -1L) {
            fromIndex = this.buffer.size;
            if (this.source.read(this.buffer, 2048L) == -1L) {
                return -1L;
            }
        }
        return index;
    }
    
    public long indexOf(final ByteString bytes) throws IOException {
        return this.indexOf(bytes, 0L);
    }
    
    public long indexOf(final ByteString bytes, long fromIndex) throws IOException {
        if (bytes.size() == 0) {
            throw new IllegalArgumentException("bytes is empty");
        }
        while (true) {
            fromIndex = this.indexOf(bytes.getByte(0), fromIndex);
            if (fromIndex == -1L) {
                return -1L;
            }
            if (this.rangeEquals(fromIndex, bytes)) {
                return fromIndex;
            }
            ++fromIndex;
        }
    }
    
    public long indexOfElement(final ByteString targetBytes) throws IOException {
        return this.indexOfElement(targetBytes, 0L);
    }
    
    public long indexOfElement(final ByteString targetBytes, long fromIndex) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (fromIndex >= this.buffer.size) {
            if (this.source.read(this.buffer, 2048L) == -1L) {
                return -1L;
            }
        }
        long index;
        while ((index = this.buffer.indexOfElement(targetBytes, fromIndex)) == -1L) {
            fromIndex = this.buffer.size;
            if (this.source.read(this.buffer, 2048L) == -1L) {
                return -1L;
            }
        }
        return index;
    }
    
    private boolean rangeEquals(final long offset, final ByteString bytes) throws IOException {
        return this.request(offset + bytes.size()) && this.buffer.rangeEquals(offset, bytes);
    }
    
    public InputStream inputStream() {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                }
                if (RealBufferedSource.this.buffer.size == 0L) {
                    final long count = RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 2048L);
                    if (count == -1L) {
                        return -1;
                    }
                }
                return RealBufferedSource.this.buffer.readByte() & 0xFF;
            }
            
            @Override
            public int read(final byte[] data, final int offset, final int byteCount) throws IOException {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                }
                Util.checkOffsetAndCount(data.length, offset, byteCount);
                if (RealBufferedSource.this.buffer.size == 0L) {
                    final long count = RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 2048L);
                    if (count == -1L) {
                        return -1;
                    }
                }
                return RealBufferedSource.this.buffer.read(data, offset, byteCount);
            }
            
            @Override
            public int available() throws IOException {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                }
                return (int)Math.min(RealBufferedSource.this.buffer.size, 2147483647L);
            }
            
            @Override
            public void close() throws IOException {
                RealBufferedSource.this.close();
            }
            
            @Override
            public String toString() {
                return RealBufferedSource.this + ".inputStream()";
            }
        };
    }
    
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.source.close();
        this.buffer.clear();
    }
    
    public Timeout timeout() {
        return this.source.timeout();
    }
    
    @Override
    public String toString() {
        return "buffer(" + this.source + ")";
    }
}
