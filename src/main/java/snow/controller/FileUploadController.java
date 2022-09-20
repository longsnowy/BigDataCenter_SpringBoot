package snow.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import snow.utils.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class FileUploadController {
    SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");

    //文件上传的类
    @PostMapping("/upload")
    public Map<String,Object> fileUpload(MultipartFile file, HttpServletRequest req){
        String originName = file.getOriginalFilename();
        if (!(originName.endsWith(".xlsx")||originName.endsWith(".csv"))){
            return ApiResponse.toJsonDefault(60204,"文件格式错误");
        }
        String format = sdf.format(new Date());
//        String realPath = req.getServletContext().getRealPath("/") + format;
        String realPath = "/home/g2431/upload" + format;
        File folder = new File(realPath);
        if (!folder.exists()){
            folder.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();

        String newName = UUID.randomUUID().toString() + "." +originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        String url = null;
        try {
            System.out.println(folder + newName);
            file.transferTo(new File(folder,newName));
            url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() +"/upload"+ format + newName;
        }catch (IOException e){
            return ApiResponse.toJsonDefault(60204,"上传失败");
        }
        return ApiResponse.toJsonSuccess(20000,url);
    }

    private void excelToTable(String path){

    }
}
