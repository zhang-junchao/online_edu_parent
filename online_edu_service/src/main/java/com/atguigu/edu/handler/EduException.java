package com.atguigu.edu.handler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Exception         如果有事务是不能回滚
//RuntimeException  有事务可以自动回滚
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("这是一个自定义异常")
public class EduException extends RuntimeException {
    @ApiModelProperty(value = "异常状态码")
    private Integer code;
    @ApiModelProperty(value = "异常信息")
    private String message;
}
