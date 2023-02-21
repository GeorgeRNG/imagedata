package io.github.georgerng;

import java.util.List;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static io.github.georgerng.Main.padLeft;

public class TextDisplay {
    public static void render(File framesFolder, File outputFile) throws IOException {
        if(!outputFile.createNewFile()) System.out.println("There was already an output file, so it has been overwritten.");
        FileWriter outputWriter = new FileWriter(outputFile);
        outputWriter.write("""
                ##
                 # main.mcfunction
                 # ImageData
                 #
                 # Created by GeorgeRNG.
                 #
                 # This file is autogenerated.
                ##
                
                """);
                
        int namePadding = 3;
        int frames = framesFolder.list().length;
        frames = 100;
        int logRate = frames / 100;

        for (int i = frames - 1; i >= 0; i-= 1) {
            try {
                outputWriter.write(textDisplayFrame(new File("./frames/frame" + padLeft(String.valueOf(i + 1), namePadding) + ".bmp"),i,2));
                if(i % logRate == 0) System.out.println(100 * (frames - i) / frames + "% Frame:" + (frames - i));
            }
            catch (Exception e) {
                System.out.println("Frame" + i + ": " + e.getMessage());
            }
        }

        outputWriter.close();
    }

    private static List<String> grayscale = List.of("0","8","7","F");
    private static String textDisplayFrame(File file, int index, int rescale) throws IOException {
        BufferedImage original = ImageIO.read(file);
        BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();
        g.drawImage(original, 0, 0, original.getWidth(), original.getHeight(), null);
        g.dispose();

        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        StringBuilder command = new StringBuilder("data merge @e[limit=1type=minecraft:text_display,tag=video,tag=");
        command.append(index);
        command.append("] run data merge entity @s {Tags:[\"video\",\"");
        command.append(index + 1);
        command.append("\"],text:\"\\\"");

        String lastColor = null;
        int lineI = 0;
        int pixelI = 0;
        int width = image.getWidth(); // chunk size to divide
        for(int i = 0; i < data.length; i += width){
            lineI++;
            if(lineI % rescale == 0) {
                int[] line = Arrays.copyOfRange(data, i, i+width);
                for (int pixel: line) {
                    pixelI++;
                    if(pixelI % rescale == 0) {
                        String color = getCode(pixel);
                        if(!Objects.equals(lastColor, color)) {
                            command.append("§");
                            command.append(color);
                            command.append("§l");//§r
                            lastColor = color;
                        }
                        command.append("█");
                    }
                }
                command.append("\\\\n");
                lastColor = null;
            }
        }
        command.replace(command.length() - 3,command.length(),"");
        command.append("\\\"\"}\n");
        return command.toString();
    }

    private static String getCode(int color) {
        return grayscale.get((int) (((color & 0xAA) * 1.25490196078431) / 64));
        // boolean i = color > (0xAA_AA_AA / 2);
        // int min = i ? 0xAA : 0x55;
        // int r = ((0xFF0000 & color) >> 16) > min ? 1 : 0;
        // int g = ((0x00FF00 & color) >>  8) > min ? 1 : 0;
        // int b = ((0x0000FF & color)      ) > min ? 1 : 0;
        // System.out.println(r);
        // return Integer.toHexString(((i ? 1 : 0) << 3) + (r << 2) + (g << 1) + (b));
    }
}
