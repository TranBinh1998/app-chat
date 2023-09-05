package com.vn.whatsapp.app.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendmessageRequest {

    private Integer userId;

    private Integer chatId;

    private String content;

}
