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
import java.util.ArrayList;
import java.util.Random;

public class RandomMazeCreator extends JComponent {
    private boolean[][] maze;
    private boolean[][] map;
    private Random rnd;
    private BufferedImage buffer;
    
    public RandomMazeCreator(int width, int height){
        rnd=new Random();
        map=new boolean[width][height];
        for (int x=0;x<width;x++){
            for (int y=0;y<height;y++){
                map[x][y]=false;
            }
        }
        createMaze(20,10,5,40,0.7);
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
        g.drawImage(buffer,0,0,null);
    }

    public boolean[][] getMap() {
        return map;
    }

    public void createMaze(int waywidth, int meshx, int meshy, int ways, double spread){
        maze=new boolean[meshx][2*meshy-1];
        int wy=rnd.nextInt(maze[0].length);
        int wx;
        if (wy%2==0)wx=rnd.nextInt(maze.length-1);
        else wx=rnd.nextInt(maze.length-1);
        maze[wx][wy]=true;
        MazePoint start=new MazePoint(wx,wy);
        ArrayList<MazePoint> candidates=getCandidates();
        for (int x=0;x<ways&&candidates.size()>0;x++){
            MazePoint p=candidates.get(rnd.nextInt(candidates.size()));
            if (rnd.nextDouble()<spread){
                MazePoint mean=getMazeMean();
                MazePoint maxp=p;
                for (int c=0;c<candidates.size();c++){
                    MazePoint tp=candidates.get(c);
                    if (tp.getDistanceTo(mean)>maxp.getDistanceTo(mean)){
                        maxp=tp;
                    }
                }
                p=maxp;
            }
            maze[p.getX()][p.getY()]=true;
            candidates=getCandidates();
        }
        maze2map(waywidth,meshx,meshy);
    }



    private void maze2map(int waywidth, int meshx, int meshy){
        for (int x=0;x<map.length;x++){
            for (int y=0;y<map[x].length;y++){
                map[x][y]=false;
            }
        }
        double xsize=(map.length-waywidth)/(double)(meshx-1);
        double ysize=(map[0].length-waywidth)/(double)(meshy-1);
        for (int y=0;y<maze[0].length;y++){
            for (int x=0;(y%2==0&&x<maze.length-1)||(y%2==1&&x<maze.length);x++){
                if (maze[x][y]){
                    if (y%2==0){
                        int ty=(int)Math.round((y/2)*ysize);
                        int tx=(int)Math.round(x*xsize);
                        for (int my=ty;my<ty+waywidth+rnd.nextInt(4)&&my<map[0].length;my++){
                            for (int mx=tx;mx<Math.round(tx+xsize)+1&&mx<map.length;mx++){

                                map[mx][my]=true;
                            }
                        }
                    }
                    else{
                        int ty=(int)Math.round((y/2)*ysize);
                        int tx=(int)Math.round(x*xsize);

                        for (int mx=tx;mx<tx+waywidth+rnd.nextInt(4)&&mx<map.length;mx++){
                            for (int my=ty;my<Math.round(ty+ysize+waywidth)&&my<map[0].length;my++){
                                map[mx][my]=true;
                            }
                        }
                    }
                }
            }
        }
        repaint();

    }

    private MazePoint getMazeMean(){
        int xsum=0,ysum=0,sum=0;
        for (int y=0;y<maze[0].length;y++){
            for (int x=0;(y%2==0&&x<maze.length-1)||(y%2==1&&x<maze.length);x++){
                if (maze[x][y]){
                    xsum+=x;
                    ysum+=y;
                    sum++;
                }
            }
        }
        return new MazePoint(xsum/sum,ysum/sum);
    }

    private ArrayList<MazePoint> getCandidates(){
        ArrayList<MazePoint> out=new ArrayList<MazePoint>();
        for (int y=0;y<maze[0].length;y++){
            for (int x=0;(y%2==0&&x<maze.length-1)||(y%2==1&&x<maze.length);x++){
                if (maze[x][y]){
                    if (y%2==0){
                        if (x>0&&!maze[x-1][y]){
                            out.add(new MazePoint(x-1,y));
                        }
                        if (x<map.length-1&&!maze[x+1][y]){
                            out.add(new MazePoint(x+1,y));
                        }
                        if (y>0&&!maze[x][y-1]){
                            out.add(new MazePoint(x,y-1));
                        }
                        if (y>0&&!maze[x+1][y-1]){
                            out.add(new MazePoint(x+1,y-1));
                        }
                        if (y<maze[0].length-1&&!maze[x][y+1]){
                            out.add(new MazePoint(x,y+1));
                        }
                        if (y<maze[0].length-1&&!maze[x+1][y+1]){
                            out.add(new MazePoint(x+1,y+1));
                        }

                    }
                    else{
                        if (y>1&&!maze[x][y-2]){
                            out.add(new MazePoint(x,y-2));
                        }
                        if (y<maze[0].length-2&&!maze[x][y+2]){
                            out.add(new MazePoint(x,y+2));
                        }
                        if (y>0&&x>0&&!maze[x-1][y-1]){
                            out.add(new MazePoint(x-1,y-1));
                        }
                        if (y<maze[0].length-1&&x>0&&!maze[x-1][y+1]){
                            out.add(new MazePoint(x-1,y+1));
                        }
                        if (y>0&&!maze[x][y-1]){
                            out.add(new MazePoint(x,y-1));
                        }
                        if (y<maze[0].length-1&&!maze[x][y+1]){
                            out.add(new MazePoint(x,y+1));
                        }
                    }
                }
            }
        }
        return out;
    }


    class MazePoint{
        private int x,y;

        MazePoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public double getDistanceTo(MazePoint p){
            return Math.sqrt(Math.pow(x-p.x,2)+Math.pow(y-p.y,2));
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MazePoint point = (MazePoint) o;

            if (x != point.x) return false;
            if (y != point.y) return false;

            return true;
        }
    }


}

