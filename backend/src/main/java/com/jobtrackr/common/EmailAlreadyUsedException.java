package com.jobtrackr.common;

public class EmailAlreadyUsedException  extends RuntimeException{
    public EmailAlreadyUsedException(String email){
        super("un compte existe déja avec l'adresse : " + email);
    }
}
