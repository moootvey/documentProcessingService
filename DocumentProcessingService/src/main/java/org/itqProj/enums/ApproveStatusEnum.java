package org.itqProj.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApproveStatusEnum {
    SUCCESSFULLY ("SUCCESSFULLY"),
    CONFLICT ("CONFLICT"),
    NOT_FOUND ("NOT_FOUND"),
    ERROR_REGISTRATION_IN_REGISTRY ("ERROR_REGISTRATION_IN_REGISTRY");

    private final String value;
}
