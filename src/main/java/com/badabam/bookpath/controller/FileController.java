package com.badabam.bookpath.controller;

import com.badabam.bookpath.config.PathDeterminant;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@Controller
public class FileController {

    PathDeterminant pathDeterminant = new PathDeterminant();
    private final String DIRECTORY = pathDeterminant.getOS_TYPE();

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, @RequestParam("username") String username) throws IOException {
        String fileName = file.getOriginalFilename();
        File saveFile = new File(DIRECTORY + fileName);
        log.info("upload request...");
        try {
            if (!saveFile.exists()) {
                saveFile.getParentFile().mkdir();
            }
            /* 응답 */
            JsonObject param = new JsonObject();
            HttpHeaders response_header = new HttpHeaders();
            response_header.set("content-type", "application/json");
            if (!file.getOriginalFilename().isEmpty()) {
                file.transferTo(saveFile);
                param.addProperty("result", true);
                param.addProperty("msg", "File uploaded successfully.");
                param.addProperty("fileName", fileName);
                param.addProperty("username", username);
            } else {
                param.addProperty("msg", "Please select a valid mediaFile..");
            }
            log.info("upload processing done!");
            return new ResponseEntity<>(param.toString(), response_header, HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
