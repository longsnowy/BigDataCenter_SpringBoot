package snow.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExcelToSQLUtils {

    //根据Excel生成SQL
    public static String ExceltoSQL(String path) throws IOException {
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
                if (unit.length() < 128)
                    tmp = " varchar(128) ";
                else
                    tmp = " text ";
            }
            tmp += "COMMENT '" + mean + "'";
            tmp += (i != cellList.size() - 2) ? "," : ");";
            sql += cell.getStringCellValue() + tmp;
        }
        System.out.println(sql);

        return sql;
    }

    //根据Excel生成SQL
    public static String ExceltoSQL(String path,int nameIndex,int unitIndex, int meansIndex) throws IOException {
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

        List<Cell> cellList = rowList.get(nameIndex);
//        List<Cell> cellDemoList = rowList.get(3);
        List<Cell> unitList = rowList.get(unitIndex);
        List<Cell> meanList = rowList.get(meansIndex);

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
                if (unit.length() < 128)
                    tmp = " varchar(128) ";
                else
                    tmp = " text ";
            }
            tmp += "COMMENT '" + mean + "'";
            tmp += (i != cellList.size() - 2) ? "," : ");";
            sql += cell.getStringCellValue() + tmp;
        }
        System.out.println(sql);

        return sql;
    }
}
