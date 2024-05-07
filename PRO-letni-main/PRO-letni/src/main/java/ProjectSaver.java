import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import serializers.ColorDeserializer;
import serializers.ColorSerializer;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ProjectSaver {

    private ComponentList componentList;

    private final ObjectMapper objectMapper;

    public ProjectSaver(ComponentList componentList) {
        this.componentList = componentList;
        this.objectMapper = new ObjectMapper();

        SimpleModule awtModule = new SimpleModule("AWT Module");
        awtModule.addSerializer(Color.class, new ColorSerializer());
        awtModule.addDeserializer(Color.class, new ColorDeserializer());

        objectMapper.registerModule(awtModule);
    }

    public boolean saveProject(String fileName) {
        try {
            objectMapper.writeValue(new File(fileName), componentList);
            String t = objectMapper.writeValueAsString(componentList);
            System.out.println(t);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadProject(String fileName) {

        try {
            File jsonFile = new File(fileName);
            Scanner scanner = new Scanner(jsonFile);

            StringBuilder jsonString = new StringBuilder();

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                jsonString.append(data);
            }

            scanner.close();


            ComponentList loadedList = objectMapper.readValue(new File(fileName), ComponentList.class);
            componentList.setBaseComponents(loadedList.getComponents());
            componentList.setEditTime(loadedList.getEditTime());

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
