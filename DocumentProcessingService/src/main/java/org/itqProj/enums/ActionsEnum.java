package org.itqProj.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ActionsEnum {
    SUBMIT ("SUBMIT"),
    APPROVE ("APPROVE");

    private final String value;
}
