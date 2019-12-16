package org.sopiro.main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bitmap
{
    private int width;
    private int height;

    private int[] pixels;

    public Bitmap(int width, int height)
    {
        this(width, height, new int[width * height]);
    }

    public Bitmap(int width, int height, int[] pixels)
    {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public Bitmap(String fileName)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File(fileName));
            width = image.getWidth();
            height = image.getHeight();

            pixels = image.getRGB(0, 0, width, height, null, 0, width);
        } catch (IOException e)
        {
            System.err.println("path : " + fileName);
            e.printStackTrace();
        }
    }

    public void render(Bitmap image, int xo, int yo)
    {
        render(image, xo, yo, 1.0f);
    }


    public void render(Bitmap image, int xo, int yo, float scale)
    {
        render(image, xo, yo, scale, scale);
    }

    public void render(Bitmap image, int xo, int yo, float scaleX, float scaleY)
    {
        for (int y = 0; y < image.height * scaleY; y++)
        {
            int yy = y + yo;
            if (yy < 0 || yy >= height)
                continue;
            for (int x = 0; x < image.width * scaleX; x++)
            {
                int xx = x + xo;
                if (xx < 0 || xx >= width)
                    continue;

                int sx = (int) (x / scaleX);
                int sy = (int) (y / scaleY);

                int color = image.pixels[sx + sy * image.width];
                if (color == 0xffff00ff)
                    continue;

                pixels[xx + yy * width] = color;
            }
        }
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void clear(int color)
    {
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = color;
    }
}

