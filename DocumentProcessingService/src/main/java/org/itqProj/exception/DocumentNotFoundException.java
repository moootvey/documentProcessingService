package org.itqProj.exception;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String documentId) {
        super("Document with id " + documentId + " not found.");
    }
}
