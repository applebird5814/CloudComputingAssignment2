package com.example.cloudass2.Controller;


import com.example.cloudass2.Util.TextToSpeech;
import com.google.cloud.storage.Storage;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequestMapping("vision")
@Controller
public class VisionController {
    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    private Storage storage;

    private String imageFileExtension[] = new String[]{".jpg", ".jpeg", ".gif", ".png", ".ico"};
    private String resultSet[] = new String[]{"VERY_LIKELY","LIKELY", "POSSIBLE","UNLIKELY" , "VERY_UNLIKELY", "UNKNOWN"};

    @Value("${gcs-resource-test-bucket}")
    private String bucketName;

    @Autowired
    private ResourceLoader resourceLoader;

    @ResponseBody
    @RequestMapping("/test")
    public String voiceTest() throws IOException {
        TextToSpeech audio = new TextToSpeech();
        String s = "Hello java spring";
        byte[] steam = audio.transferTextIntoMp3Steam(s);
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
        return "success";
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
