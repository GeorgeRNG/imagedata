package io.github.georgerng;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Bookshelf {
    public static void renderFrame(File file, File output, int rescale) throws IOException {
        FileWriter writer = new FileWriter(output);

        BufferedImage original = ImageIO.read(file);
        BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g = image.createGraphics();
        g.drawImage(original, 0, 0, original.getWidth(), original.getHeight(), null);
        g.dispose();
        
        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        
        int lineI = 0;
        int pixelI = 0;
        final int width = image.getWidth();
        final int height = image.getHeight();
        for(int i = 0; i < data.length; i += width){
            lineI++;
            // if(lineI % rescale == 0) {
                int[] line = Arrays.copyOfRange(data, i, i+width);
                for (int pixel: line) {
                    pixelI++;

                    Boolean red1   = (pixel & 0x00_FF_00_00) >  (0x00_FF_00_00 / 3)     ;
                    Boolean green1 = (pixel & 0x00_00_FF_00) >  (0x00_00_FF_00 / 3)     ;
                    Boolean blue1  = (pixel & 0x00_00_00_FF) >  (0x00_00_00_FF / 3)     ;
                    Boolean red2   = (pixel & 0x00_FF_00_00) > ((0x00_FF_00_00 / 3) * 2);
                    Boolean green2 = (pixel & 0x00_00_FF_00) > ((0x00_00_FF_00 / 3) * 2);
                    Boolean blue2  = (pixel & 0x00_00_00_FF) > ((0x00_00_00_FF / 3) * 2);

                    writer.write("setblock ");
                    writer.write(String.valueOf(pixelI));
                    writer.write(" ");
                    writer.write(String.valueOf(height - lineI));
                    writer.write(" ");
                    writer.write("0 minecraft:chiseled_bookshelf[slot_0_occupied=");
                    writer.write(String.valueOf(red1));
                    writer.write(",slot_1_occupied=");
                    writer.write(String.valueOf(green1));
                    writer.write(",slot_2_occupied=");
                    writer.write(String.valueOf(blue1));
                    writer.write(",slot_3_occupied=");
                    writer.write(String.valueOf(red2));
                    writer.write(",slot_4_occupied=");
                    writer.write(String.valueOf(green2));
                    writer.write(",slot_5_occupied=");
                    writer.write(String.valueOf(blue2));
                    writer.write(",facing=south]\n");
                }
                pixelI = 0;
            // }
        }

        writer.close();
    }
}
