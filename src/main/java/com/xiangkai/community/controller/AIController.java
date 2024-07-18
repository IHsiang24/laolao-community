package com.xiangkai.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AIController {

    @RequestMapping(path = "streamChat", method = RequestMethod.GET)
    public String getStreamChat() {
        return "/site/streamchat";
    }

}
