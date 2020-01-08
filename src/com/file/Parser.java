package com.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Parser {

    private static int offset = 0;
    private static int line = 1;
    private static byte[] bytes;

    public static void openFile(String filename) {
        File tmp = new File("levels/" + filename + ".zip");
        if (!tmp.exists()) return;

        try {
            ZipFile zipFile = new ZipFile("levels/" + filename + ".zip");
            ZipEntry jsonFile = zipFile.getEntry(filename + ".json");
            InputStream stream = zipFile.getInputStream(jsonFile);

            Parser.bytes = stream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void skipWhitespace() {
        char c = '\0';
        while (!atEnd() && (peek() == ' ' || peek() == '\t' || peek() == '\n' || peek() == '\r' || peek() == '\f')) {
            if (peek() == '\n') Parser.line++;
            advance();
        }
    }

    public static boolean parseBoolean() {
        skipWhitespace();
        StringBuilder builder = new StringBuilder();

        if (!atEnd() && peek() == 't') {
            builder.append("true");
            consume('t');
            consume('r');
            consume('u');
            consume('e');
        } else if (!atEnd() && peek() == 'f') {
            builder.append("false");
            consume('f');
            consume('a');
            consume('l');
            consume('s');
            consume('e');
        } else {
            System.out.println("Error: Expected 'true' or 'false'. Instead got '" + peek() + "' at line: " + Parser.line);
            System.exit(-1);
        }

        return builder.toString().compareTo("true") == 0;
    }

    public static double parseDouble() {
        skipWhitespace();
        char c;
        StringBuilder builder = new StringBuilder();

        while (!atEnd() && isDigit(peek()) || peek() == '.' || peek() == '-') {
            c = advance();
            builder.append(c);
        }

        return Double.parseDouble(builder.toString());
    }

    public static String parseString() {
        skipWhitespace();
        char c;
        StringBuilder builder = new StringBuilder();
        consume('"');

        while (!atEnd() && peek() != '"') {
            c = advance();
            builder.append(c);
        }
        consume('"');

        return builder.toString();
    }

    public static double parseFloat() {
        skipWhitespace();
        char c;
        StringBuilder builder = new StringBuilder();

        while (!atEnd() && isDigit(peek()) || peek() == '.' || peek() == '-') {
            c = advance();
            builder.append(c);
        }
        builder.append('f');
        consume('f');

        return Float.parseFloat(builder.toString());
    }

    public static int parseInt() {
        skipWhitespace();
        char c;
        StringBuilder builder = new StringBuilder();

        while (!atEnd() && isDigit(peek()) || peek() == '-') {
            c = advance();
            builder.append(c);
        }

        return Integer.parseInt(builder.toString());
    }

    private static void consume(char c) {
        char actual = (char)bytes[offset];
        if (actual != c) {
            System.out.println("Error: Expected '" + c + "' instead got '" + actual + "' at line: " + Parser.line);
            System.exit(-1);
        }
        offset++;
    }

    private static char advance() {
        char c = (char)bytes[offset];
        offset++;
        return c;
    }

    private static char peek() {
        if (atEnd()) return '\0';
        return (char)bytes[offset];
    }

    private static boolean atEnd() {
        return offset == bytes.length;
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
