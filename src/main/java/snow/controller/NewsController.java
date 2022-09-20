package snow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import snow.entity.DataSource;
import snow.entity.Keywords;
import snow.entity.News;
import snow.entity.User;
import snow.service.NewsService;
import snow.service.UserService;
import snow.utils.ApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NewsController {
    @Autowired
    private NewsService newsService;

    //查询接口
    @RequestMapping("/querynews/{tablename}/{num}/{size}")
    @ResponseBody
    public Map<String, Object> query( @PathVariable("tablename") String tablename,@PathVariable("num") Integer num, @PathVariable("size") Integer size) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<HashMap<String,Object>> list = newsService.query(tablename, num, size);
        map.put("items", list);
        map.put("totals", newsService.getTotals(tablename));
        return ApiResponse.toJson(20000, map);
    }

    //查询接口
    @RequestMapping("/keywords/{tablename}")
    @ResponseBody
    public Map<String, Object> keywords( @PathVariable("tablename") String tablename) {
        Map<String, Object> map = new HashMap<String, Object>();


        List<Keywords> list = newsService.getKeywords(tablename);
        map.put("items", list);
        map.put("totals", list.size());
        return ApiResponse.toJson(20000, map);
    }




}
