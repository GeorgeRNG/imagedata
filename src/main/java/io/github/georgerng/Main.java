package io.github.georgerng;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("Need a file.");
            System.exit(1);
            return;
        }
        if(args.length > 1) {
            System.err.println("Too many arguments.");
            System.exit(1);
            return;
        }
        String path = args[0];
        File file = new File(path);
        if(!file.canRead()) {
            System.err.println("Cannot read this file, or, it doesn't exist.");
            System.exit(1);
            return;
        }
        BufferedImage image = ImageIO.read(file);
        var data = divideArray(((DataBufferByte) image.getRaster().getDataBuffer()).getData(), image.getWidth());

        System.out.println(data);
    }

    public static List<byte[]> divideArray(byte[] source, int chunksize) {

        List<byte[]> result = new ArrayList<byte[]>();
        int start = 0;
        while (start < source.length) {
            int end = Math.min(source.length, start + chunksize);
            result.add(Arrays.copyOfRange(source, start, end));
            start += chunksize;
        }

        return result;
    }
}