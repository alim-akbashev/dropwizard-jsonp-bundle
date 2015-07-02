package com.github.akbashev.dropwizard;

import org.glassfish.jersey.message.MessageUtils;
import org.glassfish.jersey.server.ContainerRequest;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.InterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

public class JsonPInterceptor implements WriterInterceptor {

    @Inject
    private Provider<ContainerRequest> containerRequestProvider;

    @Override
    public void aroundWriteTo(final WriterInterceptorContext context) throws IOException, WebApplicationException {
        final JSONP jsonp = getJsonpAnnotation(context);

        if (jsonp == null) {
            context.proceed();
            return;
        }

        String callbackName = getCallbackName(jsonp);

        final boolean wrapIntoCallback = callbackName != null && callbackName.length() > 0;

        if (wrapIntoCallback) {
            context.setMediaType(MediaType.APPLICATION_JSON_TYPE);

            context.getOutputStream().write(callbackName.getBytes(MessageUtils.getCharset(context.getMediaType())));
            context.getOutputStream().write('(');
        }

        context.proceed();

        if (wrapIntoCallback) {
            context.getOutputStream().write(')');
        }
    }

    /**
     * Returns a JavaScript callback name to wrap the JSON entity into. The callback name is determined from the {@link JSONP}
     * annotation.
     *
     * @param jsonp {@link JSONP} annotation to determine the callback name from.
     * @return a JavaScript callback name.
     */
    private String getCallbackName(final JSONP jsonp) {
        String callback = null;

        if (!"".equals(jsonp.queryParam())) {
            final ContainerRequest containerRequest = containerRequestProvider.get();
            final UriInfo uriInfo = containerRequest.getUriInfo();
            final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
            final List<String> queryParameter = queryParameters.get(jsonp.queryParam());

            if (queryParameter != null && !queryParameter.isEmpty()) {
                callback = queryParameter.get(0);
            }
        }

        return callback;
    }

    /**
     * Returns a {@link JSONP} annotation of the resource method responsible for handling the current request.
     *
     * @param context an {@link InterceptorContext interceptor context} to obtain the annotation from.
     * @return {@link JSONP} annotation or {@code null} if the resource method is not annotated with this annotation.
     * @see javax.ws.rs.ext.InterceptorContext#getAnnotations()
     */
    private JSONP getJsonpAnnotation(final InterceptorContext context) {
        final Annotation[] annotations = context.getAnnotations();

        if (annotations != null && annotations.length > 0) {
            for (final Annotation annotation : annotations) {
                if (annotation instanceof JSONP) {
                    return (JSONP) annotation;
                }
            }
        }

        return null;
    }
}
