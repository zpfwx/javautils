package com.myutil.excle;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class ExcelUtils {
    public static Workbook readExcel(InputStream in, String suffix){
        Workbook wb = null;
        try {
            if ("xls".equalsIgnoreCase(suffix)) {
                return wb = new HSSFWorkbook(in);
            } else if ("xlsx".equalsIgnoreCase(suffix)) {
                return wb = new XSSFWorkbook(in);
            } else {
                return wb = null;
            }
        } catch (IOException e) {
            log.error("readExcel error!",e);
        }
        return wb;
    }

    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }

    public static List<Map<String,String>> getRowValues(InputStream in, String suffix, String columns[]) {
        Workbook wb = readExcel(in,suffix);
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        if(wb == null){
          log.error("getRowValues error wb is null");
          return list;
        }

        Sheet sheet  = wb.getSheetAt(0);
        if(sheet == null){
            log.error("getSheetAt error sheet is null");
            return list;
        }

        int rownum = sheet.getPhysicalNumberOfRows();
        Row row = sheet.getRow(0);
        if(row == null){
            log.error("getRow error, row is null");
            return list;
        }

        int colnum = row.getPhysicalNumberOfCells();
        for (int i = 1; i<rownum; i++) {
            Map<String,String> map = new LinkedHashMap<String,String>();
            row = sheet.getRow(i);
            if (row == null || row.getCell(0) == null) {
                break;
            }

            for (int j=0;j<colnum;j++){
                if (row.getCell(j) == null || row.getCell(j).getCellType() == Cell.CELL_TYPE_BLANK) {
                    break;
                }
                row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                String cellData = (String) getCellFormatValue(row.getCell(j));
                map.put(columns[j], cellData);
            }

            list.add(map);
        }

        return list;
    }

    public static void main(String[] args) {

        String filePath = "F:\\chrome_download\\Nike Cer-2023-04-16.xlsx";
        String columns[] = {"dtenId","caUrl"};
        //String columns[] = {ConstUtils.CER_HEADER_DTEN_ID,ConstUtils.CER_HEADER_IDENTITY, ConstUtils.CER_HEADER_ROOT_CA_URL,ConstUtils.CER_HEADER_PRV_PWD, ConstUtils.CER_HEADER_USER_CA_URL, ConstUtils.CER_HEADER_USER_PRKEY_URL};

        try {
            FileInputStream is = new FileInputStream(filePath);
            String suffix = filePath.substring(filePath.lastIndexOf(".")+1);
            List<Map<String,String>> rows = getRowValues(is,suffix,columns);
            //遍历解析出来的list
            for (Map<String,String> map : rows) {
                for (Map.Entry<String,String> entry : map.entrySet()) {
                    System.out.print(entry.getKey()+":"+entry.getValue()+",");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
