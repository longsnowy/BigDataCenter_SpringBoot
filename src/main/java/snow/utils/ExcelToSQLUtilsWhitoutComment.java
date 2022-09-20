package snow.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExcelToSQLUtilsWhitoutComment {

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

        List<Cell> cellList = rowList.get(0);


        String sql = "create table " + "rjjdnb(";
        for (int i = 0; i < cellList.size() - 1; i++) {
            Cell cell = cellList.get(i);

            String tmp = " text ";;
            tmp += (i != cellList.size() - 2) ? "," : ");";
            sql += cell.getStringCellValue() + tmp;
        }
        System.out.println(sql);

        return sql;
    }
}
