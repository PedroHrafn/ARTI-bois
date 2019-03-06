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
public class Particle {
    private double x,y; // x and y position
    private double a; // orientation (angle in radians: 0<=a<2*PI), where 0 = NORTH, PI/2 = EAST, etc.
    private double w; // weight (0<=weight<=1)

    public Particle(double x, double y, double a,double w) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.a = a;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        while(a<0)a+=2*Math.PI;
        while(a>2*Math.PI)a-=2*Math.PI;
        this.a = a;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }
}
