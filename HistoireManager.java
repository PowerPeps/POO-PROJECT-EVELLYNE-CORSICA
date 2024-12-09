
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class HistoireManager {
    private static final Gson gson = new Gson();

    public static List<Scene> loadFromJson(String filePath) throws IOException {
        Type listType = new TypeToken<List<Scene>>() {}.getType();
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, listType);
        }
    }

    public static void saveToJson(List<Scene> scenes, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(scenes, writer);
        }
    }
}
