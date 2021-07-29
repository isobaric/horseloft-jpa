package com.horseloft.jpa.vo.shelf;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentLoopMerge;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 2020/2/7 下午3:29
 * User: YHC
 * Desc:
 */
@Getter
@Setter
@ToString
@ColumnWidth(15)
@ContentRowHeight(23)
public class ShelfListDownloadVo implements Serializable {

    @ContentLoopMerge(eachRow = 2)
    @ExcelProperty("货架编号")
    private String shelfCode;

    @ContentLoopMerge(eachRow = 2)
    @ExcelProperty("货架坐标")
    private String axisText;

    @ContentLoopMerge(eachRow = 2)
    @ExcelProperty("是否在地图上")
    private String mapStatusText;

    @ContentLoopMerge(eachRow = 2)
    @ExcelProperty("是否锁定")
    private String lockStatusText;

    @ExcelProperty("货箱编号")
    private String slotCode;

    @ExcelProperty("物料名称")
    private String materialName;

    @ExcelProperty("物料编号")
    private String materialCode;

    @ExcelProperty("物料数量")
    private Integer materialNumber;

    @ExcelProperty("生产订单编号")
    private String productionOrderCode;

    @ExcelProperty("当前物料状态")
    private String materialStateText;
}
