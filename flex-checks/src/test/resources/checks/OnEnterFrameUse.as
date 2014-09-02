movieClip.onEnterFrame = function() {  // NOK
};

movieClip.onEnterFrame = null;         // OK
delete movieClip.onEnterFrame;         // OK

var a;

a = function() {                       // OK
}
