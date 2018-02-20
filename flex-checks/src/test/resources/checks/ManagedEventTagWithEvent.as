[Event(name="message")]
[ManagedEvents("mes")]                   // Noncompliant {{The managed event "mes" is either misspelled or is missing a companion Event metadata tag}}
public class MyClass {
}

[Event(name="message")]
[ManagedEvents("message, click")]      // Noncompliant {{The managed event "click" is either misspelled or is missing a companion Event metadata tag}}
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
