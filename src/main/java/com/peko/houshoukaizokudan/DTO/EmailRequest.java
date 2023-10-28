package com.peko.houshoukaizokudan.DTO;

import lombok.Data;

@Data
public class EmailRequest {
    private String recipient;
    private String subject;
    private String content;

    // 添加构造函数、getter 和 setter 方法

    public EmailRequest() {
    }

    public EmailRequest(String recipient, String subject, String content) {
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}