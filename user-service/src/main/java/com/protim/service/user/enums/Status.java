package com.protim.service.user.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Status {
    ACTIVE, SUSPENDED, DEACTIVATED;

    // Cache all valid enum names in uppercase for O(1) lookups
    private static final Set<String> VALID_STATUSES = Arrays.stream(Status.values())
            .map(Status::name)
            .collect(Collectors.toUnmodifiableSet());

    public static boolean isValidStatus(String statusStr) {
        if (statusStr == null || statusStr.isBlank()) {
            return false;
        }
        // Pure string lookup — no exception allocation overhead
        return VALID_STATUSES.contains(statusStr.trim().toUpperCase());
    }

    public static Status fromString(String statusStr) {
        if (statusStr == null || statusStr.isBlank()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        try {
            return Status.valueOf(statusStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: '" + statusStr +
                    "'. Allowed values are: ACTIVE, SUSPENDED, DEACTIVATED");
        }
    }
}
