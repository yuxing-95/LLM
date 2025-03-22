package com.yuxing.controller;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class returnController {
    @Autowired
    QwenChatModel model;

    @RequestMapping("/chat")
    public String test01(String message) {
        return model.chat("你好");
    }
}
