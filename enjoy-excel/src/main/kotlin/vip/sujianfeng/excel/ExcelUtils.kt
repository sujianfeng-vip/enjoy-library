package vip.sujianfeng.excel

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import vip.sujianfeng.utils.comm.ConvertUtils
import java.io.File
import java.io.FileOutputStream


/**
 * @Author SuJianFeng
 * @Date 2023/8/7
 * @Description
 **/
class ExcelUtils {

    companion object {
        fun save2xls(columns: List<ExcelColumn>, rows: List<Map<String, Any>>, fileName: String, extName: String): String {
            val workbook: Workbook = if ("xls".equals(extName, ignoreCase = true)) {
                HSSFWorkbook()
            } else {
                SXSSFWorkbook()
            }
            var rowNo = 0
            val sheet = workbook.createSheet()
            val row = sheet.createRow(rowNo)
            for (i in columns.indices) {
                 row.createCell(i).setCellValue(columns[i].columnTitle)
            }
            rows.forEach {
                val row = sheet.createRow(++rowNo)
                for ((columnNo, i) in columns.indices.withIndex()) {
                    row.createCell(columnNo).setCellValue(ConvertUtils.cStr(it[columns[i].columnName]))
                }
            }
            val fullFileName = "$fileName.$extName"
            val file = File(fullFileName)
            val fout = FileOutputStream(file)
            workbook.write(fout)
            workbook.close()
            fout.close()
            return fullFileName
        }
    }

}