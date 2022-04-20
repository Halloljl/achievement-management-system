package org.hallo.ams.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * excel操作工具类
 */
public class HSSFUtils {

    /**
     * 获取cell的类型，以字符串传出
     * @param cell
     * @return
     */
    public static String getCellValueForStr(HSSFCell cell){
        String ret = "";
        switch (cell.getCellType()){
            case HSSFCell.CELL_TYPE_STRING:
                ret = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret = cell.getNumericCellValue()+"";
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret = cell.getBooleanCellValue()+"";
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                ret = cell.getCellFormula();
                break;
            default:
                ret = "";
        }
        return ret;
    }


}
