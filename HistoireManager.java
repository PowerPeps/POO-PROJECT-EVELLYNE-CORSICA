
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class HistoireManager {
    private static final Gson gson = new Gson();

    public static List<Scene> loadFromJson(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class); // Lire l'objet JSON complet
            JsonArray scenesArray = jsonObject.getAsJsonArray("scenes"); // Extraire la clé "scenes"
            
            Type listType = new TypeToken<List<Scene>>() {}.getType(); // Type pour la liste de scènes
            return gson.fromJson(scenesArray, listType); // Désérialiser en liste de scènes
        }
    }


    public static void saveToJson(List<Scene> scenes, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(scenes, writer);
        }
    }
    
}
