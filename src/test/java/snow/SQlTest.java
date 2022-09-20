package snow;

import org.junit.jupiter.api.Test;
import snow.utils.CSV2Excel;
import snow.utils.ExcelToSQLUtilsWhitoutComment;
import snow.utils.SQLprovider;

import java.io.IOException;

public class SQlTest {
    @Test
    public void test1() throws IOException {
        CSV2Excel.csvToXLSX("/home/g2431/Documents/data/tmdb_5000_credits.csv");
        String resultSQL = ExcelToSQLUtilsWhitoutComment.ExceltoSQL("/home/g2431/Documents/data/tmdb_5000_movies.xlsx");
        System.out.println(resultSQL);
    }

    @Test
    public void test2() throws IOException {
//        CSV2Excel.csvToXLSX("/home/g2431/Documents/data/tmdb_5000_credits.csv");
        String resultSQL = new SQLprovider().ExceltoSQLCommon("/home/g2431/Documents/data/tmdb_5000_movies.xlsx",0,99,99,"testtabke");
        String resultSQL2 = new SQLprovider().ExceltoSQLCommon("/home/g2431/Documents/student.xlsx",2,1,0,"testtabke222");
    }

    @Test
    public void test3(){
        String sql = changeDic("dcccdcad","sex","xingbie","varchar(64)","text","性别");
        System.out.println(sql);
    }

    public String changeDic(String tablename, String oldfield, String field, String oldtype, String type, String comment) {
        String sql = "alter table bdctables." + tablename + " change " + oldfield + " " + field;
        if (type != null){
            sql += " " + type;
        }else {
            sql += " " + oldtype;
        }
        if (comment != null){
            sql += " COMMENT '" + comment + "';";
        }
        return sql;
    }
}
