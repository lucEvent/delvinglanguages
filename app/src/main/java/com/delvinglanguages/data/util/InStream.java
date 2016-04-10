package com.delvinglanguages.data.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class InStream extends ObjectInputStream {

    public InStream(File file) throws IOException {
        super(new FileInputStream(file));
    }

    public InStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    public String readString() throws IOException, ClassNotFoundException {
        return (String) readObject();
    }

}
