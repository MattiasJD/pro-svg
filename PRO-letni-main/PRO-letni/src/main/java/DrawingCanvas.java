import component.Circle;
import component.BaseComponent;
import component.Line;
import listener.ComponentChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;


public class DrawingCanvas extends JComponent {
    private final int width;
    private final int height;

    private boolean bCreating = false;

    private final ComponentChangeListener componentChangeListener;

    private final ComponentList componentList;


    private int index = -1;

    public DrawingCanvas(int w, int h, ComponentChangeListener componentChangeListener) {
        this.width = w;
        this.height = h;
        setSize(width, height);
        this.componentChangeListener = componentChangeListener;

        setDoubleBuffered(true);

        componentList = ComponentList.getINSTANCE();

        MouseListener myMouseListener = new MouseListener();
        addMouseMotionListener(myMouseListener);
        addMouseListener(myMouseListener);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform reset = g2d.getTransform();

        for (BaseComponent p : componentList.getComponents()) {
            g2d.setColor(p.getColor());

            if (p instanceof Line) {
                Line2D line = new Line2D.Double(p.getX(), p.getY(), p.getWidth(), p.getHeight());
                AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(p.getRotation() * 120), line.getX1(), line.getY1());
                g2d.draw(at.createTransformedShape(line));
            } else {
                g2d.translate(p.getX(), p.getY());
                g2d.rotate(p.getRotation());
                if (p instanceof component.Rectangle)
                    g2d.fillRect(-p.getWidth() / 2, -p.getHeight() / 2, p.getWidth(), p.getHeight());
                if (p instanceof Circle)
                    g2d.fillOval(-p.getWidth() / 2, -p.getHeight() / 2, p.getWidth(), p.getHeight());
            }

            g2d.setTransform(reset);
        }

    }

    public void addComponent(int index) {
        this.bCreating = true;
        this.index = index;
    }



    public void updateComponent(int index) {
        this.index = index;
    }


    class MouseListener extends MouseAdapter {

        private int startX = -1;

        private int startY = -1;


        @Override
        public void mouseMoved(MouseEvent e) {

            super.mouseMoved(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (bCreating) {
                startX = e.getX();
                startY = e.getY();
                BaseComponent p = componentList.getComponents().get(index);

                p.setX(startX);
                p.setY(startY);

                componentChangeListener.updateTableRow();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            componentChangeListener.onComponentsChange();
            componentChangeListener.updateSvgTextArea();
            bCreating = false;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (index != -1 && bCreating) {
                    BaseComponent p = componentList.getComponents().get(index);

                    if (p instanceof Circle) {
                        int size = (int) Math.round(Math.sqrt(Math.pow(startX - e.getX(), 2) + Math.pow(startY - e.getY(), 2)));

                        p.setWidth(size);
                        p.setHeight(size);
                    }
                    if (p instanceof component.Rectangle) {
                        int sizeX = (int) Math.round(Math.sqrt(Math.pow(startX - e.getX(), 2)));
                        int sizeY = (int) Math.round(Math.sqrt(Math.pow(startY - e.getY(), 2)));
                        p.setWidth(sizeX);
                        p.setHeight(sizeY);
                    }
                    if (p instanceof Line) {
                        p.setWidth(e.getX());
                        p.setHeight(e.getY());
                    }

                    repaint();
                    componentChangeListener.onComponentsChange();
                }
            }

            if (SwingUtilities.isRightMouseButton(e)) {
                //rotation
                if (index != -1) {
                    BaseComponent p = componentList.getComponents().get(index);

                    int rectCenterX = p.getX();
                    int rectCenterY = p.getY();
                    double dx = e.getX() - rectCenterX;
                    double dy = e.getY() - rectCenterY;
                    double rotation = Math.atan2(dy, dx);
                    p.setRotation(rotation);
                    repaint();
                }
            }

        }
    }
}