package io.github.georgerng;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        File out = new File("./main.mcfunction");
        
        // TextDisplay.render(new File("./frames"), out);
        Bookshelf.renderFrame(new File("banana.bmp"), out,1);
    }

    public static String padLeft(String number, int zeros) {
        try {
            return "0".repeat(Math.max(0, zeros)).substring(number.length()) + number;
        } catch (Exception ignored) {
            return number;
        }
    }
}