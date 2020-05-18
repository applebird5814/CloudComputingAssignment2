package com.example.cloudass2.controller;


import com.example.cloudass2.entity.Response;
import com.example.cloudass2.util.BigQueryUtil;
import com.example.cloudass2.util.COVIDnode;
import com.example.cloudass2.util.CloudTranslation;
import com.example.cloudass2.util.TextToSpeech;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.storage.Storage;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author miaos
 */
@RequestMapping("/ai")
@Controller
public class AIController {
    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    private Storage storage;

    private final String[] imageFileExtension = new String[]{".jpg", ".jpeg", ".gif", ".png", ".ico"};
    private final String[] resultSet = new String[]{"VERY_LIKELY","LIKELY", "POSSIBLE","UNLIKELY" , "VERY_UNLIKELY", "UNKNOWN"};

    @Value("${gcs-resource-test-bucket}")
    private String bucketName;

    @Autowired
    private ResourceLoader resourceLoader;

    @ResponseBody
    @RequestMapping("/test")
    private String test() throws InterruptedException {
        BigQueryUtil bigQueryUtil = new BigQueryUtil();
        HashMap<String,COVIDnode> map;
        try {
            map=bigQueryUtil.getCOD19("Australia");
        }catch (Exception e)
        {
            return e.getMessage();
        }
        return map.toString();
    }


    /*
    @ResponseBody
    @RequestMapping("/speechToText")
    public String speechToText(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] stream = file.getBytes();
        SpeechToText speechToText = new SpeechToText();
        String s = speechToText.transferBytesIntoTextString(stream);
        System.out.println(s);

        String uuid = "test";
        String fileName = uuid+".mp3";
        String uploadUrl = "gs://"+bucketName+"/"+fileName;
        Resource resource = new GoogleStorageResource(storage,uploadUrl);
        try (OutputStream os = ((WritableResource) resource).getOutputStream()) {
            os.write(stream);
        }catch (Exception e)
        {

        }
        return new Gson().toJson(new Response(true,s));
    }*/

    @ResponseBody
    @RequestMapping("/translation")
    private String translation(@RequestParam("text")String text,@RequestParam("language")String language) {
        System.out.println(language);
        CloudTranslation cloudTranslation = new CloudTranslation(language);
        try {
            return new Gson().toJson(new Response(true,cloudTranslation.translation(text)));
        }catch (Exception e)
        {
            return new Gson().toJson(new Response(false,e.getMessage()));
        }
    }

    @ResponseBody
    @RequestMapping("/textToSpeech")
    public String textToSpeech(@RequestParam("text") String text) throws IOException {
        TextToSpeech textToSpeech = new TextToSpeech();
        byte[] steam = textToSpeech.transferTextIntoMp3Steam(text);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fileName = uuid+".mp3";
        String uploadUrl = "gs://"+bucketName+"/"+fileName;
        Resource resource = new GoogleStorageResource(storage,uploadUrl);
        try (OutputStream os = ((WritableResource) resource).getOutputStream()) {
            os.write(steam);
        }catch (Exception e)
        {

        }
        String publicAccessUrl = "https://storage.googleapis.com/"+bucketName+"/"+fileName;
        return new Gson().toJson(new Response(true,publicAccessUrl));
    }

    @RequestMapping("/uploadImage")
    public @ResponseBody Map<String,Object> uploadImage(@RequestParam("file") MultipartFile file){
        Map<String,Object> response = new HashMap<>();
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String location ="";
        boolean isImage = false;
        for(int i=0;i<imageFileExtension.length;i++)
        {
            if(suffixName.equals(imageFileExtension[i]))
            {
                isImage=true;
            }
        }
        if(isImage)
        {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String imageName = uuid+suffixName;
            String uploadUrl = "gs://"+bucketName+"/"+imageName;
            Resource resource = new GoogleStorageResource(storage,uploadUrl);
            try (OutputStream os = ((WritableResource) resource).getOutputStream()) {
                os.write(file.getBytes());
            }catch (Exception e)
            {

            }
            String publicAccessUrl = "https://storage.googleapis.com/"+bucketName+"/"+imageName;
            if(censor(publicAccessUrl))
            {
                location="https://storage.googleapis.com/"+bucketName+"/"+imageName;
            }
            else {
                location="This image doesn't pass the censor, sorry!";
            }
        }
        else {
            location="This is not a image";
        }
        response.put("location",location);
        return response;
    }

    public boolean censor(String imageUrl)
    {
        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(
                this.resourceLoader.getResource(imageUrl), Feature.Type.SAFE_SEARCH_DETECTION);
        SafeSearchAnnotation safeSearchAnnotation =response.getSafeSearchAnnotation();
        String s = "Adult"+safeSearchAnnotation.getAdult().toString()
                +"\nspoof"+safeSearchAnnotation.getSpoof().toString()
                +"\nmedical"+safeSearchAnnotation.getMedical().toString()
                +"\nviolence"+safeSearchAnnotation.getViolence().toString()
                +"\nracy"+safeSearchAnnotation.getRacy().toString()
                ;
        for(int i=0;i<resultSet.length;i++)
        {
            if(safeSearchAnnotation.getAdult().toString().equals(resultSet[i]))
            {
                if(i<2)
                {
                    return false;
                }
                else {
                    break;
                }
            }
        }
        return true;
    }


}
