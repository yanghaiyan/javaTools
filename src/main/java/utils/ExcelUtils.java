package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import netty.entity.RequestEntity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author yan
 * @version V1.0
 * @desc
 */
public class ExcelUtils<T> {

  public HSSFWorkbook exportExcel(String title, String[] headers, List<T> dataset) {

    // 声明一个工作薄
    HSSFWorkbook workbook = new HSSFWorkbook();
    // 生成一个表格
    HSSFSheet sheet = workbook.createSheet(title);
    // 设置表格默认列宽度为15个字节
    sheet.setDefaultColumnWidth(15);

    // 生成一个样式
    HSSFCellStyle style = workbook.createCellStyle();
    // 设置这些样式
   // style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

    // 生成一个字体
    HSSFFont font = workbook.createFont();
    font.setColor(HSSFColor.VIOLET.index);
    font.setFontHeightInPoints((short) 12);
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

    // 把字体应用到当前的样式
    style.setFont(font);
    // 生成并设置另一个样式
    HSSFCellStyle style2 = workbook.createCellStyle();
    //style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
    style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
    style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    // 生成另一个字体
    HSSFFont font2 = workbook.createFont();
    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    // 把字体应用到当前的样式
    style2.setFont(font2);

    // 产生表格标题行
    HSSFRow row = sheet.createRow(0);
    for (int i = 0; i < headers.length; i++) {
      HSSFCell cell = row.createCell(i);
      cell.setCellStyle(style);
      HSSFRichTextString text = new HSSFRichTextString(headers[i]);
      cell.setCellValue(text);
    }

    // 遍历集合数据，产生数据行
    Iterator<T> it = dataset.iterator();
    int index = 0;

    try {

      while (it.hasNext()) {
        index++;
        row = sheet.createRow(index);
        T t = (T) it.next();

        // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
        Field[] fields = t.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
          HSSFCell cell = row.createCell(i);
          cell.setCellStyle(style2);
          Field field = fields[i];
          String fieldName = field.getName();
          String getMethodName = "get"
              + fieldName.substring(0, 1).toUpperCase()
              + fieldName.substring(1);

          Class tCls = t.getClass();

          Method getMethod = tCls.getMethod(getMethodName, new Class[]{});

          Object value = getMethod.invoke(t, new Object[]{});

          //全部当做字符串来处理

          if (value != null) {
            String textValue = value.toString();
            HSSFRichTextString richString = new HSSFRichTextString(textValue);
            HSSFFont font3 = workbook.createFont();
            font3.setColor(HSSFColor.BLUE.index);
            richString.applyFont(font3);
            cell.setCellValue(richString);
          }
        }
      }

    } catch (Exception ex) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }

    return workbook;
  }

  public static void main(String[] args) {
    List<RequestEntity> dataList = new ArrayList<>();
    RequestEntity entity = new RequestEntity();
    entity.setContent("test");
    entity.setExtension("extension");

    for (int i = 0; i <10 ; i++) {
      dataList.add(entity);
    }

    String[] headers = {"姓名", "身份证号", "Key序列号", "签名证书序列号", "加密证书序列号"};
    String filename = "e:\\"+ System.currentTimeMillis() + "学生信息.xls";
    try (OutputStream out = new FileOutputStream(new File(filename))){
      ExcelUtils<RequestEntity> eu = new ExcelUtils();
      HSSFWorkbook workbook = eu.exportExcel("发票领用",headers,dataList);
      workbook.write(out);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
