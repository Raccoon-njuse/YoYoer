package com.example.YoYoer.Utils;

import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * @description Excel工具类
 * @author raccoon
 * @date 2024/7/14
 */
public class ExcelUtils {

    /**
     * 读取excel文件
     * @param file
     * @throws FileNotFoundException
     */
    public static String[][] readExcel(File file) throws FileNotFoundException {
        if (file == null) {
            Log.e("NullFile", "读取Excel出错，空文件");
            return null;
        }
        InputStream stream = new FileInputStream(file);
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            String[][] result = new String[rowsCount][];
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Row firstRow = sheet.getRow(0);
            int maxCol = firstRow.getPhysicalNumberOfCells();
            for (int r = 0; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = Math.max(row.getPhysicalNumberOfCells(), maxCol);
                result[r] = new String[cellsCount];
                for (int c = 0; c < cellsCount; c++) {
                    String value = getCellAsString(row, r, c, formulaEvaluator);
                    result[r][c] = value;
                    String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                    Log.d("cellInfo", cellInfo);
                }
            }
            return result;
        } catch (Exception e) {
            Log.e("XSSFWorkbook", e.toString());
        }
        return null;
    }

    /**
     * 读取每一行内容
     * @param row
     * @param c,r 格子坐标
     * @param formulaEvaluator
     * @return
     */
    private static String getCellAsString(Row row, int r, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {
            Log.e("NullCellException", "r:" + r + "; c" + c + "; " + e.toString());
            value= "";
        }
        return value;
    }

    /**
     * 根据类型后缀判断是否excel
     * @param file 文件
     * @return 是否excel
     */
    public static boolean isExcelFile(File file) {
        if (file == null) {
            return false;
        }
        String name = file.getName();
        // “.”转义
        String[] list = name.split("\\.");
        if (list.length < 2) {
            return false;
        }
        String typeName= list[list.length - 1];
        return "xls".equals(typeName) || "xlsx".equals(typeName);
    }
}
