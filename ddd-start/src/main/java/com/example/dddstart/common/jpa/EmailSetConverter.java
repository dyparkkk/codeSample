package com.example.dddstart.common.jpa;

import com.example.dddstart.common.model.Email;
import com.example.dddstart.common.model.EmailSet;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class EmailSetConverter implements AttributeConverter<EmailSet, String> {

    @Override
    public String convertToDatabaseColumn(EmailSet attribute) {
        if(attribute == null) return null;
        return attribute.getEmails().stream()
                .map(Email::toString)
                .collect(joining(","));
    }

    @Override
    public EmailSet convertToEntityAttribute(String dbData) {
        if(dbData == null) return null;
        String[] emails = dbData.split(",");
        Set<Email> emailSet = Arrays.stream(emails)
                .map(v -> new Email(v))
                .collect(toSet());
        return new EmailSet(emailSet);
    }
}
