package com.Project.QuickHost.exception;

public class ResourceNotFoundException extends RuntimeException{
   public ResourceNotFoundException(String message)
    {
        super(message);//The super(message); call invokes the constructor of the parent class (RuntimeException) with the provided message
    }
}
