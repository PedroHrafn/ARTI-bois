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

public class MapEditorTab extends JPanel {
    private MapEditor me;
    public MapEditorTab(int width, int height, RandomMazeTab rm){
        final RandomMazeTab rmt=rm;
        setLayout(new BorderLayout());
        JPanel topPanel=new JPanel();
        JButton maze=new JButton("Get Maze");
        JButton clear=new JButton("Clear");
        topPanel.add(maze);
        topPanel.add(clear);
        add(topPanel,BorderLayout.NORTH);
        me=new MapEditor(width,height);
        add(me,BorderLayout.CENTER);
        maze.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                me.setMap(rmt.getMap());
            }
        });
        clear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                me.clearMap();
            }
        });
    }

    public void setMap(boolean[][] map){
        me.setMap(map);
    }
    public MapEditor getMapEditor() {
        return me;
    }
}
