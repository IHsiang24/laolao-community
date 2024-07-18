package com.xiangkai.community.alpha.controller;

import com.xiangkai.community.errorcode.ErrorCode;
import com.xiangkai.community.errorcode.Result;
import com.xiangkai.community.model.dto.UserInfoDTO;
import com.xiangkai.community.model.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AjaxController {
    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public Result<UserInfoVO> ajax(@RequestBody UserInfoDTO dto) {
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(dto, vo);
        return new Result<>(ErrorCode.SUCCESS, vo);
    }
}
