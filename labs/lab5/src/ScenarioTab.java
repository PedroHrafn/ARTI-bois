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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class ScenarioTab extends JPanel {
    private int tx,ty,sx,sy;
    private boolean[][] map;
    private BufferedImage buffer;

    public ScenarioTab(MapEditor me){
        this.map=me.getMapPointer();
        this.tx=-1;
        this.ty=-1;
        this.sx=-1;
        this.sy=-1;
        addMouseListener(new Listener());
    }

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
        if (sx>-1&&sy>-1){
            g2.setColor(Color.GREEN);
            g2.drawLine(sx-10,sy,sx+10,sy);
            g2.drawLine(sx,sy-10,sx,sy+10);
            g2.fillOval(sx-2,sy-2,4,4);
        }
        if (tx>-1&&ty>-1){
            g2.setColor(Color.RED);
            g2.drawLine(tx-10,ty,tx+10,ty);
            g2.drawLine(tx,ty-10,tx,ty+10);
            g2.fillOval(tx-2,ty-2,4,4);
        }
        g2.setColor(Color.GREEN);
        g2.drawString("Set start point with left mouse",10,map[0].length+20);
        g2.drawString("Set finish point with right mouse",10,map[0].length+40);
        g.drawImage(buffer,0,0,null);
    }

    public int getTx() {
        return tx;
    }

    public int getTy() {
        return ty;
    }

    public int getSx() {
        return sx;
    }

    public int getSy() {
        return sy;
    }

    class Listener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            if (e.getX()<map.length&&e.getY()<map[0].length){
                if (e.getButton()==MouseEvent.BUTTON1){
                    sx=e.getX();
                    sy=e.getY();
                }
                else{
                    tx=e.getX();
                    ty=e.getY();
                }
                repaint();
            }
        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }
}
