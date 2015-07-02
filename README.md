# dropwizard-jsonp-bundle
Simple bundle for Dropwizard that adds JSONP support for any resources.
Inspired by JsonWithPaddingInterceptor, but unlike last one it wraps response with `callback()` if `callback` arg is set in query string.

## Maven
```
<dependency>
    <groupId>com.github.alim-akbashev</groupId>
    <artifactId>dropwizard-jsonp-bundle</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Usage
Initialize bundle:
```java
public void initialize(final Bootstrap<AppConfiguration> bootstrap) {
    bootstrap.addBundle(new JsonPBundle());
}
```

Anotate resource using `JSONP` annotation:
```java
@Path("/something")
public class SomeResource {

  @GET
  @JSONP(queryParam = "thecallback")
  public String get() {
    return "..."
  }

}
```

The `queryParam` is optional. Default value is `callback`.

### Jquery Example
```javascript
$.ajax({
    type: 'GET',
    url: "http://localhost/something",
    jsonp: "thecallback",
    dataType: "jsonp",
    success: function( response ) {
        console.log( response );
    }
});
```
