package com.atguigu.edu.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberCenterVo {
    @ApiModelProperty(value = "会员id")
    private String id;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "用户头像")
    private String avatar;
}
