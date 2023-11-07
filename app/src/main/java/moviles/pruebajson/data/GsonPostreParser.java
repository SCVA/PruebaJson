package moviles.pruebajson.data;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GsonPostreParser {
    public List readJsonStream(InputStream in) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List postres = new ArrayList();
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.nextName();
        reader.nextString();
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            Postre postrecito = gson.fromJson(reader, Postre.class);
            postres.add(postrecito);
        }
        reader.endArray();
        reader.endObject();
        reader.close();
        return postres;
    }

}
