package com.tenpay.sm.common.lang.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * �����ݴ�����<code>InputStream</code>���Ƶ�<code>FilterOutputStream</code>���������. ��������ֲ��IBM developer
 * works��������, �μ�package�ĵ�.
 *
 * @author Michael Zhou
 * @version $Id: InputStreamOutputEngine.java 509 2004-02-16 05:42:07Z baobao $
 */
public class InputStreamOutputEngine implements OutputEngine {
    private static final int    DEFAULT_BUFFER_SIZE = 8192;
    private InputStream         in;
    private OutputStreamFactory factory;
    private byte[]              buffer;
    private OutputStream        out;

    public InputStreamOutputEngine(InputStream in, OutputStreamFactory factory) {
        this(in, factory, DEFAULT_BUFFER_SIZE);
    }

    public InputStreamOutputEngine(InputStream in, OutputStreamFactory factory, int bufferSize) {
        this.in          = in;
        this.factory     = (factory == null) ? DEFAULT_OUTPUT_STREAM_FACTORY
                                             : factory;
        buffer = new byte[bufferSize];
    }

    public void open(OutputStream out) throws IOException {
        if (this.out != null) {
            throw new IOException("Already initialized");
        } else {
            this.out = factory.getOutputStream(out);
        }
    }

    public void execute() throws IOException {
        if (out == null) {
            throw new IOException("Not yet initialized");
        } else {
            int amount = in.read(buffer);

            if (amount < 0) {
                out.close();
            } else {
                out.write(buffer, 0, amount);
            }
        }
    }

    public void close() throws IOException {
        in.close();
    }
}
