package com.example.dddstart.common.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/**
 * 이메일 주소 목록을 Set으로 보관하고 DB에는 한 개 칼럼에 콤마로 구분해서 저장 !
 * AttributeConverter와 함께 사용
 */
public class EmailSet {
    private Set<Email> emails = new HashSet<>();

    private EmailSet(){}

    public EmailSet(Set<Email> emails) {
        this.emails.addAll(emails);
    }

    public Set<Email> getEmails() {
        // read-only로 반환
        return Collections.unmodifiableSet(emails);
    }
}
