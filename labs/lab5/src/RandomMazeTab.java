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

public class RandomMazeTab extends JPanel{
    private JTextField waywidth, meshx, meshy, ways, spread;
    private JButton create;
    private RandomMazeCreator rmc;

    public RandomMazeTab(int width, int height){
        setLayout(new BorderLayout());
        JPanel topPanel=new JPanel();
        waywidth=new JTextField("20",3);
        meshx=new JTextField("10",3);
        meshy=new JTextField("5",3);
        ways=new JTextField("40",3);
        spread=new JTextField("0.7",3);
        create=new JButton("Create");
        rmc=new RandomMazeCreator(width,height);
        topPanel.add(new JLabel("# ways:"));
        topPanel.add(ways);
        topPanel.add(new JLabel("Spread:"));
        topPanel.add(spread);
        topPanel.add(create);
        create.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try{
                    int ww=Integer.parseInt(waywidth.getText());
                    int mx=Integer.parseInt(meshx.getText());
                    int my=Integer.parseInt(meshy.getText());
                    int w=Integer.parseInt(ways.getText());
                    double s=Double.parseDouble(spread.getText());
                    rmc.createMaze(ww,mx,my,w,s);
                }
                catch(Exception ex){}
            }
        });
        add(topPanel,BorderLayout.NORTH);
        add(rmc,BorderLayout.CENTER);
    }

    public boolean[][] getMap(){
        return rmc.getMap();
    }
}
