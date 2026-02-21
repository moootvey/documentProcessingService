package org.itqProj.component.interfaces;

import org.aspectj.lang.ProceedingJoinPoint;

public interface LoggerComponent {
    Object printLog(ProceedingJoinPoint joinPoint);
    Object saveDocumentHistoryLog(ProceedingJoinPoint joinPoint);
}
