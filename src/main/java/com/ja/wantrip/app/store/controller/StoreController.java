package com.ja.wantrip.app.store.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/store")
public class StoreController {

    @PreAuthorize("isAuthenticated")
    @GetMapping("/list")
    public String showList() {
        return "store/store";
    }

}
