/*
Copyright (C) 2012 Stephan Schiffel

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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PApp extends JFrame {
    private JTabbedPane pane;
    private MapEditorTab met;
    private SetupTab st;
    private SimulatorTab simt;
    private ScenarioTab sct;
    private RandomMazeTab rmt;
    private GameTab gt;

	public static void main(String[] args) {
		PApp app = new PApp();
		app.init();
		app.setSize(550,400);
		app.setVisible(true);
	}

	public PApp() {
		super("Particle Filter Demo");
	}

    public void init(){
        pane=new JTabbedPane(JTabbedPane.TOP);
        rmt=new RandomMazeTab(500,200);
        pane.addTab("Maze creator",rmt);
        met=new MapEditorTab(500,200,rmt);
        pane.addTab("Map editor",met);
        sct=new ScenarioTab(met.getMapEditor());
        pane.addTab("Scenario",sct);
        st=new SetupTab();
        pane.addTab("Simulation setup",st);

        simt=new SimulatorTab(met.getMapEditor(),st,sct);
        pane.addTab("Simulator",simt);

        gt=new GameTab(met.getMapEditor(),st);
        pane.addTab("Game",gt);

        setLayout(new BorderLayout());
        add(pane,BorderLayout.CENTER);
        add(new JLabel("Written 2010 by Max Buegler. http://www.maxbuegler.eu/"),BorderLayout.NORTH);

        pane.addMouseListener(new MouseListener(){
            int oldindex=0;
            boolean medited=false;
            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                if (pane.getSelectedIndex()!=oldindex){

                    if (pane.getSelectedIndex()==4&&sct.getTx()>=0&&sct.getSx()>=0){
                        simt.setup();
                    }
                    if (pane.getSelectedIndex()!=4)simt.stop();

                    if (pane.getSelectedIndex()==5){
                        if (!medited)met.setMap(rmt.getMap());
                        gt.setup();
                    }
                    if (oldindex==0){
                        medited=true;
                        met.setMap(rmt.getMap());
                    }
                    oldindex=pane.getSelectedIndex();
                }
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        pane.addKeyListener(new Listener());
    }
    class Listener implements KeyListener {
        public void keyTyped(KeyEvent e) {
            if (pane.getSelectedIndex()==5&&sct.getTx()>=0&&sct.getSx()>=0){
                gt.keypress(e.getKeyChar());
                gt.repaint();
            }
        }

        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }
    }

}
