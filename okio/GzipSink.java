//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Admin\Desktop\Minecraft-Deobfuscator3000-1.2.2\1.12 stable mappings"!

//Decompiled by Procyon!

package okio;

import java.util.zip.*;
import java.io.*;

public final class GzipSink implements Sink
{
    private final BufferedSink sink;
    private final Deflater deflater;
    private final DeflaterSink deflaterSink;
    private boolean closed;
    private final CRC32 crc;
    
    public GzipSink(final Sink sink) {
        this.crc = new CRC32();
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        this.deflater = new Deflater(-1, true);
        this.sink = Okio.buffer(sink);
        this.deflaterSink = new DeflaterSink(this.sink, this.deflater);
        this.writeHeader();
    }
    
    @Override
    public void write(final Buffer source, final long byteCount) throws IOException {
        if (byteCount < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        }
        if (byteCount == 0L) {
            return;
        }
        this.updateCrc(source, byteCount);
        this.deflaterSink.write(source, byteCount);
    }
    
    @Override
    public void flush() throws IOException {
        this.deflaterSink.flush();
    }
    
    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }
    
    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        Throwable thrown = null;
        try {
            this.deflaterSink.finishDeflate();
            this.writeFooter();
        }
        catch (Throwable e) {
            thrown = e;
        }
        try {
            this.deflater.end();
        }
        catch (Throwable e) {
            if (thrown == null) {
                thrown = e;
            }
        }
        try {
            this.sink.close();
        }
        catch (Throwable e) {
            if (thrown == null) {
                thrown = e;
            }
        }
        this.closed = true;
        if (thrown != null) {
            Util.sneakyRethrow(thrown);
        }
    }
    
    private void writeHeader() {
        final Buffer buffer = this.sink.buffer();
        buffer.writeShort(8075);
        buffer.writeByte(8);
        buffer.writeByte(0);
        buffer.writeInt(0);
        buffer.writeByte(0);
        buffer.writeByte(0);
    }
    
    private void writeFooter() throws IOException {
        this.sink.writeIntLe((int)this.crc.getValue());
        this.sink.writeIntLe(this.deflater.getTotalIn());
    }
    
    private void updateCrc(final Buffer buffer, long byteCount) {
        int segmentLength;
        for (Segment head = buffer.head; byteCount > 0L; byteCount -= segmentLength, head = head.next) {
            segmentLength = (int)Math.min(byteCount, head.limit - head.pos);
            this.crc.update(head.data, head.pos, segmentLength);
        }
    }
}
