package org.itqProj.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DocumentStatusEnum {
    DRAFT ("DRAFT"),
    SUBMITTED ("SUBMITTED"),
    APPROVED ("APPROVED");

    private final String value;
}
