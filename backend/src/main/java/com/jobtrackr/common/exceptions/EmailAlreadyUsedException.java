package com.jobtrackr.common.exceptions;

public class EmailAlreadyUsedException  extends RuntimeException{
    public EmailAlreadyUsedException(String email){
        super("un compte existe déja avec l'adresse : " + email);
    }
}
