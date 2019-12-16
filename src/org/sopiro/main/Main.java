package org.sopiro.main;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Main extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L;

    private static JFrame frame;

    private final double FPS = 1000000000.0 / 144.0;
    private final double UPS = 1000000000.0 / 300.0;

    public static final int WIDTH = 288;
    public static final int HEIGHT = 180;
    public static final int SCALE = 3;
    private static final String TITLE = "Skeleton";

    private boolean run = false;

    private BufferedImage image;
    private int[] pixels;

    private Input input = new Input();
    private GameLoop gameLoop;

    private Main()
    {
        Dimension d = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
        setMaximumSize(d);
        setMinimumSize(d);
        setPreferredSize(d);
        addKeyListener(input);
    }

    private void init()
    {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) (image.getRaster().getDataBuffer())).getData();

        gameLoop = new GameLoop(input);
    }

    private void start()
    {
        if (run)
            return;

        run = true;
        new Thread(this).start();
    }

    public void run()
    {
        long lastTime = System.nanoTime();
        long unprocessedUpdateTime = 0;
        long unprocessedRenderTime = 0;
        long frameCounter = System.currentTimeMillis();

        int frames = 0;
        int updates = 0;

        while (run)
        {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - lastTime;
            unprocessedUpdateTime += passedTime;
            unprocessedRenderTime += passedTime;
            lastTime = currentTime;

            if (unprocessedUpdateTime >= UPS)
            {
                unprocessedUpdateTime -= UPS;
                update();
                updates++;
            }

            if (unprocessedRenderTime >= FPS)
            {
                unprocessedRenderTime -= FPS;
                render();
                frames++;
            }
            if (System.currentTimeMillis() - frameCounter >= 1000)
            {
                System.out.println("frame : " + frames + ", update : " + updates);
                frames = 0;
                updates = 0;
                frameCounter += 1000;
            }
        }

        terminate();
    }

    private void update()
    {
        run = gameLoop.update();
    }

    public void render()
    {
        BufferStrategy bs = getBufferStrategy();

        if (bs == null)
        {
            createBufferStrategy(3);
            return;
        }

        Bitmap screen = new Bitmap(WIDTH, HEIGHT, pixels);

        gameLoop.render(screen);

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g.dispose();
        bs.show();
    }

    private void terminate()
    {
        frame.dispose();
    }

    public static void main(String[] args)
    {
        frame = new JFrame(TITLE);
        frame.setResizable(false);
        Main m = new Main();

        frame.add(m);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.requestFocus();

        m.init();
        m.start();
    }
}