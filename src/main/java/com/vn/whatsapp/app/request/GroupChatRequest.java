package com.vn.whatsapp.app.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupChatRequest {
    private List<Integer> userIds;
    private String chatName;
    private String chatImg;

}
