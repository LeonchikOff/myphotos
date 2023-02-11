package org.example.web.component;

import org.apache.commons.beanutils.BeanUtils;
import org.example.model.exception.ApplicationException;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@ApplicationScoped
public class FormReader {

    public <TypeForm> TypeForm readForm(HttpServletRequest request, Class<TypeForm> classOfForm) {
        try {
            TypeForm formBean = classOfForm.newInstance();
            Map<String, String[]> requestParameterMap = request.getParameterMap();
            BeanUtils.populate(formBean, requestParameterMap);
            return formBean;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new ApplicationException(e);
        }
    }
}
