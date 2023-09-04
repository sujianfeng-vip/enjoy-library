package vip.sujianfeng.excel

import org.junit.jupiter.api.Test
import vip.sujianfeng.utils.comm.GuidUtils

/**
 * @Author SuJianFeng
 * @Date 2023/8/7
 * @Description
 **/
class ExcelUtilsTest {

    @Test
    fun test() {
        val filename = "C:\\temp\\${GuidUtils.buildGuid()}"
        ExcelUtils.save2xls(ArrayList<ExcelColumn>().apply {
           this.add(ExcelColumn().apply {
               this.columnName = "userNo"
               this.columnTitle = "用户编号"
           })
           this.add(ExcelColumn().apply {
               this.columnName = "userName"
               this.columnTitle = "用户姓名"
           })
        }, ArrayList<Map<String, Any>>().apply {
           this.add(HashMap<String, Any>().apply {
               this["userNo"] = "001"
               this["userName"] = "张三"
           })
            this.add(HashMap<String, Any>().apply {
                this["userNo"] = "002"
                this["userName"] = "李四"
            })
        }, filename, "xls")
    }
}