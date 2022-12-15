package com.ja.wantrip.app.post.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AnswerForm {
    @NotBlank
    private String content;
}
