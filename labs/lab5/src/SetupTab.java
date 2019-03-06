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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SetupTab extends JPanel{
    private JTextField movenoise, rotatenoise,sensornoise, particles, distance;
    private JCheckBox compass;
    private JComboBox sensors;

    public SetupTab(){
        setLayout(new GridLayout(7,2));
        add(new JLabel("Number of sensors:",JLabel.RIGHT));
        sensors = new JComboBox(new String[]{"2","3","4","5","6","7","8","9","10"});
        sensors.setSelectedIndex(2);
        add(sensors);
        add(new JLabel("Sensor distance (0 for infinity):",JLabel.RIGHT));
        distance=new JTextField("5",5);
        add(distance);
        add(new JLabel("Agent has compass:",JLabel.RIGHT));
        compass=new JCheckBox();
        compass.setSelected(false);
        add(compass);
        add(new JLabel("Number of particles:",JLabel.RIGHT));
        particles=new JTextField("300",5);
        add(particles);
        add(new JLabel("Movement noise:",JLabel.RIGHT));
        movenoise=new JTextField("5",3);
        add(movenoise);
        add(new JLabel("Rotation noise:",JLabel.RIGHT));
        rotatenoise=new JTextField("5",3);
        add(rotatenoise);
        add(new JLabel("Sensor noise:",JLabel.RIGHT));
        sensornoise=new JTextField("5",3);
        add(sensornoise);
    }

    public int getMoveNoise(){
        try{
            return Math.max(0,Integer.parseInt(movenoise.getText()));
        }
        catch(Exception e){
            return 5;
        }
    }

    public int getRotNoise(){
        try{
            return Math.max(0,Integer.parseInt(rotatenoise.getText()));
        }
        catch(Exception e){
            return 5;
        }
    }

    public int getSensNoise(){
        try{
            return Math.max(0,Integer.parseInt(sensornoise.getText()));
        }
        catch(Exception e){
            return 5;
        }
    }

    public int getParticles(){
        try{
            return Math.max(0,Integer.parseInt(particles.getText()));
        }
        catch(Exception e){
            return 1000;
        }
    }

    public int getSensDist(){
        try{
            return Math.max(0,Integer.parseInt(distance.getText()));
        }
        catch(Exception e){
            return 1000;
        }
    }

    public boolean getCompass(){
        return compass.isSelected();
    }

    public double[] getSensors(){
        double[] out=new double[sensors.getSelectedIndex()+2];
        for (int x=0;x<out.length;x++){
            out[x]=x*(2.0*Math.PI/out.length);
        }
        return out;
    }

}
