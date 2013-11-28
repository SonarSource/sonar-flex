[Event(name="message")]
[ManagedEvents("mes")]                   // NOK
public class MyClass {
}

[Event(name="message")]
[ManagedEvents("message, click")]      // NOK
public class MyClass {
}

[Event(name="message")]
[ManagedEvents("message")]               // OK
public class MyClass {
}

[Event(name="click")]                    // OK
[Event(name="message")]
[ManagedEvents("message, click")]
public class MyClass {
}

[Event(name="message")]
[ManagedEvents("message")]               // OK
[Effect(name="blur", event="message")]
public class MyClass {
}
