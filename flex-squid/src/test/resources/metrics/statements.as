function foo() {
  ; // +1 empty statement

  var a = 1; // +1 declaration statement
  a = a + 1; // +1 expression statement

  if (false) // +1 if statement
  { // +0 compound statement
    throw new Exception(); // +1 throw statement
  }

  label: // +0 labelled statement
  for (i = 0; i < 10; i++) // +1 for statement
  { // +0 compound statement
    break; // +1 break statement
  }

  while (false) // +1 while statement
  { // +0 compound statement
    continue; // +1 continue statemen
  }

  do // +1 do-while statement
  { // +0 compound statement
  } while (false);

  for each (a in b) // +1 for-each statement
  { // +0 compound statement
  }

  with (a) // +1 with statement
  { // +0 compound statement
  }

  switch (param) { // +1 switch statement
    case 0:
    case 1:
    default:
  }

  try { // +1 try statement
  } catch (e:Exception) {
  } finally {
  }

  set("variableName", value); // +1 set-variable statement

  a = a + 1; a = a + 1; // +2 expression statement

  return; // +1 return statement
}
