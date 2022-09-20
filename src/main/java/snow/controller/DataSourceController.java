package snow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import snow.entity.DataSource;
import snow.entity.Dictionary;
import snow.service.DataSourceService;
import snow.utils.ApiResponse;
import snow.utils.CSV2Excel;
import snow.utils.ExcelToSQLUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class DataSourceController {
    @Autowired
    private DataSourceService dataSourceService;

    //创建表 Spring
    @PostMapping("/createtable/{nameIndex}/{unitIndex}/{meansIndex}")
    public Map<String, Object> createTable(MultipartFile file, @PathVariable("nameIndex") Integer nameIndex, @PathVariable("unitIndex") Integer unitIndex, @PathVariable("meansIndex") Integer meansIndex, HttpServletRequest req) {
//        String path = "/home/g2431/Documents/2018年软件基地年报.xlsx";
//        dataSourceService.createTable(path);
        String originName = file.getOriginalFilename();
        if (!(originName.endsWith(".xlsx") || originName.endsWith(".csv"))) {
            return ApiResponse.toJsonDefault(60204, "文件格式错误");
        }

        DataSource dataSource;
        try {
            dataSource = fileUpload(file, req);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.toJsonDefault(60204, "上传失败");
        }

        if (originName.endsWith("xlsx") || originName.endsWith("csv")) {
            try {
                int table = dataSourceService.createTable(dataSource.getFilePath(), nameIndex, unitIndex, meansIndex, dataSource.getTableName());
                System.out.println("table: " + table + dataSource.getTableName());
                dataSourceService.addDataSource(dataSource);
                System.out.println("dataSource: " + dataSource);
            } catch (Exception e) {
                System.out.println(e.toString());
                return ApiResponse.toJsonDefault(60205, "上传失败");
            }
        }


        Map<String, Object> map = new HashMap();
        map.put("dataSource", dataSource);
        return ApiResponse.toJson(20000, map);
    }

    //从excel添加
    @PostMapping("/addfromexcel/{id}/{beginIndex}")
    public Map<String, Object> addFromExcel(MultipartFile file, @PathVariable("id") Integer id, @PathVariable("beginIndex") Integer beginIndex, HttpServletRequest req) {
        String originName = file.getOriginalFilename();
        if (!(originName.endsWith(".xlsx") || originName.endsWith(".csv"))) {
            return ApiResponse.toJsonDefault(60204, "文件格式错误");
        }

        DataSource dataSource;
        try {
            dataSource = fileUpload(file, req);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.toJsonDefault(60204, "上传失败");
        }

        if (originName.endsWith("xlsx") || originName.endsWith("csv")) {
            try {
                DataSource dataSource1 = dataSourceService.getOneById(id);
                int table = dataSourceService.addFromExcel(dataSource.getFilePath(), dataSource1.getTableName(), beginIndex);
                System.out.println("table: " + table + dataSource1.getTableName());
                System.out.println("dataSource: " + dataSource1);
            } catch (Exception e) {
                System.out.println(e.toString());
                return ApiResponse.toJsonDefault(60205, "上传失败");
            }
        }

        Map<String, Object> map = new HashMap();
        map.put("dataSource", dataSource);
        return ApiResponse.toJson(20000, map);
    }

    //文件上传
    private DataSource fileUpload(MultipartFile file, HttpServletRequest req) throws IOException {
        DataSource dataSource = new DataSource();

        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
        String format = sdf.format(new Date());
        String realPath = "/home/g2431/upload" + format;
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();

        String uuid = UUID.randomUUID().toString();

        String newName = uuid + "." + originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        System.out.println(folder + newName);
        String excelPath = folder + "/" + newName;
        file.transferTo(new File(folder, newName));

        if (originalFilename.endsWith("csv"))
            excelPath = CSV2Excel.csvToXLSX(excelPath);

        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/upload" + format + newName;

        dataSource.setFileUrl(url);
        dataSource.setFilePath(excelPath);
        dataSource.setShowName(originalFilename);
        dataSource.setTableName(uuid.replace("-", "").replaceAll("[0-9]", ""));
        dataSource.setType(originalFilename.substring(originalFilename.lastIndexOf('.') + 1).equals("xlsx") ? "excel" : "csv");
        System.out.println(dataSource);

        return dataSource;
    }

    //获取列表
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<DataSource> list = dataSourceService.getAll(1, 20);
        map.put("list", list);
        return ApiResponse.toJson(20000, map);
    }

    //删除数据源
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public Map<String, Object> delete(@PathVariable("id") Integer id) {
        int i = dataSourceService.deleteById(id);
        if (i > 0) {
            return ApiResponse.toJsonSuccess(20000, "删除成功");
        } else {
            return ApiResponse.toJsonDefault(62560, "删除失败");
        }
    }

    //修改
    @RequestMapping("/update")
    @ResponseBody
    public Map<String, Object> update(@RequestBody DataSource dataSource) {
        int i = dataSourceService.updateById(dataSource);
        if (i > 0) {
            return ApiResponse.toJsonSuccess(20000, "修改成功");
        } else {
            return ApiResponse.toJsonDefault(62560, "修改失败");
        }
    }

    //查询接口
    @RequestMapping("/query/{num}/{size}")
    @ResponseBody
    public Map<String, Object> query(@RequestBody DataSource dataSource, @PathVariable("num") Integer num, @PathVariable("size") Integer size) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<DataSource> list = dataSourceService.query(dataSource, num, size);
        map.put("items", list);
        map.put("totals", list.size());
        return ApiResponse.toJson(20000, map);
    }

    //查找一个
    @RequestMapping("/getOneById/{id}")
    @ResponseBody
    public Map<String, Object> getOneById(@PathVariable("id") Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        DataSource dataSource = dataSourceService.getOneById(id);

        dataSource.getTableName();

        return ApiResponse.toJson(20000, map);
    }

    //获取字典
    @RequestMapping("/getDictionary/{id}")
    @ResponseBody
    public Map<String, Object> getDictionary(@PathVariable("id") Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Dictionary> list = dataSourceService.getDictionary(id);
        map.put("items", list);
        return ApiResponse.toJson(20000, map);
    }

    //添加一个
    @PostMapping("/addonedata/{id}")
    public Map<String, Object> addOneData(@PathVariable("id") Integer id, @RequestBody Map<String, Object> map) {
        Map<String, Object> responseMap = new HashMap<>();

        int i = dataSourceService.addOneData(map, id);

        responseMap.put("nums", i);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
        }
        return ApiResponse.toJson(20000, responseMap);
    }

    @PostMapping("/addonedatabydic/{id}")
    public Map<String, Object> addOneDataByDic(@PathVariable("id") Integer id, @RequestBody List<Dictionary> list) {
        Map<String, Object> responseMap = new HashMap<>();
        Map<String, Object> map = new LinkedHashMap<>();

        for (Dictionary dictionary : list) {
            map.put(dictionary.getField(), dictionary.getDefault());
        }

        System.out.println(map);

        int i = dataSourceService.addOneData(map, id);

        responseMap.put("nums", i);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
        }
        return ApiResponse.toJson(20000, responseMap);
    }

    @GetMapping("/getTableDetails/{id}/{num}/{size}")
    public Map<String, Object> list(@PathVariable("id") Integer id, @PathVariable("num") Integer num, @PathVariable("size") Integer size) {
        Map<String, Object> responseMap = new HashMap<>();
        List<HashMap<String, Object>> hashMaps = dataSourceService.queryTableDetails(id, num, size);
        responseMap.put("items", hashMaps);
        responseMap.put("total", dataSourceService.getTableTotal(id));

        return ApiResponse.toJson(20000, responseMap);
    }

    //获取文件路径
    @GetMapping("/getfileurl/{id}")
    public Map<String, Object> getFileUrl(@PathVariable("id") Integer id) {
        Map<String, Object> responseMap = new HashMap<>();

        String fileUrl = dataSourceService.getFileUrl(id);
        responseMap.put("fileurl", fileUrl);

        return ApiResponse.toJson(20000, responseMap);
    }

    //修改字典
    @RequestMapping("/updateDic/{id}/{field}/{type}/{comment}")
    @ResponseBody
    public Map<String, Object> updateDic(@PathVariable("id") Integer id, @PathVariable("field") String field, @PathVariable("type") String type, @PathVariable("comment") String comment) {
        Map<String, Object> map = new HashMap<String, Object>();
        DataSource dataSource = dataSourceService.getOneById(id);

        String tablename = dataSource.getTableName();

        int result = dataSourceService.updateDic(tablename, field, type, comment);
        map.put("num", result);

        return ApiResponse.toJson(20000, map);
    }

    //修改字典
    @PostMapping(value = "/changeDic")
    @ResponseBody
    public Map<String, Object> changeDic(@RequestBody JSONObject dic) {
        Integer id = dic.getInteger("id");
        String oldfield = dic.getString("oldfield");
        String field = dic.getString("field");
        String oldtype = dic.getString("oldtype");
        String type = dic.getString("type");
        String comment = dic.getString("comment");

        System.out.println(id + "  " + oldfield);

        Map<String, Object> map = new HashMap<String, Object>();
        DataSource dataSource = dataSourceService.getOneById(id);
        String tablename = dataSource.getTableName();
        int result = dataSourceService.changeDic(tablename, oldfield, field, oldtype, type, comment);
        map.put("num", result);
        return ApiResponse.toJson(20000, map);
    }

    //修改字典
    @RequestMapping("/addField/{id}/{field}/{type}/{comment}")
    @ResponseBody
    public Map<String, Object> addField(@PathVariable("id") Integer id, @PathVariable("field") String field, @PathVariable("type") String type, @PathVariable("comment") String comment) {
        Map<String, Object> map = new HashMap<String, Object>();
        DataSource dataSource = dataSourceService.getOneById(id);

        String tablename = dataSource.getTableName();

        int result = dataSourceService.addField(tablename, field, type, comment);
        map.put("num", result);

        return ApiResponse.toJson(20000, map);
    }

    //修改字典
    @RequestMapping("/deleteField/{id}/{field}")
    @ResponseBody
    public Map<String, Object> deleteField(@PathVariable("id") Integer id, @PathVariable("field") String field) {
        Map<String, Object> map = new HashMap<String, Object>();
        DataSource dataSource = dataSourceService.getOneById(id);

        String tablename = dataSource.getTableName();

        int result = dataSourceService.deleteField(tablename, field);
        map.put("num", result);

        return ApiResponse.toJson(20000, map);
    }

    //获取树json数据
    @RequestMapping("/tableTree")
    @ResponseBody
    public Map<String, Object> tableTree() {
        Map<String, Object> map = new HashMap<String, Object>();

        List<DataSource> list = dataSourceService.getAll(1, 200);

        List<Map<String, Object>> tableList = new ArrayList();

        for (int i = 0; i < list.size(); i++) {
            List<Dictionary> dicList = dataSourceService.getDictionary(list.get(i).getId());
            Map<String, Object> cmap = new HashMap<>();
            cmap.put("name", list.get(i).getTableName());
            cmap.put("label", list.get(i).getTableName());

            List<Map<String, Object>> childrenList = new ArrayList<>();

            for (Dictionary dic : dicList) {
                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("name", list.get(i).getTableName() + "." + dic.getField());
                fieldMap.put("label", dic.getComment());
                childrenList.add(fieldMap);
            }

            cmap.put("children", childrenList);
            tableList.add(cmap);

        }

        map.put("data", tableList);
        return ApiResponse.toJson(20000, map);
    }

    //空值检测
    @RequestMapping("/getNull/{id}/{field}")
    @ResponseBody
    public Map<String, Object> nullSearch(@PathVariable("id") Integer id, @PathVariable("field") String field) {
        Map<String, Object> responseMap = new HashMap<>();

        DataSource dataSource = dataSourceService.getOneById(id);

        String tablename = dataSource.getTableName();

        List<HashMap<String, Object>> hashMaps = dataSourceService.nullSearch(tablename, field);
        responseMap.put("items", hashMaps);

        return ApiResponse.toJson(20000, responseMap);
    }

    //空值补全
    @RequestMapping("/nullUpdate/{id}/{field}/{value}/{rowid}")
    @ResponseBody
    public Map<String, Object> nullUpdate(@PathVariable("id") Integer id, @PathVariable("field") String field, @PathVariable("value") String value, @PathVariable("rowid") String rowid) {
        Map<String, Object> responseMap = new HashMap<>();

        DataSource dataSource = dataSourceService.getOneById(id);

        String tablename = dataSource.getTableName();

        int result = dataSourceService.nullUpdate(tablename, field,value,rowid);
        responseMap.put("num", result);

        return ApiResponse.toJson(20000, responseMap);
    }

    //jsonUpdate
    @RequestMapping("/jsonUpdate/{id}/{field}")
    @ResponseBody
    public Map<String, Object> jsonUpdate(@PathVariable("id") Integer id, @PathVariable("field") String field) {
        Map<String, Object> responseMap = new HashMap<>();

        DataSource dataSource = dataSourceService.getOneById(id);
        String tablename = dataSource.getTableName();

        List<HashMap<String, Object>> list = dataSourceService.queryTableDetails(id,1,500000);

        Integer sum = 0;
        for (HashMap<String, Object> map : list) {
            Object object = map.get(field);
            String jsonStr = object.toString();
            String newStr = dataSourceService.jsonToStr(jsonStr);
            int result = dataSourceService.nullUpdate(tablename,field,newStr,map.get("id").toString());
            sum += result;
        }


        responseMap.put("num", sum);

        return ApiResponse.toJson(20000, responseMap);
    }

    //按条件删除
    @RequestMapping("/deleteByNum/{id}/{field}/{value}/{type}")
    @ResponseBody
    public Map<String, Object> deleteByNum(@PathVariable("id") Integer id,@PathVariable("field") String field,@PathVariable("value") String value,@PathVariable("type") String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        DataSource dataSource = dataSourceService.getOneById(id);
        String tablename = dataSource.getTableName();

        Integer result = dataSourceService.deleteByNum(tablename, field, value, type);

        map.put("num",result);

        return ApiResponse.toJson(20000, map);
    }


    //删除重复数据
    @RequestMapping("/deleteRepe/{id}")
    public Map<String, Object> deleteRepe(@PathVariable("id") Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        DataSource dataSource = dataSourceService.getOneById(id);
        String tablename = dataSource.getTableName();

        Integer result = dataSourceService.deleteRepe(tablename);

        map.put("num",result);

        return ApiResponse.toJson(20000, map);
    }

}
