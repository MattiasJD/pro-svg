package component;

import java.awt.*;

public class Rectangle extends BaseComponent {
    private static int rectangleCount = 1;

    public Rectangle() {
        setName("Rectangle " + rectangleCount);
        rectangleCount++;
    }

    public Rectangle(int x, int y) {
        this();
        setX(x);
        setY(y);
    }

    public Rectangle(int x, int y, int width, int height, Color color) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setColor(color);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
    }

    @Override
    public String toSVG() {
        return String.format("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\"/>", this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.colorToString());
    }
}
