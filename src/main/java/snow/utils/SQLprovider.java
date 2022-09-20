package snow.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.ibatis.jdbc.SQL;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import snow.entity.Dictionary;
import snow.mapper.DataSourceMapper;
import snow.service.DataSourceService;

public class SQLprovider {

    @Autowired
    private DataSourceService dataSourceService;

    public String ExceltoSQLCommon(String path, Integer nameIndex, Integer unitIndex, Integer meansIndex, String tablename) throws IOException {
        FileInputStream inputStream = new FileInputStream(path);
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
//        for (List<Cell> cells : rowList) {
//            System.out.println(cells);
//        }

        List<Cell> cellList = rowList.get(nameIndex - 1);


        List<Cell> unitList = null;

        List<Cell> meanList = null;

        Set<String> intSet = new HashSet<>();
        intSet.add("个");
        intSet.add("人");
        intSet.add("件");
        intSet.add("岁");

        Set<String> doubleSet = new HashSet<>();
        doubleSet.add("千元");
        doubleSet.add("平方米");

        String sql = "create table bdctables." + tablename + "(";
        for (int i = 0; i < cellList.size() - 1; i++) {
            Cell cell = cellList.get(i);

            String tmp = null;

//            if (unitIndex > 0) {
//                unitList = rowList.get(unitIndex - 1);
//                String unit = unitList.get(i).toString();
//                if (intSet.contains(unit)) {
//                    tmp = " int ";
//                } else if (doubleSet.contains(unit)) {
//                    tmp = " double ";
//                } else {
//                    tmp = " text ";
//                }
//            } else {
            tmp = " text ";
//            }

            if (meansIndex > 0) {
                meanList = rowList.get(meansIndex - 1);
                String mean = meanList.get(i).toString();
                tmp += "COMMENT '" + mean + "'";
            } else {
                meanList = rowList.get(nameIndex - 1);
                String mean = meanList.get(i).toString();
                tmp += "COMMENT '" + mean + "'";
            }
            tmp += (i != cellList.size() - 2) ? "," : ");";
            sql += cell.getStringCellValue() + tmp;
        }
        System.out.println(sql);
        return sql;
    }

    //将Excel转换为建表的SQL语句
    public String ExceltoSQL(String path, String tablename) throws IOException {
        FileInputStream inputStream = new FileInputStream(path);
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

        String sql = "create table bdctables." + tablename + "(";
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
        return sql;
    }

    //将Excel转换为插入的SQL语句
    public String ExceltoInsertSQL(String path, String tablename, Integer beginIndex) throws IOException {
        FileInputStream inputStream = new FileInputStream(path);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<List<Cell>> rowList = new ArrayList<>();
        for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i);
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                cellList.add(cell);
            }
            rowList.add(cellList);
        }
        for (List<Cell> cells : rowList) {
            System.out.println(cells);
        }

        //insert into fedbcceabaeffdd values('10001','dafw','男','18'),('10001','dafw','男','18');

        String sql = "insert into bdctables." + tablename + " values";




        for (int i = beginIndex - 1; i < rowList.size() - 1; i++) {
            List<Cell> cells = rowList.get(i);
            sql += "('";
            for (int j = 0; j < cells.size() - 1; j++) {
                Cell cell = cells.get(j);
                String tmp = cell.toString();
                if (tmp.endsWith(".0")) {
                    tmp = tmp.substring(0, tmp.length() - 2);
                }
                sql += tmp + "'," + "'";
            }
            String tmp = cells.get(cells.size() - 1).toString();
            if (tmp.endsWith(".0")) {
                tmp = tmp.substring(0, tmp.length() - 2);
            }
            sql += tmp + "'),";
        }

        List<Cell> cells = rowList.get(rowList.size() - 1);
        sql += "('";
        for (int j = 0; j < cells.size() - 1; j++) {
            Cell cell = cells.get(j);
            String tmp = cell.toString();
            if (tmp.endsWith(".0")) {
                tmp = tmp.substring(0, tmp.length() - 2);
            }
            sql += tmp + "'," + "'";
        }
        String tmp = cells.get(cells.size() - 1).toString();
        if (tmp.endsWith(".0")) {
            tmp = tmp.substring(0, tmp.length() - 2);
        }
        sql += tmp + "')";

        System.out.println(sql);

        return sql;
    }

    public String getAddOneSql(Map<String, Object> map, String tablename) throws IOException {

        StringBuilder sqlTmp = new StringBuilder();
        sqlTmp.append("insert into bdctables." + tablename + " values('");

        System.out.println(tablename);

        System.out.println(map);

//        List<Dictionary> dicList = dataSourceService.getDicByName("fedbcceabaeffdd");

//        for (int i = 0; i < dicList.size() - 1; i++) {
//            sqlTmp.append(map.get(dicList.get(i).getField()) + "','");
//        }
//        sqlTmp.append(map.get(dicList.get(dicList.size() - 1).getField()) + "');");

//        for (Map.Entry<String,Object> entry : map.entrySet()) {
//            sqlTmp.append(map.get(entry.getValue() + "','"));
//
//        }

        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            if (entries.hasNext()) {
                sqlTmp.append(entry.getValue() + "','");
            } else {
                sqlTmp.append(entry.getValue() + "');");
            }
        }

        System.out.println(sqlTmp);

        return sqlTmp.toString();
    }

    public String changeDic(String tablename, String oldfield, String field, String oldtype, String type, String comment) {
        String sql = "alter table bdctables." + tablename + " change " + oldfield + " " + field;
        if (type != null) {
            sql += " " + type;
        } else {
            sql += " " + oldtype;
        }
        if (comment != null) {
            sql += " COMMENT '" + comment + "';";
        }
        return sql;
    }

    public String addField(String tablename, String field, String type, String comment) {
        String sql = "alter table bdctables." + tablename + " add column " + field + " " + type + " comment '" + comment + "';";
        return sql;
    }

}
