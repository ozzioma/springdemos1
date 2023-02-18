package ozzydev.springdemos.controller;

import com.sap.olingo.jpa.processor.core.api.JPAODataRequestContext;
import com.sap.olingo.jpa.processor.core.api.JPAODataRequestHandler;
import com.sap.olingo.jpa.processor.core.api.JPAODataSessionContextAccess;
import org.apache.olingo.commons.api.ex.ODataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("products/v1/")
@RequestScope
@CrossOrigin(maxAge = 3600)
public class ProductsODataController {

    @Autowired
    @Qualifier("secondaryODataSessionContext")
    private JPAODataSessionContextAccess serviceContext;
    @Autowired
    private JPAODataRequestContext requestContext;

    @RequestMapping(value = "**", method = {RequestMethod.GET, RequestMethod.PATCH, // NOSONAR
            RequestMethod.POST, RequestMethod.DELETE})
    public void crud(final HttpServletRequest req, final HttpServletResponse resp) throws ODataException {

        new JPAODataRequestHandler(serviceContext, requestContext).process(req, resp);
    }
}
