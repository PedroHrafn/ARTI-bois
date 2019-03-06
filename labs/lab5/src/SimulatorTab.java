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
import java.util.Random;

public class SimulatorTab extends JPanel {
    private MapViewer mv;
    private Thread simulationthread;
    private Map map;
    private Agent agent;
    private MapEditor me;
    private SetupTab st;
    private ScenarioTab sct;
    private JButton startstop;
    private boolean stopped;
    private Random rnd;

    public SimulatorTab(MapEditor me, SetupTab st, ScenarioTab scet){
        this.me=me;
        this.st=st;
        this.sct=scet;
        this.rnd=new Random();
        setLayout(new BorderLayout());
        JPanel topPanel=new JPanel();
        startstop=new JButton("Start");
        JButton loadmap=new JButton("Get map from editor");
        topPanel.add(startstop);

        startstop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (stopped){
                    startstop.setText("Stop");
                    stopped=false;
                    simulationthread=new SimuThread();
                    simulationthread.start();
                }
                else{
                    stopped=true;
                    startstop.setText("Start");
                }
            }
        });
        loadmap.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (sct.getTx()>=0&&sct.getSx()>=0)
                setup();
            }
        });
        stopped=true;
        add(topPanel,BorderLayout.NORTH);
        mv=new MapViewer();
        add(mv,BorderLayout.CENTER);
    }

    public void stop(){
        stopped=true;
        startstop.setText("Start");
    }

    public void setup(){
        stopped=true;        
        startstop.setText("Start");
        map=new Map(me.getMap(),sct.getTx(),sct.getTy());
        agent=new Agent(sct.getSx(),sct.getSy(),rnd.nextDouble()*2*Math.PI,st.getSensors(),map,st.getParticles(),st.getMoveNoise(),st.getRotNoise(),st.getSensNoise(),st.getCompass(),st.getSensDist());
        mv.setup(map,agent,st.getSensors());
        mv.repaint();
    }

    class SimuThread extends Thread{
        public void run(){
            if (agent==null||mv==null){
                stopped=true;
                startstop.setText("Start");
                return;
            }
            while (!stopped){

                agent.step();
                mv.repaint();
                try{
                    Thread.sleep(10);
                }
                catch(Exception e){}
            }
        }
    }
}
