package org.example;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        new YLands().render(new File("data.txt"), new File("frames"),1);
        // TextDisplay.render(new File("./frames"), out);
//        Bookshelf.renderFrames(new File("frames"), "outputframes" ,1);

    }

    public static String padLeft(String number, int zeros) {
        try {
            return "0".repeat(Math.max(0, zeros)).substring(number.length()) + number;
        } catch (Exception ignored) {
            return number;
        }
    }
}