package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class YLands {
    public void render(File outputFile, File framesFolder, int rescale) throws IOException {
        var outputWriter = new FileWriter(outputFile);

        var builder = new StringBuilder("Dummy,Data\nData,");

        Files.createDirectories(framesFolder.toPath());
        var list = framesFolder.list();
        if (list == null) return;
        for (var frame : Arrays.stream(list)
                .filter(filename -> filename.matches("frame\\d{4}\\.bmp"))
                .sorted().toList()
        ) {
            BufferedImage original = ImageIO.read(framesFolder.toPath().resolve(frame).toFile());
            BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

            Graphics2D g = image.createGraphics();
            g.drawImage(original, 0, 0, original.getWidth(), original.getHeight(), null);
            g.dispose();

            int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

            int lineI = 0;
            int pixelI = 0;
            int width = image.getWidth(); // chunk size to divide
            for (int i = 0; i < data.length; i += width) {
                lineI++;
                if (lineI % rescale != 0) continue;
                int[] line = Arrays.copyOfRange(data, i, i + width);
                for (int pixel : line) {
                    pixelI++;
                    if (pixelI % rescale != 0) continue;

//                    int r = ((0xFF0000 & pixel) >> 16);
//                    int g = ((0x00FF00 & pixel) >>  8);
                    int b = ((0x0000FF & pixel));

                    if(b >= 0x7F) builder.append("1");
                    else builder.append("0");
                }
                builder.append("2");
            }
            builder.append("3");
        }

        outputWriter.write(builder.toString());
        outputWriter.close();
    }
}
