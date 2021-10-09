package com.shursulei.dqc.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;


/**
 * 审批状态为已修改和完成的计算在内，已撤销的不算
 * 审批结果为同意的计算在内，其它不算
 * 按工号进行聚合统计，如果工号重复或工号为空可能出错
 * 如果列名变化，或开始时间/结束时间的字符串格式变化，有可能出错
 */

public class TripCalc {

    public static void main(String[] args) {
        // 设置读入路径
        String filePath = "/Users/apple/Downloads/202109出差源数据.xlsx";
        String filePath2 = "/Users/apple/Downloads/202109出差源数据结果.xlsx";
        List<String> columns = Arrays.asList("发起人姓名", "发起人部门", "成本中心", "发票抬头");

        // 读取Excel
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
        } catch (Exception e) {
            System.out.println("读取Excel失败，请检查Excel路径是否正确");
            return;
        }

        // 读取第一张表单
        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getLastRowNum() + 1;
        Row tmp = sheet.getRow(0);
        if (tmp == null) {
            System.out.println("读取工作表为空，请复核");
            return;
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
        List<List<String>> infoList = new LinkedList<>();
        Map<String, List<List<String>>> timeMap = new HashMap<>();
        Map<String, Double> aliDayMap = new HashMap<>();
        for (int row = 1; row < rows; row++) {
            rowData = sheet.getRow(row);
            String status = rowData.getCell(statusIndex).getStringCellValue();
            String result = rowData.getCell(resultIndex).getStringCellValue();
            if ("同意".equals(result) && ("完成".equals(status) || "已修改".equals(status))) {
                String id = rowData.getCell(idIndex).getStringCellValue();
                List<List<String>> timeList = new LinkedList<>();
                if (timeMap.containsKey(id)) {
                    timeList = timeMap.get(id);
                } else {
                    List<String> info = new LinkedList<>();
                    info.add(rowData.getCell(idIndex).getStringCellValue());
                    for (Integer col : columsIndex) {
                        info.add(rowData.getCell(col).getStringCellValue());
                    }
                    infoList.add(info);
                }

                List<String> tripTime = new LinkedList<>();
                tripTime.add(rowData.getCell(begIndex).getStringCellValue());
                tripTime.add(rowData.getCell(endIndex).getStringCellValue());
                timeList.add(tripTime);
                timeMap.put(id, timeList);

                Double aliDay = aliDayMap.get(id);
                if (aliDay == null) {
                    aliDay = (double)0;
                }
                aliDay += Double.valueOf(rowData.getCell(aliDayIndex).getStringCellValue());
                aliDayMap.put(id, aliDay);
            }
        }

        // 日期计算
        for (List<String> info : infoList) {
            String id = info.get(0);
            info.add("" + aliDayMap.get(id));
            List<List<String>> timeList = timeMap.get(id);
            calcTime(timeList, info);
        }

        // 结果输出
        sheet = workbook.createSheet("计算结果");
        rowData = sheet.createRow(0);
        rowData.createCell(0).setCellValue("发起人工号");
        for (int col = 0; col < columns.size(); col++) {
            rowData.createCell(col + 1).setCellValue(columns.get(col));
        }
        rowData.createCell(columns.size() + 1).setCellValue("阿里时长");
        rowData.createCell(columns.size() + 2).setCellValue("有效时长");
        rowData.createCell(columns.size() + 3).setCellValue("重叠时间");

        for (int row = 0; row < infoList.size(); row++) {
            rowData = sheet.createRow(row + 1);
            List<String> info = infoList.get(row);
            for (int col = 0; col < info.size(); col++) {
                rowData.createCell(col).setCellValue(info.get(col));
            }
        }
        try {
            workbook.write(new FileOutputStream(filePath2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void calcTime(List<List<String>> timeList, List<String> info) {
        Set<String> set = new HashSet<>();
        List<String> list = new LinkedList<>();
        for (List<String> trip : timeList) {
            Calendar begDate = getDate(trip.get(0));
            String begNoon = getNoon(trip.get(0));
            Calendar endDate = getDate(trip.get(1));
            String endNoon = getNoon(trip.get(1));
            // 开始结束日期相等
            if (endDate.equals(begDate)) {
                if (begNoon.equals(endNoon)) {
                    addToSet(begDate, begNoon, set, list);
                } else if (begNoon.equals("下午") && endNoon.equals("上午")) {
                    System.out.println("时间错误：" + trip);
                } else {
                    addToSet(begDate, begNoon, set, list);
                    addToSet(endDate, endNoon, set, list);
                }
                // 开始结束日期不同
            } else if (endDate.after(begDate)) {
                // 处理开始日期
                addToSet(begDate, begNoon, set, list);
                if (begNoon.equals("上午")) {
                    addToSet(begDate, "下午", set, list);
                }
                // 处理中间日期
                begDate.add(Calendar.DATE, 1);
                while (endDate.after(begDate)) {
                    addToSet(begDate, "上午", set, list);
                    addToSet(begDate, "下午", set, list);
                    begDate.add(Calendar.DATE, 1);
                }
                // 处理结束日期
                addToSet(endDate, endNoon, set, list);
                if (endNoon.equals("下午")) {
                    addToSet(endDate, "上午", set, list);
                }
            } else {
                System.out.println("时间错误" + trip);
            }
        }

        Double date = (double) set.size() / 2;
        info.add("" + date);
        info.add("" + list);
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

    private static void addToSet(Calendar date, String noon, Set<String> set, List<String> list){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String str = df.format(date.getTime()) + noon;
        if (set.contains(str)) {
            list.add(str);
        } else {
            set.add(str);
        }
    }
}
