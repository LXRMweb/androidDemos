package cn.lxrm.demo.crazycat;

/** Description: 画布中的原点的状态-枚举
 * @author created by ChenMeiYu at 2021-1-26 11:28, v1.0
 *      modified by [TODO-修改者] at [TODO-修改时间], [TODO-版本], 修改内容概述如下:
 *          [TODO-修改内容概述]
 */
public enum DotStatusEnum {
    /** OFF: 障碍，疯狂猫不能到达相应位置
     */
    OFF,
    /** ON: 通道，允许疯狂猫进入
     */
    ON,
    /** IN: 疯狂猫所在的位置
     */
    IN
}
