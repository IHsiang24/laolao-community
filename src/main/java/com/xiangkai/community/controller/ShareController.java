package com.xiangkai.community.controller;

import com.xiangkai.community.errorcode.ErrorCode;
import com.xiangkai.community.errorcode.Result;
import com.xiangkai.community.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ShareController {

    @Autowired
    private ShareService shareService;

    @RequestMapping(path = "/share", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> share(
            @RequestParam(value = "mode", defaultValue = "image") String mode,
            @RequestParam(value = "pageUrl") String pageUrl) {

        String shareUrl = shareService.share(mode, pageUrl);
        return new Result<>(ErrorCode.SUCCESS, shareUrl);
    }

    @RequestMapping(path = "/share/{filename}", method = RequestMethod.GET)
    public void getShareContent(@PathVariable("filename") String filename,
                                HttpServletResponse response) {

        shareService.getShareContent(filename, response);
    }
}
