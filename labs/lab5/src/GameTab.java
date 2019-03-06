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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GameTab extends JPanel {
    private GameViewer gv;
    private Map map;
    private GameAgent agent;
    private MapEditor me;
    private SetupTab st;
    private JButton showagent;
    private boolean showa;
    private Random rnd;

    public GameTab(MapEditor me, SetupTab st){
        this.me=me;
        this.st=st;
        this.rnd=new Random();
        setLayout(new BorderLayout());
        JPanel topPanel=new JPanel();
        showagent=new JButton("Show agent");
        topPanel.add(showagent);
        showagent.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (!showa){
                    showagent.setText("Hide agent");
                }
                else{
                    showagent.setText("Show agent");
                }
                showa=!showa;
                gv.setShowagent(showa);
            }
        });

        add(topPanel,BorderLayout.NORTH);
        gv=new GameViewer();
        //setup();
        add(gv,BorderLayout.CENTER);
        showagent.addKeyListener(new Listener());
        addKeyListener(new Listener());
    }

    public void setup(){
        showa=false;
        gv.setShowagent(false);
        showagent.setText("Show agent");
        map=new Map(me.getMap(),0,0);
        int ax=rnd.nextInt(map.getWidth());
        int ay=rnd.nextInt(map.getHeight());
        while(!map.getData(ax,ay)){
            ax=rnd.nextInt(map.getWidth());
            ay=rnd.nextInt(map.getHeight());
        }
        agent=new GameAgent(ax,ay,rnd.nextInt(20)*Math.PI/10,st.getSensors(),map,st.getParticles(),st.getMoveNoise(),st.getRotNoise(),st.getSensNoise(),st.getCompass(),st.getSensDist());
        gv.setup(map,agent,st.getSensors());
        gv.repaint();
    }

    public void keypress(char key){
        agent.step(key);
        gv.repaint();
    }
    class Listener implements KeyListener {
        public void keyTyped(KeyEvent e) {

            agent.step(e.getKeyChar());
            gv.repaint();
        }

        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }
    }
    
}
