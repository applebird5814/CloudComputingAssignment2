package com.example.cloudass2.Controller;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.ComputeEngineCredentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequestMapping("vision")
@Controller
public class VisionController {
    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    private String imageFileExtension[] = new String[]{".jpg", ".jpeg", ".gif", ".png", ".ico"};
    private String resultSet[] = new String[]{"VERY_LIKELY","LIKELY", "POSSIBLE","UNLIKELY" , "VERY_UNLIKELY", "UNKNOWN"};


    private void authCompute() {
        // Explicitly request service account credentials from the compute engine instance.
        GoogleCredentials credentials = ComputeEngineCredentials.create();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        System.out.println("Buckets:");
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
    }

    @RequestMapping("/test")
    public void test(){
        authCompute();
    }


    @RequestMapping("/uploadImage")
    public @ResponseBody Map<String,Object> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
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
            System.out.println("调用成功");
            String realPath = httpServletRequest.getSession().getServletContext().getRealPath("/");
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String imageName = uuid+suffixName;
            System.out.println("文件名"+imageName);
            String direPath = realPath+"uploadImage\\test";
            File direFile = new File(direPath);
            if(direFile.exists() == false || direFile.isDirectory() == false){
                direFile.mkdir();
            }
            String path = direPath+"\\"+imageName;
            try {
                file.transferTo(new File(path));
            }catch (Exception e)
            {

            }
            location="/uploadImage/test/"+imageName;
        }
        else {
            location="不是图片";
        }

        response.put("location",location);
        return response;
    }

    @ResponseBody
    @RequestMapping("/censor")
    public String censor(@RequestParam("url") String imageUrl)
    {
        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(
                this.resourceLoader.getResource(imageUrl), Feature.Type.SAFE_SEARCH_DETECTION);
        SafeSearchAnnotation safeSearchAnnotation =response.getSafeSearchAnnotation();
        String s = "Adult"+safeSearchAnnotation.getAdult().toString()+
                "\nspoof"+safeSearchAnnotation.getSpoof().toString()
                +"\nmedical"+safeSearchAnnotation.getMedical().toString()
                +"\nviolence"+safeSearchAnnotation.getViolence().toString()
                +"\nracy"+safeSearchAnnotation.getRacy().toString()
                ;
        System.out.println(s);
        for(int i=0;i<resultSet.length;i++)
        {
            if(safeSearchAnnotation.getRacy().toString().equals(resultSet[i]))
            {
                if(i<2)
                {
                    return "该图片没有通过审核";
                }
                else {
                    break;
                }
            }
        }
        return"图片通过了审核";
    }


}
