package org.example.ejb.service.interceptors;

import org.example.model.model.ImageResource;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
public class ImageResourceInterceptor {

    @AroundInvoke
    public Object aroundProcessImageResource(InvocationContext ic) throws Exception {
        try(ImageResource imageResource = (ImageResource) ic.getParameters()[0]) {
            return ic.proceed();
        }

//        ImageResource imageResource = (ImageResource) ic.getParameters()[0];
//        try {
//           return ic.proceed();
//        } finally {
//            imageResource.close();
//        }
    }
}
