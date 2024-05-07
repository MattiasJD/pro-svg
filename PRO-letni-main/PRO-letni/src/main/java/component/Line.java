package component;

import java.awt.*;

public class Line extends BaseComponent {
    private static int lineCount = 1;

    public Line(){
        setName("Line " + lineCount);
        lineCount++;
    }

    public Line(int x, int y) {
        this();
        setX(x);
        setY(y);
    }

    public Line(int x1, int y1, int x2, int y2, Color color) {
        setX(x1);
        setY(y1);
        setWidth(x2);
        setHeight(y2);
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
        return String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>", this.getX(), this.getY(), this.getWidth(), this.getHeight(), super.colorToString());
    }
}
