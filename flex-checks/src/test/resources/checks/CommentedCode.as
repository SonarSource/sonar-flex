// Noncompliant@+4
function sayHello() {
  /*
  this line is fine, but the following is not
  if (something) {

  no violation on the following line, because there is at most one violation per comment
  if (something) {
  */

  // Noncompliant@+1
  // if (something) {

  // good
}
