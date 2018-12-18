    package util;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public final class URILookup {
    
    public static final String getBaseURL() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        HttpServletRequest req = (HttpServletRequest) ec.getRequest();
        
        return req.getScheme() + "://" + req.getServerName() + (req.getServerPort() == 80 ? "" : (":" + req.getServerPort())) + req.getContextPath() + "/";
    }
    
    public static final String getBaseAPI() {
        return getBaseURL() + "webapi/";
    }
    
    public static final String getServerDocumentsFolder() {
            return FacesContext.getCurrentInstance().getExternalContext().getInitParameter("SERVER_DOCUMENTS_FOLDER");
    }
}
