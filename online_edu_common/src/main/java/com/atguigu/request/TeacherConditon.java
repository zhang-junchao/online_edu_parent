package com.atguigu.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeacherConditon {
    @ApiModelProperty(value = "讲师姓名")
    private String name;
    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer level;
    @ApiModelProperty(value = "查询开始时间")
    private String beginTime;
    @ApiModelProperty(value = "查询结束时间")
    private String endTime;
}
