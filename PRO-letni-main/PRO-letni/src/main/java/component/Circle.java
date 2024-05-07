package component;

import java.awt.*;

public class Circle extends BaseComponent {

    private static int circleCount = 1;

    public Circle() {
        setName("Circle " + circleCount);
        circleCount++;
    }

    public Circle(int x, int y, int radius) {
        this();
        setX(x);
        setY(y);
        setRadius(radius); //this.radius = radius;
    }

    public Circle(int x, int y, int radius, Color color) {
        setX(x);
        setY(y);
        setRadius(radius);
        setColor(color);
    }

    public int getRadius() {
        return getWidth() / 2;
    }

    public void setRadius(int radius) {
        super.setWidth(radius * 2);
        super.setHeight(radius * 2);
    }

    @Override
    public void setWidth(int width) {
        super.setHeight(width);
        super.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        super.setWidth(height);
        super.setHeight(height);
    }

    @Override
    public String toSVG() {
        return String.format("<circle cx=\"%d\" cy=\"%d\" r=\"%d\" fill=\"%s\"/>", this.getX(), this.getY(), this.getRadius(), this.colorToString());
    }
}
