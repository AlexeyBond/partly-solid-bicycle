# Component meta-attributes

Meta-attributes are named properties associated with component or it's properties/methods by `@Meta` annotation.
Annotation processor uses meta-attributes to determine desired behavior of generated code.

## Equivalent annotations

For some attributes there are separate annotations assigning one or more meta-attributes.
To assign meta attributes annotation interface should have method named `asMeta` returning `@Meta` annotation (usually such method has a default value containing `@Meta` annotation with attributes that should be assigned).

If the value of attribute of `@Meta` annotation returned by `asMeta` method starts with `$` then the rest of value is interpreted as name of method of the annotation containing that `asMeta` method.
Value returned by that method will be used as value of corresponding meta-attribute.

Example:

```java
public @interface ExampleAnnotation {
    String value();
    
    Meta asMeta() default @Meta({
        "attr1=foo",
        "attr2=$value"
    });
}
```

annotation

```java
@ExampleAnnotation("bar")
```

will be equivalent to

```java
@Meta({
    "attr1=foo",
    "attr2=bar"
})
```

Note: There may be multiple different annotations with `asMeta` method

Note: In case of multiple `@Meta` annotations or annotations with `asMeta` method defining value for the same meta-attribute preprocessor provides no guarantees of what of those values will be used

## List of meta-attributes supported by core preprocessor

| name | equivalent annotations | description |
|------|-----------------------|-------------|
| property.serializedName | `@SerializedName` | Name of property when serialized. This attribute is considered by "loader" and "saver" companions. |
| property.isSkipped | `@SkipProperty` | "true" if the property must be ignored by annotation processor |
| property.isOptional | `@Optional` | "true" if the property is optional for serialized representation of component |
| property.bind | `@From*` | "true" if value associated with some node should be assigned to the property by "connector" companion on component connection |
| property.bindMode | `@From*` | defines where from "connector" takes path to the node to take value from |
| property.bindPath | `@FromPath` | text representation of node path if `property.bindMode=path` |
| property.bindAttribute | `@FromAttribute` | name of a attribute (serializable property created by annotation processor) containing the path to the node if `property.bindMode=attribute` |
| property.bindPathDefault | `@FromAttribute` | default path for `property.bindMode=attribute`; non-empty value makes created property optional |
| property.forceReset | - | "true" if the property with `property.bind=true` should be set to `null` when component is disconnected (by default it's done only for properties without setter) |
| method.subscribe | `@On`* | "true" if the method should be subscribed to events happening on some event source (`Topic`) |
| method.subscribeMode | `@On`* | defines where from connector takes the topic to subscribe the method to |
| method.subscribePath | `@OnEventAtPath` | when `method.subscribeMode=path` defines the path to the node containing the topic as component |
| method.subscribeAttribute | `@OnEventAtAttribute` | when `method.subscribeMode=attribute` defines name of attribute containing path to the node containing the topic |
| method.subscribePathDefault | `@OnEventAtAttribute` |  when `method.subscribeMode=attribute` defines default path to the node; non-empty value makes created attribute optional |
| method.subscribeProperty | `@OnEventAtProperty` | when `method.subscribeMode=property` defines name of component's property containing the topic |
