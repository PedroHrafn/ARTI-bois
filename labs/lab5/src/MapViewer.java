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
import java.awt.image.BufferedImage;

public class MapViewer extends JComponent{
    private Map map;
    private Agent agent;
    private BufferedImage buffer;
    private int outputindex;
    private final int BLACK_RGB=Color.BLACK.getRGB();
    private final int WHITE_RGB=Color.WHITE.getRGB();
    private final int RED_RGB=Color.RED.getRGB();
    private double[] sensors;

    public MapViewer(){
        this.map=null;
        this.agent=null;
        this.sensors=null;
    }

    public void setup(Map map,Agent agent, double[] sensors){
        this.map=map;
        this.agent=agent;
        this.sensors=sensors;
    }

    @Override
    public void paint(Graphics g) {
        if (buffer==null||buffer.getWidth()!=getWidth()||buffer.getHeight()!=getHeight()){
            buffer=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        }
        Graphics2D g2=(Graphics2D)buffer.getGraphics();
        if (map==null){
            g2.setColor(Color.BLACK);
            g2.fillRect(0,0,getWidth(),getHeight());
            g2.setColor(Color.WHITE);
            FontMetrics fM=g2.getFontMetrics();
            String s="Please create scenario first";
            int width=(int)fM.getStringBounds(s,g2).getWidth();
            g2.drawString(s,(getWidth()-width)/2,50);
            g.drawImage(buffer,0,0,null);
            return;
        }



        for (int x=0;x<map.getWidth();x++){
            for (int y=0;y<map.getHeight();y++){
                if (map.getData(x,y)){
                    buffer.setRGB(x,y,WHITE_RGB);
                }
                else buffer.setRGB(x,y,BLACK_RGB);
            }
        } 

        Particle[] p=agent.getParticles();
        for (int x=0;x<p.length;x++){
            if (Math.round(p[x].getX())>0&&Math.round(p[x].getX())<buffer.getWidth()&&Math.round(p[x].getY())>0&&Math.round(p[x].getY())<buffer.getHeight())
                buffer.setRGB((int)Math.round(p[x].getX()),(int)Math.round(p[x].getY()),RED_RGB);
        }
        
        g2.setColor(Color.GRAY);
        g2.fillRect(map.getWidth(),0,getWidth()-map.getWidth(),map.getHeight());
        g2.fillRect(0,map.getHeight(),getWidth(),getHeight()-map.getHeight());

        int headx=(int)Math.round(agent.getX()+8*Math.sin(agent.getAngle()));
        int heady=(int)Math.round(agent.getY()-8*Math.cos(agent.getAngle()));
        int tail1x=(int)Math.round(agent.getX()+8*Math.sin(agent.getAngle()+2.5*Math.PI/3));
        int tail1y=(int)Math.round(agent.getY()-8*Math.cos(agent.getAngle()+2.5*Math.PI/3));
        int tail2x=(int)Math.round(agent.getX()+8*Math.sin(agent.getAngle()-2.5*Math.PI/3));
        int tail2y=(int)Math.round(agent.getY()-8*Math.cos(agent.getAngle()-2.5*Math.PI/3));
        drawSensorValues(g2);
        g2.setColor(Color.GREEN);
        g2.fillPolygon(new int[]{headx,tail1x,tail2x},new int[]{heady,tail1y,tail2y},3);
        g2.setColor(Color.RED);
        g2.fillRect(tail1x,tail1y,1,1);
        g2.fillRect(tail2x,tail2y,1,1);

        g.drawImage(buffer,0,0,null);
    }

    private void drawSensorValues(Graphics2D g){
        for (int x=0;x<sensors.length;x++){
            drawDistance(agent.getAngle()+sensors[x],g,Color.BLUE);
        }
    }

    private void drawDistance(double angle,Graphics2D g, Color c){
        boolean detected=false;
        double r=1;
        while(!detected){
            r+=0.5;
            int x=(int)Math.round(agent.getX()+r*Math.sin(angle));
            int y=(int)Math.round(agent.getY()-r*Math.cos(angle));
            if (x<0||x>map.getWidth()-1||y<0||y>map.getHeight()-1)detected=true;
            else if (!map.getData(x,y))detected=true;
            if (agent.getMaxr()>0&&r>=10*agent.getMaxr())detected=true;
        }
        int x=(int)Math.round(agent.getX()+r*Math.sin(angle));
        int y=(int)Math.round(agent.getY()-r*Math.cos(angle));
        g.setColor(c);
        g.drawLine(agent.getX(),agent.getY(),x,y);
    }
}


