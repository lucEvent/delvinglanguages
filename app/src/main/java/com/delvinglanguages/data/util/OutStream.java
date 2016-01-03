package com.delvinglanguages.data.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class OutStream extends ObjectOutputStream {

    public OutStream(File file) throws IOException {
        super(new FileOutputStream(file));
    }

    @Override
    public void close() throws IOException {
        super.flush();
        super.close();
    }

    public void writeString(String string) throws IOException {
        writeObject(string);
    }
}
