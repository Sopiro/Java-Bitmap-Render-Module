package org.sopiro.main;

import java.util.Random;

public class GameLoop
{
    private Random r = new Random();

    private int time;

    public GameLoop()
    {
    }

    public boolean update()
    {
        time++;

        return true;
    }

    public void render(Bitmap screen)
    {
//        screen.clear(0xffffff);

        int r = 50;

        double xo = Math.cos(time * 0.01) * r + (Main.WIDTH - 2 * r) / 2.0;
        double yo = Math.sin(time * 0.01) * r + (Main.HEIGHT - 2 * r) / 2.0;

        screen.render(Arts.star, (int) xo, (int) yo);
    }
}

