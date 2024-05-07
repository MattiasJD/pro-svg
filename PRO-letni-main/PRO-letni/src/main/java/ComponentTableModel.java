import component.BaseComponent;

import javax.swing.table.AbstractTableModel;

public class ComponentTableModel extends AbstractTableModel {
    private final ComponentList components;
    private final String[] columnNames = { "Name", "Width", "Height", "PosX", "PosY", "Rotation" };

    public ComponentTableModel(ComponentList components) {
        this.components = components;
    }

    @Override
    public int getRowCount() {
        return components.getComponents().size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return switch (column) {
            case 0 -> String.class;
            case 1, 2, 3, 4 -> Integer.class;
            case 5 -> Double.class;
            default -> Object.class;
        };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public Object getValueAt(int row, int column) {
        BaseComponent component = components.getComponents().get(row);
        return switch (column) {
            case 0 -> component.getName();
            case 1 -> component.getWidth();
            case 2 -> component.getHeight();
            case 3 -> component.getX();
            case 4 -> component.getY();
            case 5 -> component.getRotation();
            default -> "";
        };
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        BaseComponent component = components.getComponents().get(row);
        switch (column) {
            case 0 -> component.setName((String) value);
            case 1 -> component.setWidth((Integer) value);
            case 2 -> component.setHeight((Integer) value);
            case 3 -> component.setX((Integer) value);
            case 4 -> component.setY((Integer) value);
            case 5 -> component.setRotation((Double) value);
        }
    }
}