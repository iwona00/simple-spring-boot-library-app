package pl.javaspringboot.libraryapp.library.utils;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class LibraryUtils {

    public static Map<String, Object> getMapFromJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Map.class);
    }

    public static Map<String, Object> getMapFromJson(JSONObject jsonObject) {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject.toString(), Map.class);
    }

    public static String createJsonSampleData(){
        return "{\"author\": {\"name\" : \"Dan\",\"surname\" : \"Brown\"},\"title\" : \"The Da Vinci Code\"}";
    }

    public static JSONObject getFirstJsonObjectFromResponseEntity(ResponseEntity<String> response) {
        final int START_INDEX    = 0;
        JSONObject jsonFirstBook = null;
        try {
            jsonFirstBook = new JSONArray(response.getBody()).getJSONObject(START_INDEX);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonFirstBook;
    }

    public static void addJwtTokenToHeaders(HttpHeaders httpHeaders) {
        httpHeaders.add("authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwicGFzc3dvcmQiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIn0.PjfBPCmST3GyjyBpNfqYCtLqZL55z4qvva-T8b3KUGo");
    }

}
