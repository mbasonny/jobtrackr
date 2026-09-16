package com.project.jobtrackr.common.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){

        super(message);
    }
}
