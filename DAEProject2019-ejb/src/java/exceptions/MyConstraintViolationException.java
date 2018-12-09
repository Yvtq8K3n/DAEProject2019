/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Joao Marquez
 */
public class MyConstraintViolationException extends Exception{

    public MyConstraintViolationException(String message) {
        super(message);
    }
    
}
