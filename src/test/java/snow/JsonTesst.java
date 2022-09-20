package snow;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import snow.utils.CSV2Excel;
import snow.utils.ExcelToSQLUtilsWhitoutComment;
import snow.utils.SQLprovider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTesst {
    @Test
    public void test1() throws IOException {

        String str = "[{\"id\": 12, \"name\": \"Adventure\"}, {\"id\": 14, \"name\": \"Fantasy\"}, {\"id\": 10751, \"name\": \"Family\"}]";
        List<Object> list = JSON.parseArray(str);
        List<String> nameList = new ArrayList<>();
        for (Object object : list) {
            Map<String, Object> ret = (Map<String, Object>) object;//取出list里面的值转为map
            nameList.add(ret.get("name").toString());
        }
        StringBuilder strTemp = new StringBuilder();
        for (int i = 0; i < nameList.size() - 1; i++) {
            strTemp.append(nameList.get(i).toString()).append(",");
        }
        strTemp.append(nameList.get(list.size() - 1).toString());

    }

}
