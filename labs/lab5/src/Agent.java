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

public class Agent {
    private double xpos,ypos;
    private double angle;
    private int prevdir, prevreset;
    private Map map;
    private Random rnd;
    private ParticleFilter pf;
    private double[] sensors;
    private int movnoise, rotnoise,sensnoise, maxr;
    private int wallhit=0;
    private boolean dir, compass;

    public Agent(int x, int y, double angle, double[] sensors, Map map, int particles, int movnoise, int rotnoise,int sensnoise,boolean compass, int maxr){
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
        return (int)Math.round(xpos);
    }

    public int getY() {
        return (int)Math.round(ypos);
    }

    public double getAngle() {
        return angle;
    }


    public Particle[] getParticles(){
        return pf.getParticles();
    }
    private boolean lock=false;
    private int step=0;
    public void step(){
        int action;
        double value;
        if (wallhit<=0){
            Particle[] s=pf.getParticles();
            double xsum=0,ysum=0,asum=0,x2sum=0,y2sum=0,a2sum=0,wangle=0;
            for (int i=1;i<s.length;i++){
                xsum+=s[i].getX();
                ysum+=s[i].getY();
                asum+=s[i].getA();
                x2sum+=s[i].getX()*s[i].getX();
                y2sum+=s[i].getY()*s[i].getY();
                a2sum+=s[i].getA()*s[i].getA();
            }
            double xmean=xsum/s.length;
            double ymean=ysum/s.length;
            double amean=asum/s.length;
            double xstdev=Math.sqrt((x2sum - xsum*xmean)/(s.length - 1));
            double ystdev=Math.sqrt((y2sum - ysum*ymean)/(s.length - 1));
            double astdev=Math.sqrt((a2sum - asum*amean)/(s.length - 1));

            for (int i=1;i<s.length;i++){
                wangle+=map.getDirection((int)Math.round(s[i].getX()),(int)Math.round(s[i].getY()),amean);
            }
            wangle/=s.length;
            step++;
            if (10*astdev+xstdev+ystdev<130||step>20)lock=true;
            else if (10*astdev+xstdev+ystdev>180)lock=false;

            if (Math.abs(wangle)<Math.PI/4||!lock){
                action=ParticleFilter.ACTION_MOVE;
                value=3;
            }
            else{
                action=ParticleFilter.ACTION_ROTATE;
                value=wangle/10;
            }

        }
        else{
            if (wallhit>10){
                action=ParticleFilter.ACTION_ROTATE;

                if (dir)value=Math.PI/20;
                else value=-Math.PI/20;
            }
            else{
                action=ParticleFilter.ACTION_MOVE;
                value=2;
            }
            wallhit--;
        }
        prevreset++;
        if (prevreset>2){
            prevdir=-1;
            prevreset=0;
        }

        if (action==ParticleFilter.ACTION_MOVE){
            if (!moveAhead(value)){
                wallhit=15;
                dir=rnd.nextBoolean();
            }
        }
        if (action==ParticleFilter.ACTION_ROTATE)rotate(value);

        if (compass)pf.applyCompassData(angle);
        double[] sensors=getSensorValues();
        pf.step(sensors,action,value);

    }

    private void rotate(double ang){
        angle+=ang;
        while(angle<0)angle+=2*Math.PI;
        while(angle>2*Math.PI)angle-=2*Math.PI;
    }

    private boolean moveAhead(double d){
        double nx=xpos+d*Math.sin(angle);
        double ny=ypos-d*Math.cos(angle);
        if (nx<0||nx>map.getWidth()-1||ny<0||ny>map.getHeight()-1||!map.getData((int)Math.round(nx),(int)Math.round(ny)))return false;
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
