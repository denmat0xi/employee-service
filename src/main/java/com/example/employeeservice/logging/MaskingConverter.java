package com.example.employeeservice.logging;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Logback converter designed to sanitize sensitive data in application logs.
 * <p>
 * This converter intercepts log messages before they are appended to the output destination.
 * It uses regular expressions to identify and mask PII (Personally Identifiable Information)
 * such as email addresses and phone numbers, replacing them with a fixed mask string.
 * This ensures compliance with data protection policies and prevents credential leakage.
 */
public class MaskingConverter extends MessageConverter {

    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}";
    private static final String PHONE_PATTERN = "\\d{10,15}";

    /**
     * Transforms the raw log message by applying masking rules.
     *
     * @param event the logging event containing the original message
     * @return the processed, masked message string
     */
    @Override
    public String convert(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        return maskMessage(message);
    }

    /**
     * Orchestrates the masking process by applying all defined sensitive data patterns.
     *
     * @param message the raw log message to process
     * @return the message with sensitive data replaced by masks
     */
    private String maskMessage(String message) {
        message = mask(message, EMAIL_PATTERN, "email");
        message = mask(message, PHONE_PATTERN, "phone");
        return message;
    }

    /**
     * Replaces substrings matching the provided regex pattern with a masking string.
     *
     * @param message the original string
     * @param regex   the regular expression pattern to match sensitive data
     * @param label   a description of the data type (used for clarity)
     * @return the resulting string with replaced sensitive data
     */
    private String mask(String message, String regex, String label) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "?");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}