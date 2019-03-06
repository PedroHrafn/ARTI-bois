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
import java.util.Random;

public class GameAgent {
    private int xpos,ypos;
    private double angle;
    private Map map;
    private Random rnd;
    private ParticleFilter pf;
    private double[] sensors;
    private int movnoise, rotnoise,sensnoise, maxr;
    private boolean compass;

    public GameAgent(int x, int y, double angle, double[] sensors, Map map, int particles, int movnoise, int rotnoise,int sensnoise,boolean compass, int maxr){
        this.xpos=x;this.ypos=y;this.angle=angle;this.map=map;
        this.rnd=new Random();
        this.sensors=sensors;
        this.compass=compass;
        this.maxr=maxr;
        this.pf=new ParticleFilter(map,particles,sensors,movnoise,rotnoise,sensnoise,maxr);
        this.movnoise=movnoise;
        this.rotnoise=rotnoise;
        this.sensnoise=sensnoise;

    }

    public int getX() {
        return xpos;
    }

    public int getY() {
        return ypos;
    }

    public double getAngle() {
        return angle;
    }

    public Particle[] getParticles(){
        return pf.getParticles();
    }


    public void step(char key){
        int action=-1;
        double value=0;
        if (key=='a'){
            action=ParticleFilter.ACTION_ROTATE;
            value=-Math.PI/10;
        }
        else if (key=='d'){
            action=ParticleFilter.ACTION_ROTATE;
            value=Math.PI/10;
        }
        else if (key=='w'){
            action=ParticleFilter.ACTION_MOVE;
            value=3;
        }
        else if (key=='s'){
            action=ParticleFilter.ACTION_MOVE;
            value=-3;
        }
        if (action>-1){
            if (action==ParticleFilter.ACTION_MOVE)moveAhead(value);
            if (action==ParticleFilter.ACTION_ROTATE)rotate(value);
			double[] sensors=getSensorValues();
            pf.step(sensors,action,value);
			if (compass)pf.applyCompassData(angle);
        }
    }

    private void rotate(double ang){
        angle+=ang;
        while(angle<0)angle+=2*Math.PI;
        while(angle>2*Math.PI)angle-=2*Math.PI;
    }

    private boolean moveAhead(double d){
        int nx=(int)Math.round(xpos+d*Math.sin(angle));
        int ny=(int)Math.round(ypos-d*Math.cos(angle));
        if (nx<0||nx>map.getWidth()-1||ny<0||ny>map.getHeight()-1||!map.getData(nx,ny))return false;
        xpos=nx;
        ypos=ny;
        return true;
    }

    private double[] getSensorValues(){
        double vals[]=new double[sensors.length];
        for (int x=0;x<sensors.length;x++){
            vals[x]=getDistance(angle+sensors[x]);
            vals[x]+=(sensnoise*vals[x]/100.0)*rnd.nextGaussian();
        }
        return vals;
    }

    public int getMaxr() {
        return maxr;
    }

    public boolean getCompass() {
        return compass;
    }

    private double getDistance(double angle){
        boolean detected=false;
        double r=1;
        while(!detected){
            r+=0.5;
            int x=(int)Math.round(xpos+r*Math.sin(angle));
            int y=(int)Math.round(ypos-r*Math.cos(angle));
            if (x<0||x>map.getWidth()-1||y<0||y>map.getHeight()-1)detected=true;
            else if (!map.getData(x,y))detected=true;
            if (maxr>0&&r>=10*maxr)detected=true;
        }
        return r;
    }
}
