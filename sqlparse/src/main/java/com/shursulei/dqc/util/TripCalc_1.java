package com.shursulei.dqc.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hanfeng
 * @version 1.0
 * @date 2021/12/30 14:32
 */
public class TripCalc_1 {
    public static void main(String[] args) {
        String filePath = "D:\\desktop\\document\\乐刻\\埋点平台\\202112出差源数据结果.xlsx";
        String filePath2 = "D:\\desktop\\document\\乐刻\\埋点平台\\202112出差源数据最终结果.xlsx";
        String initfilepath = "D:\\desktop\\document\\乐刻\\埋点平台\\202112出差源数据.xlsx";
        String deleteFilePath = "D:\\desktop\\document\\乐刻\\埋点平台\\出差餐补剔除12月.xlsx";
        Map<String,Set<String>> initData=getInitData(initfilepath);
        Map<String,Set<String>> deleteData=getdeleteData(deleteFilePath);
        Map<String,Double> LastData=new HashMap<String,Double>();
        for(Map.Entry<String,Set<String>> init:initData.entrySet()){
            Set<String> initValue = init.getValue() == null?new HashSet<>():init.getValue();
            if(deleteData.get(init.getKey())!=null){
                Set<String> deleteValue = deleteData.get(init.getKey());
                Set<String> jiaojieSet=new HashSet<>();
                jiaojieSet.clear();
                jiaojieSet.addAll(deleteValue);
                jiaojieSet.retainAll(initValue);
                LastData.put(init.getKey(),(double)jiaojieSet.size()/2);
            }else {
                LastData.put(init.getKey(),0.0);
            }

        }
        System.out.println("test22");
        System.out.println(LastData);


        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
        } catch (Exception e) {
            System.out.println("读取Excel失败，请检查Excel路径是否正确");
            return;
        }
        List<DateResult> dateResultList = new LinkedList<>();
        Sheet resultSheet = workbook.getSheet("计算结果");
        int resultRows = resultSheet.getLastRowNum() + 1;
        Row resultData = resultSheet.getRow(0);
        for(int row=1;row<resultRows;row++){
            resultData=resultSheet.getRow(row);
            DateResult dateResult=new DateResult();
            dateResult.setUserId(resultData.getCell(0).getStringCellValue());
            dateResult.setName(resultData.getCell(1).getStringCellValue());
            dateResult.setGroupName(resultData.getCell(2).getStringCellValue());
            dateResult.setGroupCenter(resultData.getCell(3).getStringCellValue());
            dateResult.setInvoiceTitle(resultData.getCell(4).getStringCellValue());
            dateResult.setBeginTime(resultData.getCell(5).getStringCellValue());
            dateResult.setEndTime(resultData.getCell(6).getStringCellValue());
            dateResult.setAliTime(resultData.getCell(7).getStringCellValue());
            dateResult.setFactTime(resultData.getCell(8).getStringCellValue());
            dateResult.setDubTime(resultData.getCell(9).getStringCellValue());
            dateResultList.add(dateResult);
        }
        List<LastDateResult> lastDateResultList=new ArrayList<>();
        for(DateResult dateResult:dateResultList){
            LastDateResult lastDateResult= new LastDateResult();
            lastDateResult.setUserId(dateResult.getUserId());
            lastDateResult.setName(dateResult.getName());
            lastDateResult.setGroupName(dateResult.getGroupName());
            lastDateResult.setGroupCenter(dateResult.getGroupCenter());
            lastDateResult.setInvoiceTitle(dateResult.getInvoiceTitle());
            lastDateResult.setBeginTime(dateResult.getBeginTime());
            lastDateResult.setEndTime(dateResult.getEndTime());
            lastDateResult.setAliTime(dateResult.getAliTime());
            lastDateResult.setFactTime(dateResult.getFactTime());
            lastDateResult.setDubTime(dateResult.getDubTime());
            if(LastData.containsKey(dateResult.getUserId())){
                lastDateResult.setLastFactTime(String.valueOf(Double.parseDouble(dateResult.getFactTime())-LastData.get(dateResult.getUserId())));
            }else{
                lastDateResult.setLastFactTime(dateResult.getFactTime());
            }
            lastDateResultList.add(lastDateResult);
        }
        //list写入新excel
        // 结果输出
        Sheet lastSheet = workbook.createSheet("最后计算结果");
        Row lastrowData = lastSheet.createRow(0);
        lastrowData.createCell(0).setCellValue("发起人工号");
        List<String> Lastcolumns = Arrays.asList("发起人姓名", "发起人部门", "成本中心", "发票抬头","开始时间","结束时间","阿里时长"
                ,"有效时长","最后有效时长","剔除时间","重叠时间");

        for (int col = 0; col < Lastcolumns.size(); col++) {
            lastrowData.createCell(col + 1).setCellValue(Lastcolumns.get(col));
        }
        for (int i=1;i<lastDateResultList.size();i++){
            Row nrow= (XSSFRow) lastSheet.createRow(i);
//            for (int j=0;j<Lastcolumns.size();j++)
//            {
//                nrow.createCell(j).setCellValue(lastDateResultList.get(i).getName());
//            }
            Cell nell=nrow.createCell(0);
            nell.setCellValue(lastDateResultList.get(i).getUserId());
            nell=nrow.createCell(1);
            nell.setCellValue(lastDateResultList.get(i).getName());
            nell=nrow.createCell(2);
            nell.setCellValue(lastDateResultList.get(i).getGroupName());
            nell=nrow.createCell(3);
            nell.setCellValue(lastDateResultList.get(i).getGroupCenter());
            nell=nrow.createCell(4);
            nell.setCellValue(lastDateResultList.get(i).getInvoiceTitle());
            nell=nrow.createCell(5);
            nell.setCellValue(lastDateResultList.get(i).getBeginTime());
            nell=nrow.createCell(6);
            nell.setCellValue(lastDateResultList.get(i).getEndTime());
            nell=nrow.createCell(7);
            nell.setCellValue(lastDateResultList.get(i).getAliTime());
            nell=nrow.createCell(8);
            nell.setCellValue(lastDateResultList.get(i).getFactTime());
            nell=nrow.createCell(9);
            nell.setCellValue(lastDateResultList.get(i).getLastFactTime());
            nell=nrow.createCell(10);
            nell.setCellValue(lastDateResultList.get(i).getDeleteDays());
            nell=nrow.createCell(11);
            nell.setCellValue(lastDateResultList.get(i).getDubTime());
        }
        try {
            workbook.write(new FileOutputStream(filePath2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static Map<String,Set<String>> getdeleteData(String filePath){

        List<String> columns = Arrays.asList("发起人姓名", "发起人部门", "成本中心", "发票抬头");
        // 读取Excel
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
        } catch (Exception e) {
            System.out.println("读取Excel失败，请检查Excel路径是否正确");
            return null;
        }
        Sheet deleteSheet = workbook.getSheetAt(0);
        int rows = deleteSheet.getLastRowNum() + 1;
        Row tmp = deleteSheet.getRow(0);
        if (tmp == null) {
            System.out.println("读取工作表为空，请复核");
            return null;
        }
        Row deleterowData = deleteSheet.getRow(0);
        // 数据扫描
        Map<String,Set<String>> userDeleteMap=new HashMap<>();
        for (int row = 1; row < rows; row++) {
            deleterowData = deleteSheet.getRow(row);
            String userId=deleterowData.getCell(0).getStringCellValue();
            Set<String> dateDeleteSet=Arrays.stream(deleterowData.getCell(2).getStringCellValue().replace (" ", "").split(",")).collect(Collectors.toSet());
            userDeleteMap.put(userId,dateDeleteSet);
        }
        System.out.println(userDeleteMap);
        return userDeleteMap;
    }
    private static Map<String,Set<String>> getInitData(String filePath){

        List<String> columns = Arrays.asList("发起人姓名", "发起人部门", "成本中心", "发票抬头","开始时间","结束时间");

        // 读取Excel
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
        } catch (Exception e) {
            System.out.println("读取Excel失败，请检查Excel路径是否正确");
            return null;
        }
        // 读取第一张表单
        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getLastRowNum() + 1;
        Row tmp = sheet.getRow(0);
        if (tmp == null) {
            System.out.println("读取工作表为空，请复核");
            return null;
        }
        int cols = tmp.getPhysicalNumberOfCells();
        // 读取标题栏
        List<Integer> columsIndex = new LinkedList<>();
        Integer begIndex = 0, endIndex = 0, idIndex = 0, statusIndex = 0, resultIndex = 0, aliDayIndex = 0;
        Row rowData = sheet.getRow(0);
        for (String colName : columns) {
            for (int col = 0; col < cols; col++) {
                if (colName.equals(rowData.getCell(col).getStringCellValue())) {
                    columsIndex.add(col);
                }
            }
        }
        for (int col = 0; col < cols; col++) {
            if ("发起人工号".equals(rowData.getCell(col).getStringCellValue())) {
                idIndex = col;
            }
            if ("开始时间".equals(rowData.getCell(col).getStringCellValue())) {
                begIndex = col;
            }
            if ("结束时间".equals(rowData.getCell(col).getStringCellValue())) {
                endIndex = col;
            }
            if ("审批状态".equals(rowData.getCell(col).getStringCellValue())) {
                statusIndex = col;
            }
            if ("审批结果".equals(rowData.getCell(col).getStringCellValue())) {
                resultIndex = col;
            }
            if ("时长(天)".equals(rowData.getCell(col).getStringCellValue())) {
                aliDayIndex = col;
            }
        }
        // 数据扫描
        Map<String, List<List<String>>> timeMap = new HashMap<>();
        //用户时间key_value
        Map<String,Set<String>> userMap=new HashMap<>();
        for (int row = 1; row < rows; row++) {
            rowData = sheet.getRow(row);
            String status = rowData.getCell(statusIndex).getStringCellValue();
            String result = rowData.getCell(resultIndex).getStringCellValue();
            if ("同意".equals(result) && ("完成".equals(status) || "已修改".equals(status))) {
                String id = rowData.getCell(idIndex).getStringCellValue();
                //开始时间
                Calendar begDate = getDate(rowData.getCell(begIndex).getStringCellValue());
                String begNoon = getNoon(rowData.getCell(begIndex).getStringCellValue());
                //结束时间
                Calendar endDate = getDate(rowData.getCell(endIndex).getStringCellValue());
                String endNoon = getNoon(rowData.getCell(endIndex).getStringCellValue());
                // 开始结束日期相等
                if (endDate.equals(begDate)) {
                    if (begNoon.equals(endNoon)) {
                        addToSet(begDate, begNoon, id, userMap);
                    } else if (begNoon.equals("下午") && endNoon.equals("上午")) {
                        System.out.println("时间错误：" );
                    } else {
                        addToSet(begDate, begNoon, id, userMap);
                        addToSet(endDate, endNoon, id, userMap);
                    }
                    // 开始结束日期不同
                } else if (endDate.after(begDate)) {
                    // 处理开始日期
                    addToSet(begDate, begNoon, id, userMap);
                    if (begNoon.equals("上午")) {
                        addToSet(begDate, "下午", id, userMap);
                    }
                    // 处理中间日期
                    begDate.add(Calendar.DATE, 1);
                    while (endDate.after(begDate)) {
                        addToSet(begDate, "上午", id, userMap);
                        addToSet(begDate, "下午", id, userMap);
                        begDate.add(Calendar.DATE, 1);
                    }
                    // 处理结束日期
                    addToSet(endDate, endNoon, id, userMap);
                    if (endNoon.equals("下午")) {
                        addToSet(endDate, "上午", id, userMap);
                    }
                } else {
                    System.out.println("时间错误" );
                }
            }
        }
        System.out.println(userMap);
        return userMap;
    }
    private static Calendar getDate(String time) {
        String str = time.split(" ")[0];
        Calendar cal = Calendar.getInstance();
        LocalDate date = LocalDate.parse(str);
        cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    private static String getNoon(String time) {
        String noon = time.split(" ")[1];
        if ((!noon.equals("上午")) && !noon.equals("下午")) {
            if (noon.equals("AM")) {
                noon = "上午";
            } else if (noon.equals("PM")) {
                noon = "下午";
            } else {
                System.out.println("日期解析错误：" + time);
            }

        }
        return noon;
    }
    private static void addToSet(Calendar date, String noon, String userId,Map<String,Set<String>> mapSet){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String str = df.format(date.getTime()) + noon;
        if(mapSet.containsKey(userId)){
            Set<String> keyValue=mapSet.get(userId);
            keyValue.add(str);
            mapSet.put(userId,keyValue);
        }else {
            Set<String> userSet=new HashSet<>();
            userSet.add(str);
            mapSet.put(userId,userSet);
        }
    }
}
