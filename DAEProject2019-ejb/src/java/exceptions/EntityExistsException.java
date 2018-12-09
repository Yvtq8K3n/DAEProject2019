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
public class EntityExistsException extends Exception{

    public EntityExistsException() {
        super("Entity already exists");
    }

    public EntityExistsException(String message) {
        super(message);
    }
    
}
