/*
Copyright (C) 2010

This file is part of the Particle filter applet
written by Max Bügler
http://www.maxbuegler.eu/

Particle filter applet is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2, or (at your option) any
later version.

Particle filter applet is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MapEditor extends JPanel {
    private boolean[][] map;
    private int brush=35;
    private BufferedImage buffer;
    private int mousex,mousey;

    public MapEditor(int width, int height){
        map=new boolean[width][height];
        for (int x=0;x<width;x++){
            for (int y=0;y<height;y++){
                map[x][y]=false;
            }
        }
        Listener l=new Listener();
        addMouseListener(l);
        addMouseMotionListener(l);
        addMouseWheelListener(l);
    }

    @Override
    public void paint(Graphics g){
        if (buffer==null||buffer.getWidth()!=getWidth()||buffer.getHeight()!=getHeight()){
            buffer=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2=(Graphics2D)buffer.getGraphics();
        g2.setColor(Color.GRAY);
        g2.fillRect(0,0,getWidth(),getHeight());
        for (int x=0;x<map.length;x++){
            for (int y=0;y<map[x].length;y++){
                if (map[x][y])g2.setColor(Color.WHITE);
                else g2.setColor(Color.BLACK);
                g2.fillRect(x,y,1,1);
            }
        }
        g2.setColor(Color.GRAY);
        g2.drawRect(mousex-brush/2,mousey-brush/2,brush,brush);
        g2.setColor(Color.GREEN);
        g2.drawString("Draw/erase way with left/right mouse",10,map[0].length+20);
        g2.drawString("Scale with mouse wheel",10,map[0].length+40);

        g.drawImage(buffer,0,0,null);
    }

    public boolean[][] getMap() {
        boolean[][] out=new boolean[map.length][map[0].length];
        boolean white=false;
        for (int x=0;x<map.length;x++){
            for (int y=0;y<map[x].length;y++){
                out[x][y]=map[x][y];
                if (out[x][y])white=true;
            }
        }
        if (!white){
            for (int x=0;x<map.length;x++){
                for (int y=0;y<map[x].length;y++){
                    out[x][y]=true;
                }
            }
        }
        return out;
    }

    public boolean[][] getMapPointer() {
        return map;
    }

    public void setMap(boolean[][] m){
        for (int x=0;x<map.length;x++){
            for (int y=0;y<map[x].length;y++){
                map[x][y]=m[x][y];
            }
        }
        repaint();
    }

    public void clearMap(){
        for (int x=0;x<map.length;x++){
            for (int y=0;y<map[x].length;y++){
                map[x][y]=false;
            }
        }
        repaint();
    }

    private void paintBrush(int xpos,int ypos, boolean erase){
        for (int x=0;x<brush;x++){
            for (int y=0;y<brush;y++){
                int mx=xpos+x-brush/2;
                int my=ypos+y-brush/2;
                if (mx>=0&&mx<map.length&&my>=0&&my<map[0].length){
                    if (!erase)
                        map[mx][my]=true;
                    else
                        map[mx][my]=false;
                }
            }
        }
        repaint();
    }
    class Listener implements MouseListener, MouseMotionListener, MouseWheelListener {
        int button;
        public void mouseClicked(MouseEvent e) {
            paintBrush(e.getX(),e.getY(),e.getButton()!=MouseEvent.BUTTON1);
        }

        public void mousePressed(MouseEvent e) {
            button=e.getButton();
        }

        public void mouseReleased(MouseEvent e) {}

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        public void mouseDragged(MouseEvent e) {
            mousex=e.getX();
            mousey=e.getY();
            paintBrush(e.getX(),e.getY(),button!=MouseEvent.BUTTON1);
        }

        public void mouseMoved(MouseEvent e) {
            mousex=e.getX();
            mousey=e.getY();
            repaint();
        }

        public void mouseWheelMoved(MouseWheelEvent e) {
            brush-=e.getWheelRotation();
            brush=Math.max(20,brush);
            repaint();
        }
    }

}


