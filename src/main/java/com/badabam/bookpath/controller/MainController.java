package com.badabam.bookpath.controller;

import com.badabam.bookpath.domain.RootDirDTO;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@Controller
public class MainController {
    SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public final static String DATA_PATH = "sampleData/sampleBookmark.json";
    public final static int SecondToMillis = 1000;
    public static JsonArray bookmark_json;

    static {
        try {
            bookmark_json = (JsonArray) new JsonParser().parse(new InputStreamReader(new FileInputStream(DATA_PATH), StandardCharsets.UTF_8));
            //bookmark_json = (JsonArray) new JsonParser().parse(new FileReader(DATA_PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/rootDir/{count}")
    public ResponseEntity<String> getRootDir(@PathVariable("count") Long count) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //Gson gson = new Gson();

        JsonObject json = new JsonObject();
        json.addProperty("data", "#0. 개인공부");
        json.addProperty("link", "");
        long ADD_DATE = 1624807905L; // 초단위로 바꾸려면 1000
        long LAST_MODIFIED = 1632394783;
        ADD_DATE *= 1000;
        LAST_MODIFIED *= 1000;
        json.addProperty("ADD_DATE", smd.format(ADD_DATE));
        json.addProperty("LAST_MODIFIED", smd.format(LAST_MODIFIED));

        return new ResponseEntity<>(gson.toJson(json), HttpStatus.OK);
    }

    /**
     * 상위 루트의 정보를 count 개 만큼 반환
     * 상위에 노출되는 root directory의 개수는 int 형 범위라고 가정
     *
     * @param count
     * @return
     */
    @GetMapping("/rootDirs={count}")
    public ResponseEntity<String> getRootDirs(@PathVariable("count") int count) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray result = new JsonArray();
        //JsonArray jsonArray = bookmark_json.getAsJsonArray("type");
        log.info("HTTP GET REQUEST : /rootDirs="+count);
        //log.info(bookmark_json.toString());

        /* count가 양수이고, 실제 상위 디렉토리의 수보다 작거나 값을 요청했을 때 */
        if (count >= 0 && count <= bookmark_json.size()) {
            for (int i = 0; i < count; i++) {
                JsonObject jsonObject = (JsonObject) bookmark_json.get(i);
                if (jsonObject.get("type").getAsString().equals("directory")) {
                    JsonObject temp = new JsonObject();
                    temp.addProperty("type", "directory");
                    temp.addProperty("add_date", jsonObject.get("add_date").getAsLong() * SecondToMillis);
                    temp.addProperty("last_modified", jsonObject.get("last_modified").getAsLong() * SecondToMillis);
                    temp.addProperty("title", jsonObject.get("title").getAsString());
                    result.add(temp);
                } else {
                    break; // directory 가 더 없을 때
                }
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("-----------------------");
        log.info(gson.toJson(result));
        return new ResponseEntity<>(gson.toJson(result), HttpStatus.OK);
    }

    @GetMapping("/getChildren={value}")
    public ResponseEntity<String> getChildrens(@PathVariable("value") int value){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.info("HTTP GET REQUEST : /showChildren="+value);
        JsonObject jsonObject;
        /*상위 디렉토리 중 특정 디렉토리 번호를 기준으로 하위 디렉토리, 링크들 반환*/
        try {
            jsonObject = (JsonObject) bookmark_json.get(value);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(gson.toJson(jsonObject), HttpStatus.OK);
    }
}
