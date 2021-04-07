package cn.lxrm.demo.crazycat;

import androidx.annotation.NonNull;

/**
 * README: 游戏画布中的一个点，POJO对象
 *
 * @author created by ChenMeiYu at 2021-1-26 11:25, v1.0
 * modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 * [TODO-修改内容概述]
 */
public class DotVO {
    // 横坐标
    private int col;
    // 纵坐标
    private int row;
    // 状态
    private DotStatusEnum status;

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int y) {
        this.row = row;
    }

    public DotStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DotStatusEnum status) {
        this.status = status;
    }

    public DotVO(int col, int row) {
        this.col = col;
        this.row = row;
        this.status = DotStatusEnum.ON;
    }

    @NonNull
    @Override
    public String toString() {
        return "("+this.col+","+this.row+")";
    }
}
