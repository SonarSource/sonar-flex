[Event]                                           // OK

[Event(name="click", type="flash.events.Event")]  // OK

[Inspectable(defaultValue="false")]               // OK

[Event(name="click")]                             // Noncompliant {{The "click" event type is missing in this metadata tag}}


