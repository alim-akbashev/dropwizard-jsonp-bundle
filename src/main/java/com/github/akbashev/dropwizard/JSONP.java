package com.github.akbashev.dropwizard;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JSONP {

    /**
     * Default query parameter name to obtain the JavaScript callback function name from.
     */
    String DEFAULT_QUERY = "callback";

    /**
     * The JavaScript callback function name is obtained from a query parameter with the given name.
     */
    String queryParam() default DEFAULT_QUERY;
}
