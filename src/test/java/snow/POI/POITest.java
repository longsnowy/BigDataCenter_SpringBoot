package snow.POI;

import ch.qos.logback.core.util.TimeUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.DateUtil;
import snow.utils.ExcelToSQLUtils;

public class POITest {

    @Test
    public void test1() throws IOException {
        FileInputStream inputStream = new FileInputStream("D://2018年软件基地年报.xlsx");
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<List<Cell>> rowList = new ArrayList<>();
        for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i);
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < row.getLastCellNum() + 1; j++) {
                Cell cell = row.getCell(j);
                cellList.add(cell);
            }
            rowList.add(cellList);
        }
        for (List<Cell> cells : rowList) {
            System.out.println(cells);
        }


        String sql = "create table " + "rjjdnb(";

        List<Cell> cellList = rowList.get(2);
        List<Cell> cellDemoList = rowList.get(3);
        for (int i = 0; i < cellList.size() - 1; i++) {

            Cell cell = cellList.get(i);

            int cellType = cellDemoList.get(i).getCellType();

            Object cellValue = null;
            String tmp = null;
            switch (cellType) {
                case Cell.CELL_TYPE_STRING: //字符串类型
                    cellValue = cell.getStringCellValue();
                    cellValue = StringUtils.isEmpty(cellValue) ? "" : cellValue;
                    tmp = i != cellList.size() - 1 ? " varchar(64)," : " varchar(64));";
                    sql += cell.getStringCellValue() + tmp;
                    break;
                case Cell.CELL_TYPE_BOOLEAN:  //布尔类型
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    tmp = i != cellList.size() - 1 ? " varchar(64)," : " varchar(64));";
                    sql += cell.getStringCellValue() + tmp;
                    break;
                case Cell.CELL_TYPE_NUMERIC: //数值类型
//                    System.out.println(cell);
//                    if (DateUtil.isCellDateFormatted(cell)) {  //判断日期类型
//                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        Date dt = DateUtil.getJavaDate(cell.getNumericCellValue());// 获取成DATE类型
//                        cellValue = dateformat.format(dt);
//                        tmp = i!=cellList.size()-1? " varchar(64),":" varchar(64));";
//                        sql += cell.getStringCellValue() + tmp;
//
//                    } else {  //否
//                    cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
                    cellValue = cell.toString();
                    tmp = i != cellList.size() - 1 ? " double," : " double);";
                    sql += cell.getStringCellValue() + tmp;
//                    }
                    break;
                default: //其它类型，取空串吧
                    cellValue = cell.toString();
                    tmp = i != cellList.size() - 1 ? " varchar(64)," : " varchar(64));";
                    sql += cell.getStringCellValue() + tmp;
                    break;
            }
            System.out.println(cellType);
        }
        System.out.println(sql);
    }

    @Test
    public void test2() throws IOException {
        FileInputStream inputStream = new FileInputStream("D://2018年软件基地年报2基地内企业情况.xlsx");
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<List<Cell>> rowList = new ArrayList<>();
        for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i);
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < row.getLastCellNum() + 1; j++) {
                Cell cell = row.getCell(j);
                cellList.add(cell);
            }
            rowList.add(cellList);
        }
        for (List<Cell> cells : rowList) {
            System.out.println(cells);
        }

        List<Cell> cellList = rowList.get(2);
        List<Cell> cellDemoList = rowList.get(3);
        List<Cell> unitList = rowList.get(1);
        List<Cell> meanList = rowList.get(0);

        Set<String> intSet = new HashSet<>();
        intSet.add("个");
        intSet.add("人");
        intSet.add("件");

        Set<String> doubleSet = new HashSet<>();
        doubleSet.add("千元");
        doubleSet.add("平方米");

        String sql = "create table " + "rjjdnb(";
        for (int i = 0; i < cellList.size() - 1; i++) {
            Cell cell = cellList.get(i);
            String unit = unitList.get(i).toString();
            String mean = meanList.get(i).toString();

            String tmp = null;

            if (intSet.contains(unit)) {
                tmp = " int ";
            } else if (doubleSet.contains(unit)) {
                tmp = " double ";
            } else {
                tmp = " varchar(64) ";
            }
            tmp += "COMMENT '" + mean + "'";
            tmp += (i != cellList.size() - 2) ? "," : ");";
            sql += cell.getStringCellValue() + tmp;
        }
        System.out.println(sql);
    }

    @Test
    void test3() throws IOException {
        String sql = ExcelToSQLUtils.ExceltoSQL("D://2018年软件基地年报2基地内企业情况.xlsx");
        System.out.println("!!!!!!!!!" + sql);
        ApplicationContext ctx = new ClassPathXmlApplicationContext("application.properties");
        //获取IoC容器中JdbcTemplate实例
        JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");

        jdbcTemplate.execute(sql);

        System.out.println(sql);
    }

    //判断整数（int）
    private boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    //判断浮点数（double和float）
    private boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }


}
