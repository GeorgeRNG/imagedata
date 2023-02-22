package io.github.georgerng;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Bookshelf {
    public static void renderFrames(File folder, String output, int rescale) {
        int namePadding = 3;
        int frames = folder.list().length;
        frames = 100;
        int logRate = frames / 100;

        for (int i = 0; i < frames; i++) {
            try {
                File inputFile = new File("./frames/frame" + Main.padLeft(String.valueOf(i + 1), namePadding) + ".bmp") ;
                Path outputFile = Path.of(output + "/frame" + i + ".mcfunction");
                Double time = renderFrame(inputFile,outputFile,rescale);
                if(i % logRate == 0) System.out.println(100 * i / frames + "% Frame:" + i + " which took " + time + " ms");
            }
            catch (Exception e) {
                System.out.println("Frame" + i + ": " + e.getMessage());
            }
        }
    }

    public static double renderFrame(File file, Path output, int rescale) throws IOException {
        long time = System.nanoTime();

        Files.deleteIfExists(output);
        Files.createFile(output);
        BufferedWriter writer = Files.newBufferedWriter(output, StandardOpenOption.WRITE);
        
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

                    StringBuffer outputBuilder = new StringBuffer();
                    outputBuilder.append("setblock ");
                    outputBuilder.append(String.valueOf(pixelI));
                    outputBuilder.append(" ");
                    outputBuilder.append(String.valueOf(height - lineI));
                    outputBuilder.append(" ");
                    outputBuilder.append("0 minecraft:chiseled_bookshelf[slot_0_occupied=");
                    outputBuilder.append(String.valueOf(red1));
                    outputBuilder.append(",slot_1_occupied=");
                    outputBuilder.append(String.valueOf(green2));
                    outputBuilder.append(",slot_2_occupied=");
                    outputBuilder.append(String.valueOf(blue1));
                    outputBuilder.append(",slot_3_occupied=");
                    outputBuilder.append(String.valueOf(red2));
                    outputBuilder.append(",slot_4_occupied=");
                    outputBuilder.append(String.valueOf(green1));
                    outputBuilder.append(",slot_5_occupied=");
                    outputBuilder.append(String.valueOf(blue2));
                    outputBuilder.append(",facing=south]\n");

                    writer.write(outputBuilder.toString());

                    // writer.write("setblock ");
                    // writer.write(String.valueOf(pixelI));
                    // writer.write(" ");
                    // writer.write(String.valueOf(height - lineI));
                    // writer.write(" ");
                    // writer.write("0 minecraft:chiseled_bookshelf[slot_0_occupied=");
                    // writer.write(String.valueOf(red1));
                    // writer.write(",slot_1_occupied=");
                    // writer.write(String.valueOf(green2));
                    // writer.write(",slot_2_occupied=");
                    // writer.write(String.valueOf(blue1));
                    // writer.write(",slot_3_occupied=");
                    // writer.write(String.valueOf(red2));
                    // writer.write(",slot_4_occupied=");
                    // writer.write(String.valueOf(green1));
                    // writer.write(",slot_5_occupied=");
                    // writer.write(String.valueOf(blue2));
                    // writer.write(",facing=south]\n");
                    

                    // writer.write("setblock " + pixelI + " " + (height - lineI) + " " + "0 minecraft:chiseled_bookshelf[slot_0_occupied=" + red1 + ",slot_1_occupied=" + green2 + ",slot_2_occupied=" + blue1 + ",slot_3_occupied=" + red2 + ",slot_4_occupied=" + green1 + ",slot_5_occupied=" + blue2 + ",facing=south]\n");
                    // writer.write(String.format("setblock %d %d 0 minecraft:chiseled_bookshelf[slot_0_occupied=%s,slot_1_occupied=%s,slot_2_occupied=%s,slot_3_occupied=%s,]",pixel,height-lineI,red1,green2,blue1,red2,green1,blue2));
                    
                }
                pixelI = 0;
            // }
        }

        // outputBuilder.toString();
        // Files.write(output, outputBuilder.toString().getBytes() ,StandardOpenOption.WRITE);
        writer.close();

        return ((double) (System.nanoTime() - time)) / 1000000;
    }
}
