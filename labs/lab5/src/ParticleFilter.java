
/*
Copyright (C) 2010,2012

This file is part of the Particle filter applet
written by Max Bügler
http://www.maxbuegler.eu/

slight modifications by Stephan Schiffel

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

public class ParticleFilter {

    // the map
    private Map map;

    // the set of particles
    private Particle[] particles;

    // random number generator
    private Random rnd;

    // two possible actions
    public static final int ACTION_MOVE = 0;
    public static final int ACTION_ROTATE = 1;

    // angles of the sensors relative to the orientation of the robot
    private double[] sensors;

    // noise for movement, rotation and sensors (standard deviation)
    private double movnoise;
    private double rotnoise;

    private double sensnoise;

    // maximal range of the sensors
    private int maxr;

    public ParticleFilter(Map map, int particlecount, double[] sensors, int movnoise, int rotnoise, int sensnoise,
            int maxr) {
        this.map = map;
        this.sensors = sensors;
        // System.out.println("rotnoise: " + rotnoise + ", movnoise: " + movnoise);
        this.movnoise = movnoise / 10.0;
        this.rotnoise = rotnoise / 90.0;
        this.sensnoise = sensnoise;
        this.maxr = maxr;
        rnd = new Random();
        this.particles = new Particle[particlecount];
        // Initialize particles (uniform random distribution over the map)
        for (int i = 0; i < particlecount; i++) {
            // Place a particle if there is no wall
            boolean notWall = false;
            int xcord = 0;
            int ycord = 0;
            while (!notWall) {
                xcord = rnd.nextInt(map.getWidth());
                ycord = rnd.nextInt(map.getHeight());
                notWall = map.getData(xcord, ycord);
            }
            this.particles[i] = new Particle(xcord, ycord, rnd.nextDouble() * Math.PI * 2, 1.0 / particlecount);
        }

    }

    // this is the main particle filter function that is called after each step
    public void step(double[] sensorvalues, int action, double value) {
        // TODO: fill out
        applyAction(action, value);
        applyObservation(sensorvalues);
        this.particles = resample(this.particles, 0.1);
    }

    // apply the transition model to all particles
    private void applyAction(int action, double value) {
        System.out.println("apply");
        for (int i = 0; i < particles.length; i++) {
            sampleFromTransitionModel(particles[i], action, value);
        }
    }

    // change the particle p according to the transition model
    private void sampleFromTransitionModel(Particle p, int action, double value) {
        if (action == ACTION_MOVE) {
            // System.out.println("move");
            // value is the distance of the movement
            // Note that there is some uncertainty in the movement.
            // The error in the distance travelled is distributed according to
            // a gaussian distribution with standard deviation movnoise*value.
            // TODO: fill out
            double newX = p.getX() + (value + value * movnoise * rnd.nextGaussian()) * Math.sin(p.getA());
            double newY = p.getY() - (value + value * movnoise * rnd.nextGaussian()) * Math.cos(p.getA());
            // System.out.println("from x: " + p.getX() + ", to: " + newX + ", angle: " +
            // p.getA());
            // System.out.println("from y: " + p.getY() + ", to: " + newY + ", angle: " +
            // p.getA());
            p.setX(newX);
            p.setY(newY);

        } else if (action == ACTION_ROTATE) {
            // System.out.println("rotate");
            // value is the angle of the rotation (by how much the robot rotates clockwise)
            // Note that there is some uncertainty in the rotation.
            // The error in the angle is distributed according to
            // a gaussian distribution with standard deviation rotnoise*value.
            // TODO: fill out
            p.setA(p.getA() + value + rnd.nextGaussian() * rotnoise * value);

        }

    }

    // apply the sensor model to all particles
    private void applyObservation(double[] sensorvalues) {
        System.out.println("observe");
        // Weight each particle according to observation probability
        double total = 0;
        for (int i = 0; i < this.particles.length; i++) {
            double probability = getObservationProbability(sensorvalues, this.particles[i]);
            this.particles[i].setW(probability);
            total += probability;
        }
        // Normalize weights, so they sum up to 1
        if (total != 0.0) {
            for (int i = 0; i < this.particles.length; i++) {
                this.particles[i].setW(this.particles[i].getW() / total);
            }
        }

    }

    // returns P(e|x)
    private double getObservationProbability(double[] sensorvalues, Particle p) {
        return map.getObservationProbability(p.getX(), p.getY(), p.getA(), sensors, sensorvalues, sensnoise, maxr);
    }

    // resample the list of particles
    private Particle[] resample(Particle[] s, double confusion) {
        System.out.println("resample");
        int n = s.length;
        Particle[] out = new Particle[n];
        double[] c = new double[n];
        c[0] = s[0].getW();
        for (int i = 1; i < n; i++) {
            c[i] = c[i - 1] + s[i].getW();
        }
        double[] u = new double[n];
        u[0] = rnd.nextDouble() / n;
        int i = 0;
        int outindex = 0;
        for (int j = 0; j < n; j++) {
            while (u[j] > c[i]) {
                i = i + 1;
            }

            // if (rnd.nextDouble()>confusion)

            if (rnd.nextDouble() > confusion) {
                // without noise
                // out[outindex++]=new Particle(s[i].getX(),s[i].getY(),s[i].getA(),1.0/n);

                // adding some noise
                out[outindex++] = new Particle(s[i].getX() + 0.5 * movnoise * rnd.nextGaussian(),
                        s[i].getY() + 0.5 * movnoise * rnd.nextGaussian(),
                        s[i].getA() + 0.005 * rotnoise * rnd.nextGaussian(), 1.0 / n);
            } else { // add some random poses
                int px = rnd.nextInt(map.getWidth());
                int py = rnd.nextInt(map.getHeight());
                while (!map.getData(px, py)) {
                    px = rnd.nextInt(map.getWidth());
                    py = rnd.nextInt(map.getHeight());
                }
                out[outindex++] = new Particle(px, py, rnd.nextDouble() * 2 * Math.PI, 1.0 / n);
            }
            if (j + 1 < n)
                u[j + 1] = u[j] + (1.0 / n);
        }
        return out;
    }

    // ignore every thing below this point

    public void applyCompassData(double angle) {
        System.out.println("applycomp");
        for (int i = 0; i < particles.length; i++) {
            particles[i].setA(angle);
        }
    }

    public Particle[] getParticles() {
        System.out.println("getpart");
        return particles;
    }

}
