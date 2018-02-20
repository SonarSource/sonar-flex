movieClip.onEnterFrame = function() {  // Noncompliant {{Refactor this code to remove the use of "onEnterFrame" event handler.}}
};

movieClip.onEnterFrame = null;         // OK
delete movieClip.onEnterFrame;         // OK

var a;

a = function() {                       // OK
}
