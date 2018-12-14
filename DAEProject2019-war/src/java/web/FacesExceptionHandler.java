package web;

import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class FacesExceptionHandler {

    /**
     * Adds a message to the current faces context so that it is shown in the
     * h:messages component of the current page and shows a message in the
     * server log window.
     *
     * @param e exception whose messsage will be shown in the server log window
     * @param messageUI message to be shown in the page
     * @param logger logger used to show the message in the server log window
     */
    public static void handleException(Exception e, String messageUI, Logger logger) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageUI, null);
        //FacesMessage message = new FacesMessage(messageUI);
        FacesContext.getCurrentInstance().addMessage(null, message);
        if (logger != null) {
            logger.warning(e.getMessage());
        }
    }

    /**
     * Adds a message to the current faces context so that it is shown in the
     * h:message component of a given page component and shows the given
     * exception message in the server log window.
     *
     * @param e exception whose messsage will be shown in the server log window
     * @param messageUI message to be shown in the page
     * @param component component to witch the message will be associated
     * @param logger logger used to show the message in the server log window
     */
    public static void handleException(Exception e, String messageUI, UIComponent component, Logger logger) {
        FacesMessage message = new FacesMessage(messageUI);
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(component.getClientId(context), message);
        if (logger != null) {
            logger.warning(e.getMessage());
        }
    }

}
