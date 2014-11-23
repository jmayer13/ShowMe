/*- 
 * Classname:             ShowMe.java 
 * 
 * Version information:   1.0
 * 
 * Date:                  22/03/2014 - 11:26:51 
 * 
 * author:                Jonas Mayer (jmayer13@hotmail.com) 
 * Copyright notice:      (informações do método, pra que serve, idéia principal) 
 */
package showme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

/**
 * Classe principal
 *
 * @see
 * @author Jonas Mayer (jmayer13@hotmail.com)
 */
public class ShowMe {

    //janela principal 
    private JFrame frame;
    //graficos da janela
    private Graphics2D graphics;
    //thread que desenha
    private Thread drawerThread;
    //coordenadas do mouse
    private int mouseX = 0;
    private int mouseY = 0;
    //pontos folha
    private List<Point> sheetPoints;
    //tamanho das linhas
    private int rate = 10;
    //tamanho da tela
    private int screenX = 0;
    private int screenY = 0;
    //lista de cores
    private List<Color> colorlist;

    /**
     * Contrutor sem parâmetros
     */
    public ShowMe() {
        //inicializa variaveis
        sheetPoints = new ArrayList();
        makeColorList();
        frame = new JFrame();

        //define cor de fundo
        frame.getContentPane().setBackground(Color.WHITE);

        //obtêm tamanho da tela
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        screenX = (int) dim.getWidth();//largura
        screenY = (int) dim.getHeight();//altura

        //define tamanho
        frame.setBounds(0, 0, screenX, screenY);

        //configurações de teça
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);

        //define eventos de saida
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.addKeyListener(new KeyListener() {
            //ESC = sair
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.dispose();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.dispose();
                }
            }
        });

        //define evento de alteração de rate
        frame.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                rate = rate + e.getWheelRotation();
                if (rate < 10) {
                    rate = 10;
                }
                if (rate > screenX / 2) {
                    rate = screenX / 2;
                }
            }
        });

        //inicia desenho
        drawMagic();

        //configurações finais
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }//fim do método construtor

    /**
     * Desenha linhas
     */
    private void drawMagic() {

        //cria thread
        drawerThread = new Thread() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    //pega gráficos
                    graphics = (Graphics2D) frame.getGraphics();

                    //pega posição do mouse
                    Point mousePoint = MouseInfo.getPointerInfo().getLocation();

                    //verifica se o mouse foi movido e atualiza raiz
                    boolean isDead = false;
                    if (mouseX != mousePoint.x) {
                        mouseX = mousePoint.x;
                        isDead = true;
                    }
                    if (mouseY != mousePoint.y) {
                        mouseY = mousePoint.y;
                        isDead = true;
                    }

                    //reinicia arvore
                    if (isDead) {
                        sheetPoints.clear();
                        sheetPoints.add(mousePoint);
                    }

                    //escolhe ponto randomicamente
                    Point chooseOne = sheetPoints.get(new Random().nextInt(sheetPoints.size()));
                    //verifica se não é a raiz e remove
                    if ((chooseOne.x != mouseX && chooseOne.y != mouseY) || sheetPoints.size() > rate) {
                        sheetPoints.remove(chooseOne);
                    }

                    //cria ponto randomico
                    Point randomPoint = makePoint(chooseOne);

                    //adiciona ponto como folha
                    sheetPoints.add(randomPoint);

                    //define cor randomicamente
                    graphics.setColor(colorlist.get(new Random().nextInt(colorlist.size())));

                    //desenha linha
                    graphics.drawLine(chooseOne.x, chooseOne.y, randomPoint.x, randomPoint.y);

                    //dorme 
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        Thread.interrupted();
                    }
                }
            }//fim do método run
        };

        //inicia thread
        drawerThread.start();
    }//fim do método drawLine

    /**
     * Faz ponto randomicamnete baseado no rate e nó mãe
     *
     * @param point nó mãe
     * @return <code>Point</code> ponto randômico
     */
    private Point makePoint(Point point) {
        int x = 0;
        int y = 0;
        while (!(x > 0 && x < screenX) || !(y > 0 && y < screenY)) {
            x = new Random().nextInt(2 * rate) + point.x - rate;
            y = new Random().nextInt(2 * rate) + point.y - rate;
        }
        return new Point(x, y);

    }//fim do método makePoint

    /**
     * Cria lista com cores
     */
    private void makeColorList() {
        colorlist = new ArrayList();
        colorlist.add(new Color(244, 244, 244));
        colorlist.add(new Color(135, 206, 235));
        colorlist.add(new Color(139, 137, 112));
        colorlist.add(new Color(191, 62, 255));
        colorlist.add(new Color(139, 0, 0));
        colorlist.add(new Color(238, 232, 205));
        colorlist.add(new Color(255, 255, 255));
        colorlist.add(new Color(205, 133, 63));
        colorlist.add(new Color(205, 104, 137));
        colorlist.add(new Color(139, 134, 78));
        colorlist.add(new Color(255, 192, 203));
        colorlist.add(new Color(139, 136, 120));
        colorlist.add(new Color(155, 205, 155));
        colorlist.add(new Color(139, 123, 139));
        colorlist.add(new Color(205, 96, 144));
        colorlist.add(new Color(139, 87, 66));
        colorlist.add(new Color(24, 116, 205));
        colorlist.add(new Color(255, 125, 64));
        colorlist.add(new Color(205, 175, 149));
        colorlist.add(new Color(238, 92, 66));
        colorlist.add(new Color(0, 255, 255));
        colorlist.add(new Color(238, 18, 137));
        colorlist.add(new Color(180, 238, 180));
        colorlist.add(new Color(139, 117, 0));
        colorlist.add(new Color(222, 184, 135));
        colorlist.add(new Color(160, 82, 45));
        colorlist.add(new Color(105, 139, 105));
        colorlist.add(new Color(205, 190, 112));
        colorlist.add(new Color(205, 201, 201));
        colorlist.add(new Color(139, 58, 58));
        colorlist.add(new Color(72, 209, 204));
        colorlist.add(new Color(198, 113, 113));
        colorlist.add(new Color(72, 61, 139));
        colorlist.add(new Color(128, 0, 0));
        colorlist.add(new Color(159, 121, 238));
        colorlist.add(new Color(141, 182, 205));
        colorlist.add(new Color(238, 0, 238));
        colorlist.add(new Color(189, 183, 107));
        colorlist.add(new Color(147, 112, 219));
        colorlist.add(new Color(139, 62, 47));
        colorlist.add(new Color(92, 172, 238));
        colorlist.add(new Color(176, 226, 255));
        colorlist.add(new Color(102, 139, 139));
        colorlist.add(new Color(0, 238, 238));
        colorlist.add(new Color(105, 105, 105));
        colorlist.add(new Color(124, 205, 124));
        colorlist.add(new Color(165, 42, 42));
        colorlist.add(new Color(75, 0, 130));
        colorlist.add(new Color(131, 111, 255));
        colorlist.add(new Color(127, 255, 0));
        colorlist.add(new Color(139, 26, 26));
        colorlist.add(new Color(205, 41, 144));
        colorlist.add(new Color(238, 180, 180));
        colorlist.add(new Color(70, 130, 180));
        colorlist.add(new Color(238, 58, 140));
        colorlist.add(new Color(100, 149, 237));
        colorlist.add(new Color(139, 34, 82));
        colorlist.add(new Color(139, 139, 131));
        colorlist.add(new Color(56, 142, 142));
        colorlist.add(new Color(250, 250, 210));
        colorlist.add(new Color(119, 136, 153));
        colorlist.add(new Color(255, 193, 193));
        colorlist.add(new Color(255, 64, 64));
        colorlist.add(new Color(238, 0, 0));
        colorlist.add(new Color(122, 139, 139));
        colorlist.add(new Color(0, 128, 0));
        colorlist.add(new Color(238, 174, 238));
        colorlist.add(new Color(110, 139, 61));
        colorlist.add(new Color(238, 238, 0));
        colorlist.add(new Color(193, 255, 193));
        colorlist.add(new Color(255, 239, 213));
        colorlist.add(new Color(0, 197, 205));
        colorlist.add(new Color(255, 131, 250));
        colorlist.add(new Color(122, 197, 205));
        colorlist.add(new Color(139, 101, 8));
        colorlist.add(new Color(139, 54, 38));
        colorlist.add(new Color(192, 192, 192));
        colorlist.add(new Color(69, 139, 0));
        colorlist.add(new Color(188, 143, 143));
        colorlist.add(new Color(139, 125, 107));
        colorlist.add(new Color(202, 225, 255));
        colorlist.add(new Color(154, 192, 205));
        colorlist.add(new Color(255, 69, 0));
        colorlist.add(new Color(108, 123, 139));
        colorlist.add(new Color(46, 139, 87));
        colorlist.add(new Color(169, 169, 169));
        colorlist.add(new Color(82, 139, 139));
        colorlist.add(new Color(238, 173, 14));
        colorlist.add(new Color(255, 0, 255));
        colorlist.add(new Color(255, 130, 71));
        colorlist.add(new Color(142, 142, 142));
        colorlist.add(new Color(139, 119, 101));
        colorlist.add(new Color(255, 228, 196));
        colorlist.add(new Color(152, 245, 255));
        colorlist.add(new Color(205, 133, 0));
        colorlist.add(new Color(192, 255, 62));
        colorlist.add(new Color(16, 78, 139));
        colorlist.add(new Color(238, 213, 210));
        colorlist.add(new Color(65, 105, 225));
        colorlist.add(new Color(171, 130, 255));
        colorlist.add(new Color(112, 128, 144));
        colorlist.add(new Color(198, 226, 255));
        colorlist.add(new Color(113, 198, 113));
        colorlist.add(new Color(28, 134, 238));
        colorlist.add(new Color(139, 90, 43));
        colorlist.add(new Color(255, 165, 0));
        colorlist.add(new Color(126, 192, 238));
        colorlist.add(new Color(255, 222, 173));
        colorlist.add(new Color(255, 97, 3));
        colorlist.add(new Color(205, 55, 0));
        colorlist.add(new Color(0, 104, 139));
        colorlist.add(new Color(34, 139, 34));
        colorlist.add(new Color(178, 58, 238));
        colorlist.add(new Color(121, 205, 205));
        colorlist.add(new Color(84, 255, 159));
        colorlist.add(new Color(255, 245, 238));
        colorlist.add(new Color(238, 201, 0));
        colorlist.add(new Color(255, 110, 180));
        colorlist.add(new Color(205, 112, 84));
        colorlist.add(new Color(238, 203, 173));
        colorlist.add(new Color(84, 139, 84));
        colorlist.add(new Color(122, 103, 238));
        colorlist.add(new Color(255, 228, 181));
        colorlist.add(new Color(78, 238, 148));
        colorlist.add(new Color(238, 99, 99));
        colorlist.add(new Color(240, 128, 128));
        colorlist.add(new Color(205, 183, 181));
        colorlist.add(new Color(135, 206, 250));
        colorlist.add(new Color(238, 64, 0));
        colorlist.add(new Color(0, 206, 209));
        colorlist.add(new Color(238, 232, 170));
        colorlist.add(new Color(238, 118, 0));
        colorlist.add(new Color(141, 238, 238));
        colorlist.add(new Color(138, 43, 226));
        colorlist.add(new Color(25, 25, 112));
        colorlist.add(new Color(93, 71, 139));
        colorlist.add(new Color(106, 90, 205));
        colorlist.add(new Color(132, 112, 255));
        colorlist.add(new Color(139, 76, 57));
        colorlist.add(new Color(255, 127, 0));
        colorlist.add(new Color(81, 81, 81));
        colorlist.add(new Color(205, 50, 120));
        colorlist.add(new Color(255, 48, 48));
        colorlist.add(new Color(69, 139, 116));
        colorlist.add(new Color(255, 0, 0));
        colorlist.add(new Color(145, 44, 238));
        colorlist.add(new Color(125, 158, 192));
        colorlist.add(new Color(178, 34, 34));
        colorlist.add(new Color(139, 58, 98));
        colorlist.add(new Color(139, 37, 0));
        colorlist.add(new Color(0, 0, 238));
        colorlist.add(new Color(104, 131, 139));
        colorlist.add(new Color(210, 105, 30));
        colorlist.add(new Color(238, 210, 238));
        colorlist.add(new Color(238, 169, 184));
        colorlist.add(new Color(205, 173, 0));
        colorlist.add(new Color(152, 251, 152));
        colorlist.add(new Color(139, 105, 105));
        colorlist.add(new Color(238, 48, 167));
        colorlist.add(new Color(131, 139, 131));
        colorlist.add(new Color(95, 158, 160));
        colorlist.add(new Color(0, 128, 128));
        colorlist.add(new Color(131, 139, 139));
        colorlist.add(new Color(205, 91, 69));
        colorlist.add(new Color(238, 162, 173));
        colorlist.add(new Color(255, 255, 240));
        colorlist.add(new Color(0, 0, 0));
        colorlist.add(new Color(0, 238, 0));
        colorlist.add(new Color(61, 89, 171));
        colorlist.add(new Color(218, 165, 32));
        colorlist.add(new Color(199, 21, 133));
        colorlist.add(new Color(128, 138, 135));
        colorlist.add(new Color(255, 174, 185));
        colorlist.add(new Color(132, 132, 132));
        colorlist.add(new Color(255, 236, 139));
        colorlist.add(new Color(107, 142, 35));
        colorlist.add(new Color(0, 0, 205));
        colorlist.add(new Color(151, 255, 255));
        colorlist.add(new Color(205, 16, 118));
        colorlist.add(new Color(255, 250, 240));
        colorlist.add(new Color(30, 30, 30));
        colorlist.add(new Color(245, 222, 179));
        colorlist.add(new Color(240, 255, 240));
        colorlist.add(new Color(139, 121, 94));
        colorlist.add(new Color(238, 154, 73));
        colorlist.add(new Color(205, 145, 158));
        colorlist.add(new Color(220, 20, 60));
        colorlist.add(new Color(216, 191, 216));
        colorlist.add(new Color(50, 205, 50));
        colorlist.add(new Color(209, 95, 238));
        colorlist.add(new Color(205, 149, 12));
        colorlist.add(new Color(154, 205, 50));
        colorlist.add(new Color(255, 248, 220));
        colorlist.add(new Color(0, 205, 205));
        colorlist.add(new Color(154, 50, 205));
        colorlist.add(new Color(255, 128, 0));
        colorlist.add(new Color(255, 215, 0));
        colorlist.add(new Color(139, 95, 101));
        colorlist.add(new Color(48, 128, 20));
        colorlist.add(new Color(205, 140, 149));
        colorlist.add(new Color(255, 181, 197));
        colorlist.add(new Color(179, 238, 58));
        colorlist.add(new Color(40, 40, 40));
        colorlist.add(new Color(189, 252, 201));
        colorlist.add(new Color(230, 230, 250));
        colorlist.add(new Color(142, 142, 56));
        colorlist.add(new Color(205, 181, 205));
        colorlist.add(new Color(110, 123, 139));
        colorlist.add(new Color(128, 128, 128));
        colorlist.add(new Color(139, 71, 137));
        colorlist.add(new Color(170, 170, 170));
        colorlist.add(new Color(238, 233, 191));
        colorlist.add(new Color(176, 196, 222));
        colorlist.add(new Color(255, 140, 105));
        colorlist.add(new Color(83, 134, 139));
        colorlist.add(new Color(238, 106, 167));
        colorlist.add(new Color(255, 231, 186));
        colorlist.add(new Color(185, 211, 238));
        colorlist.add(new Color(237, 145, 33));
        colorlist.add(new Color(238, 122, 233));
        colorlist.add(new Color(3, 168, 158));
        colorlist.add(new Color(205, 85, 85));
        colorlist.add(new Color(238, 106, 80));
        colorlist.add(new Color(184, 134, 11));
        colorlist.add(new Color(255, 127, 36));
        colorlist.add(new Color(255, 185, 15));
        colorlist.add(new Color(193, 205, 193));
        colorlist.add(new Color(79, 148, 205));
        colorlist.add(new Color(238, 149, 114));
        colorlist.add(new Color(205, 102, 0));
        colorlist.add(new Color(255, 218, 185));
        colorlist.add(new Color(240, 230, 140));
        colorlist.add(new Color(248, 248, 255));
        colorlist.add(new Color(193, 193, 193));
        colorlist.add(new Color(227, 207, 87));
        colorlist.add(new Color(205, 104, 57));
        colorlist.add(new Color(139, 129, 76));
        colorlist.add(new Color(135, 38, 87));
        colorlist.add(new Color(176, 23, 31));
        colorlist.add(new Color(138, 54, 15));
        colorlist.add(new Color(205, 170, 125));
        colorlist.add(new Color(104, 34, 139));
        colorlist.add(new Color(238, 118, 33));
        colorlist.add(new Color(238, 121, 66));
        colorlist.add(new Color(96, 123, 139));
        colorlist.add(new Color(220, 220, 220));
        colorlist.add(new Color(255, 182, 193));
        colorlist.add(new Color(238, 130, 238));
        colorlist.add(new Color(255, 160, 122));
        colorlist.add(new Color(255, 127, 80));
        colorlist.add(new Color(0, 139, 0));
        colorlist.add(new Color(139, 115, 85));
        colorlist.add(new Color(255, 228, 225));
        colorlist.add(new Color(255, 235, 205));
        colorlist.add(new Color(238, 229, 222));
        colorlist.add(new Color(137, 104, 205));
        colorlist.add(new Color(255, 105, 180));
        colorlist.add(new Color(105, 89, 205));
        colorlist.add(new Color(0, 154, 205));
        colorlist.add(new Color(154, 255, 154));
        colorlist.add(new Color(108, 166, 205));
        colorlist.add(new Color(255, 240, 245));
        colorlist.add(new Color(255, 250, 250));
        colorlist.add(new Color(144, 238, 144));
        colorlist.add(new Color(0, 139, 69));
        colorlist.add(new Color(139, 131, 120));
        colorlist.add(new Color(238, 216, 174));
        colorlist.add(new Color(255, 106, 106));
        colorlist.add(new Color(143, 188, 143));
        colorlist.add(new Color(255, 225, 255));
        colorlist.add(new Color(174, 238, 238));
        colorlist.add(new Color(139, 90, 0));
        colorlist.add(new Color(238, 213, 183));
        colorlist.add(new Color(176, 224, 230));
        colorlist.add(new Color(150, 205, 205));
        colorlist.add(new Color(139, 35, 35));
        colorlist.add(new Color(61, 145, 64));
        colorlist.add(new Color(188, 238, 104));
        colorlist.add(new Color(128, 128, 0));
        colorlist.add(new Color(188, 210, 238));
        colorlist.add(new Color(205, 198, 115));
        colorlist.add(new Color(0, 250, 154));
        colorlist.add(new Color(205, 0, 205));
        colorlist.add(new Color(71, 60, 139));
        colorlist.add(new Color(227, 168, 105));
        colorlist.add(new Color(233, 150, 122));
        colorlist.add(new Color(122, 55, 139));
        colorlist.add(new Color(58, 95, 205));
        colorlist.add(new Color(238, 233, 233));
        colorlist.add(new Color(155, 48, 255));
        colorlist.add(new Color(255, 165, 79));
        colorlist.add(new Color(197, 193, 170));
        colorlist.add(new Color(255, 193, 37));
        colorlist.add(new Color(159, 182, 205));
        colorlist.add(new Color(255, 153, 18));
        colorlist.add(new Color(205, 102, 29));
        colorlist.add(new Color(139, 137, 137));
        colorlist.add(new Color(41, 36, 33));
        colorlist.add(new Color(205, 205, 180));
        colorlist.add(new Color(139, 99, 108));
        colorlist.add(new Color(0, 205, 0));
        colorlist.add(new Color(162, 181, 205));
        colorlist.add(new Color(255, 99, 71));
        colorlist.add(new Color(0, 0, 139));
        colorlist.add(new Color(139, 139, 122));
        colorlist.add(new Color(85, 85, 85));
        colorlist.add(new Color(255, 20, 147));
        colorlist.add(new Color(0, 199, 140));
        colorlist.add(new Color(102, 205, 0));
        colorlist.add(new Color(238, 207, 161));
        colorlist.add(new Color(139, 69, 19));
        colorlist.add(new Color(205, 186, 150));
        colorlist.add(new Color(139, 71, 38));
        colorlist.add(new Color(238, 197, 145));
        colorlist.add(new Color(153, 50, 204));
        colorlist.add(new Color(0, 178, 238));
        colorlist.add(new Color(205, 183, 158));
        colorlist.add(new Color(238, 59, 59));
        colorlist.add(new Color(240, 255, 255));
        colorlist.add(new Color(186, 85, 211));
        colorlist.add(new Color(135, 206, 255));
        colorlist.add(new Color(0, 205, 102));
        colorlist.add(new Color(47, 79, 79));
        colorlist.add(new Color(255, 211, 155));
        colorlist.add(new Color(205, 205, 193));
        colorlist.add(new Color(0, 191, 255));
        colorlist.add(new Color(72, 118, 255));
        colorlist.add(new Color(180, 82, 205));
        colorlist.add(new Color(139, 105, 20));
        colorlist.add(new Color(0, 238, 118));
        colorlist.add(new Color(255, 52, 179));
        colorlist.add(new Color(205, 0, 0));
        colorlist.add(new Color(0, 201, 87));
        colorlist.add(new Color(0, 0, 128));
        colorlist.add(new Color(102, 205, 170));
        colorlist.add(new Color(252, 230, 201));
        colorlist.add(new Color(139, 139, 0));
        colorlist.add(new Color(199, 97, 20));
        colorlist.add(new Color(205, 51, 51));
        colorlist.add(new Color(253, 245, 230));
        colorlist.add(new Color(238, 223, 204));
        colorlist.add(new Color(238, 130, 98));
        colorlist.add(new Color(208, 32, 144));
        colorlist.add(new Color(39, 64, 139));
        colorlist.add(new Color(193, 205, 205));
        colorlist.add(new Color(224, 238, 238));
        colorlist.add(new Color(128, 0, 128));
        colorlist.add(new Color(255, 246, 143));
        colorlist.add(new Color(205, 105, 201));
        colorlist.add(new Color(124, 252, 0));
        colorlist.add(new Color(210, 180, 140));
        colorlist.add(new Color(127, 255, 212));
        colorlist.add(new Color(255, 130, 171));
        colorlist.add(new Color(238, 224, 229));
        colorlist.add(new Color(205, 192, 176));
        colorlist.add(new Color(209, 238, 238));
        colorlist.add(new Color(238, 220, 130));
        colorlist.add(new Color(250, 240, 230));
        colorlist.add(new Color(139, 69, 0));
        colorlist.add(new Color(205, 197, 191));
        colorlist.add(new Color(224, 238, 224));
        colorlist.add(new Color(142, 229, 238));
        colorlist.add(new Color(148, 0, 211));
        colorlist.add(new Color(250, 128, 114));
        colorlist.add(new Color(205, 92, 92));
        colorlist.add(new Color(205, 129, 98));
        colorlist.add(new Color(255, 62, 150));
        colorlist.add(new Color(238, 238, 209));
        colorlist.add(new Color(255, 239, 219));
        colorlist.add(new Color(139, 28, 98));
        colorlist.add(new Color(205, 179, 139));
        colorlist.add(new Color(113, 113, 198));
        colorlist.add(new Color(238, 44, 44));
        colorlist.add(new Color(224, 255, 255));
        colorlist.add(new Color(240, 248, 255));
        colorlist.add(new Color(128, 128, 105));
        colorlist.add(new Color(255, 255, 0));
        colorlist.add(new Color(234, 234, 234));
        colorlist.add(new Color(99, 184, 255));
        colorlist.add(new Color(0, 255, 0));
        colorlist.add(new Color(183, 183, 183));
        colorlist.add(new Color(238, 230, 133));
        colorlist.add(new Color(118, 238, 0));
        colorlist.add(new Color(205, 193, 197));
        colorlist.add(new Color(30, 144, 255));
        colorlist.add(new Color(91, 91, 91));
        colorlist.add(new Color(0, 245, 255));
        colorlist.add(new Color(85, 107, 47));
        colorlist.add(new Color(255, 255, 224));
        colorlist.add(new Color(0, 255, 127));
        colorlist.add(new Color(0, 0, 255));
        colorlist.add(new Color(51, 161, 201));
        colorlist.add(new Color(245, 245, 245));
        colorlist.add(new Color(187, 255, 255));
        colorlist.add(new Color(255, 250, 205));
        colorlist.add(new Color(250, 235, 215));
        colorlist.add(new Color(67, 110, 238));
        colorlist.add(new Color(205, 200, 177));
        colorlist.add(new Color(118, 238, 198));
        colorlist.add(new Color(125, 38, 205));
        colorlist.add(new Color(123, 104, 238));
        colorlist.add(new Color(238, 121, 159));
        colorlist.add(new Color(238, 180, 34));
        colorlist.add(new Color(85, 26, 139));
        colorlist.add(new Color(221, 160, 221));
        colorlist.add(new Color(67, 205, 128));
        colorlist.add(new Color(139, 10, 80));
        colorlist.add(new Color(245, 245, 220));
        colorlist.add(new Color(139, 0, 139));
        colorlist.add(new Color(224, 102, 255));
        colorlist.add(new Color(74, 112, 139));
        colorlist.add(new Color(244, 164, 96));
        colorlist.add(new Color(32, 178, 170));
        colorlist.add(new Color(54, 100, 139));
        colorlist.add(new Color(139, 134, 130));
        colorlist.add(new Color(156, 102, 31));
        colorlist.add(new Color(0, 139, 139));
        colorlist.add(new Color(0, 229, 238));
        colorlist.add(new Color(180, 205, 205));
        colorlist.add(new Color(245, 255, 250));
        colorlist.add(new Color(94, 38, 18));
        colorlist.add(new Color(139, 102, 139));
        colorlist.add(new Color(205, 201, 165));
        colorlist.add(new Color(162, 205, 90));
        colorlist.add(new Color(191, 239, 255));
        colorlist.add(new Color(139, 71, 93));
        colorlist.add(new Color(60, 179, 113));
        colorlist.add(new Color(139, 126, 102));
        colorlist.add(new Color(238, 238, 224));
        colorlist.add(new Color(139, 131, 134));
        colorlist.add(new Color(0, 134, 139));
        colorlist.add(new Color(173, 216, 230));
        colorlist.add(new Color(255, 140, 0));
        colorlist.add(new Color(238, 154, 0));
        colorlist.add(new Color(178, 223, 238));
        colorlist.add(new Color(142, 56, 142));
        colorlist.add(new Color(205, 205, 0));
        colorlist.add(new Color(0, 100, 0));
        colorlist.add(new Color(205, 79, 57));
        colorlist.add(new Color(105, 139, 34));
        colorlist.add(new Color(255, 114, 86));
        colorlist.add(new Color(205, 155, 155));
        colorlist.add(new Color(219, 112, 147));
        colorlist.add(new Color(202, 255, 112));
        colorlist.add(new Color(64, 224, 208));
        colorlist.add(new Color(138, 51, 36));
        colorlist.add(new Color(205, 38, 38));
        colorlist.add(new Color(218, 112, 214));
        colorlist.add(new Color(139, 125, 123));
        colorlist.add(new Color(255, 187, 255));
        colorlist.add(new Color(173, 255, 47));
        colorlist.add(new Color(211, 211, 211));
        colorlist.add(new Color(205, 150, 205));
        colorlist.add(new Color(164, 211, 238));
        colorlist.add(new Color(205, 155, 29));

    }//fim do método createColorList

    //método principal
    public static void main(String[] args) {
        new ShowMe();
    }//fim do método main  
}//fim da classe ShowMe  

