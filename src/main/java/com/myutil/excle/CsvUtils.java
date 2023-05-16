package com.myutil.excle;

//import com.dten.device.operator.infrastructure.command.CsvFileInfo;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class CsvUtils {

    /**
     * parse csv file
     *
     * @param file csv file
     * @return 数组
     */
    public static List<String[]> getCsvDataMethod2(MultipartFile file) {

        List<String[]> list = new ArrayList<String[]>();
        int i = 0;
        try {
            CSVReader csvReader = new CSVReaderBuilder(
                    new BufferedReader(
                            new InputStreamReader(file.getInputStream(), "utf-8"))).build();
            Iterator<String[]> iterator = csvReader.iterator();
            while (iterator.hasNext()) {
                String[] next = iterator.next();
                //remove the file header
                if (i >= 1) {
                    list.add(next);
                }
                i++;
            }
            return list;
        } catch (Exception e) {
            log.error("parse csv file error!",e);
            return list;
        }
    }


    /**
     * parse csv file
     *
     * @param file  csv file
     * @param clazz 类
     * @param <T>   泛型
     * @return 泛型bean集合
     */
    public static <T> List<T> getCsvDataMethod3(MultipartFile file, Class<T> clazz) {
        InputStreamReader in = null;
        CsvToBean<T> csvToBean = null;
        try {
            in = new InputStreamReader(file.getInputStream(), "utf-8");
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);
            csvToBean = new CsvToBeanBuilder<T>(in).withMappingStrategy(strategy).build();
        } catch (Exception e) {
            log.error("parse csv file error!",e);
            return null;
        }
        return csvToBean.parse();
    }


    /**
     * parse csv File
     *
     * @param file
     * @return
     */
    public static List<String> getCsvDataMethod1(MultipartFile file) {
        ArrayList<String> csvFileList = new ArrayList<>();

        InputStreamReader in = null;
        String s = null;
        try {
            in = new InputStreamReader(file.getInputStream(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(in);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(",");
                //CsvFileInfo csvFile = new CsvFileInfo();
                //csvFile.setDtenId(splitResult(split[0]));
               // csvFile.setRootCaUrl(splitResult(split[1]));
                //csvFileList.add(csvFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFileList;
    }

    private static String splitResult(String once) {
        String result = "";
        for (int i = 0; i < once.length(); i++) {
            if (once.charAt(i) != '"') {
                result += once.charAt(i);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String filePath = "F:\\chrome_download\\Nike Cer-2023-04-16.csv";
        //MultipartFile file = new File(filePath);
        try{
            File file = new File(filePath);
            InputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
            List<CsvFileInfo> datas = CsvUtils.getCsvDataMethod3(multipartFile,CsvFileInfo.class);
            for (CsvFileInfo csvFileInfo: datas){
                System.out.println(csvFileInfo.getDtenId()+","+csvFileInfo.getIdentity()+" "+csvFileInfo.getRootCaUrl()+" "+csvFileInfo.getPrvKeyPwd());
            }

        }catch (Exception ex){

        }

    }
}
