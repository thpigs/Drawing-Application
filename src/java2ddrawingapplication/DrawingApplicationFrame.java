/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import java.awt.Color;
/**
 *
 * @author acv
 */
///*

public class DrawingApplicationFrame extends JFrame
{

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    private final JPanel holder;
    private final JPanel topLine;
    private final JPanel botLine;
    // create the widgets for the firstLine Panel.
    private final JLabel shape;
    public final JComboBox selector;
    private final JButton color1;
    private final JButton color2;
    private final JButton undo;
    private final JButton clear;
    //create the widgets for the secondLine Panel.
    private final JLabel options;
    private final JCheckBox filled;
    private final JCheckBox useGradient;
    private final JCheckBox dashed;
    private final JLabel lineWidth;
    public JSpinner width;
    private final JLabel dashLength;
    public JSpinner length;
    // Variables for drawPanel.
    public DrawPanel drawPanel = new DrawPanel();
    public MyShapes currentShape;
    public Color primColor = Color.BLACK;
    public Color secColor = Color.BLACK;
    public boolean fill; 
    ArrayList<MyShapes> shapes = new ArrayList<MyShapes>();
    // add status label
    public JLabel status;
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        // add widgets to panels
        topLine = new JPanel();
        botLine = new JPanel();
        
        //first line widgets
        shape = new JLabel("Shape:");
        topLine.add(shape);
        String[] shapesList = {"Line","Oval","Rectangle"};
        selector = new JComboBox(shapesList);
        selector.setSelectedIndex(0);
        topLine.add(selector);
        color1 = new JButton("1st Color...");
        color2 = new JButton("2nd Color...");
        undo = new JButton("Undo");
        clear = new JButton("Clear");
        topLine.add(color1);
        topLine.add(color2);
        topLine.add(undo);
        topLine.add(clear);
        topLine.setBackground(new Color(0,255,255));

        //second line widgets
        options = new JLabel("Options:");
        botLine.add(options);
        filled = new JCheckBox("Filled");
        useGradient = new JCheckBox("Use Gradient");
        dashed = new JCheckBox("Dash Length");
        lineWidth = new JLabel("LineWidth:");
        dashLength = new JLabel("Dash Length:");
        width = new JSpinner();
        length = new JSpinner();
        botLine.add(filled);
        botLine.add(useGradient);
        botLine.add(dashed);
        botLine.add(lineWidth);
        botLine.add(width);
        botLine.add(dashLength);
        botLine.add(length);
        botLine.setBackground(new Color(0,255,255));

        //combining all settings panels
        holder = new JPanel();
        holder.setLayout(new GridLayout(2,1));
        holder.add(topLine);
        holder.add(botLine);
        
        // add topPanel to North, drawPanel to Center, and statusLabel to South
        status = new JLabel();
        drawPanel.setBackground(Color.WHITE);
        add(holder, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        status.setVisible(false);
        
        //add listeners and event handlers
        ButtonHandler erase = new ButtonHandler();
        ButtonHandler wipe = new ButtonHandler();
        ButtonHandler firstColor = new ButtonHandler();
        ButtonHandler secondColor = new ButtonHandler();
        color1.addActionListener(firstColor);
        color2.addActionListener(secondColor);
        undo.addActionListener(erase);
        clear.addActionListener(wipe);
                
        DrawPanel.MouseHandler handler = drawPanel.new MouseHandler();
        drawPanel.addMouseListener(handler);
        drawPanel.addMouseMotionListener(handler);
        
        // Set standard values for JSpinners
        width.setValue(5);
        length.setValue(5);
   }

    // Create event handlers, if needed
    private class ButtonHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event) 
        {
            if (event.getSource() == color1)
            {
                primColor = JColorChooser.showDialog(color1, "Choose a color", primColor);
            }
            
            if (event.getSource() == color2)
            {
                secColor = JColorChooser.showDialog(color2, "Choose a color", secColor);

            }
            
            if (event.getSource() == undo && shapes.isEmpty() == false)
            {
                shapes.remove(shapes.size()-1);
                repaint();
            }
            
            if (event.getSource() == clear)
            {
                shapes.clear();
                repaint();
            }
        }
    }
    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {
        public DrawPanel()
        {
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
             
            //loop through and draw each shape in the shapes arraylist
            for(MyShapes shape : shapes)
            {
                shape.draw(g2d);
            }
        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {
            
            public void mousePressed(MouseEvent event)
            {
                // All shapes will be dots 
                Point point = new Point(event.getX(),event.getY());
                float dashLength[] = {(Integer)length.getValue()};
                
                // Lines
                if (selector.getSelectedItem().equals("Line") && useGradient.isSelected() && dashed.isSelected())
                {   
                    currentShape = new MyLine(point, point, new GradientPaint(0, 0, primColor, 50, 50, secColor, true), new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashLength, 0));
                    shapes.add(currentShape);
                }
                else if (selector.getSelectedItem().equals("Line") && useGradient.isSelected() && dashed.isSelected() == false)
                {
                    currentShape = new MyLine(point, point, new GradientPaint(0, 0, primColor, 50, 50, secColor, true), new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    shapes.add(currentShape);
                }
                else if (selector.getSelectedItem().equals("Line") && useGradient.isSelected() == false && dashed.isSelected())
                {
                    currentShape = new MyLine(point, point, primColor, new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashLength, 0));
                    shapes.add(currentShape);
                }
                else if (selector.getSelectedItem().equals("Line") && useGradient.isSelected() == false && dashed.isSelected() == false)
                {
                    currentShape = new MyLine(point, point, primColor, new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    shapes.add(currentShape);
                }
                
                // Rectangles
                if (selector.getSelectedItem().equals("Rectangle") && useGradient.isSelected() && dashed.isSelected())
                {
                    currentShape = new MyRectangle(point, point, new GradientPaint(0, 0, primColor, 50, 50, secColor, true), new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashLength, 0), filled.isSelected());
                    shapes.add(currentShape);
                }
                else if(selector.getSelectedItem().equals("Rectangle") && useGradient.isSelected() && dashed.isSelected() == false)
                {
                    currentShape = new MyRectangle(point, point, new GradientPaint(0, 0, primColor, 50, 50, secColor, true), new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), filled.isSelected());
                    shapes.add(currentShape);
                }
                else if (selector.getSelectedItem().equals("Rectangle") && useGradient.isSelected() == false && dashed.isSelected())
                {
                    currentShape = new MyRectangle(point, point, primColor, new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashLength, 0), filled.isSelected());
                    shapes.add(currentShape);
                }
                else if(selector.getSelectedItem().equals("Rectangle") && useGradient.isSelected() == false && dashed.isSelected() == false)
                {
                    currentShape = new MyRectangle(point, point, primColor, new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), filled.isSelected());
                    shapes.add(currentShape);
                }
                
                // Oval
                if (selector.getSelectedItem().equals("Oval") && useGradient.isSelected() && dashed.isSelected())
                {
                    currentShape = new MyOval(point, point, new GradientPaint(0, 0, primColor, 50, 50, secColor, true), new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashLength, 0), filled.isSelected());
                    shapes.add(currentShape);
                }
                else if(selector.getSelectedItem().equals("Oval") && useGradient.isSelected() && dashed.isSelected() == false)
                {
                    currentShape = new MyOval(point, point, new GradientPaint(0, 0, primColor, 50, 50, secColor, true), new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), filled.isSelected());
                    shapes.add(currentShape);
                }
                else if (selector.getSelectedItem().equals("Oval") && useGradient.isSelected() == false && dashed.isSelected())
                {
                    currentShape = new MyOval(point, point, primColor, new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashLength, 0), filled.isSelected());
                    shapes.add(currentShape);
                }
                else if(selector.getSelectedItem().equals("Oval") && useGradient.isSelected() == false && dashed.isSelected() == false)
                {
                    currentShape = new MyOval(point, point, primColor, new BasicStroke((Integer)width.getValue(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), filled.isSelected());
                    shapes.add(currentShape);
                }
                repaint();
                status.setText(String.format("(%d, %d)", event.getX(),event.getY()));
            }

            public void mouseReleased(MouseEvent event)
            {
                status.setText(String.format("(%d, %d)", event.getX(),event.getY()));
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                Point point = new Point(event.getX(),event.getY());
                currentShape.setEndPoint(point);
                repaint();
                status.setText(String.format("(%d, %d)", event.getX(),event.getY()));
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                status.setText(String.format("(%d, %d)", event.getX(),event.getY()));
                status.setVisible(true);
            }
        }
    }
}

