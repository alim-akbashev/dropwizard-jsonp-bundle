# dropwizard-jsonp-bundle
Simple bundle for Dropwizard that adds JSONP support for any resources

## Usage
```java
public void initialize(final Bootstrap<AppConfiguration> bootstrap) {
    bootstrap.addBundle(new JsonPBundle());
}
```

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
