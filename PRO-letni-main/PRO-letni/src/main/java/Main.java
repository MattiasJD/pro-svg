import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 800;
    private static EditorWindow editorWindow;
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->  editorWindow = new EditorWindow(WIDTH, HEIGHT)
        );

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(editorWindow != null){

                    editorWindow.updateTime();
                }
            }
        });

        timer.start();
    }
}