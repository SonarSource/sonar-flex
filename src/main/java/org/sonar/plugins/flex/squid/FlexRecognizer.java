package org.sonar.plugins.flex.squid;

import org.sonar.squid.recognizer.CodeRecognizer;

public final class FlexRecognizer extends CodeRecognizer {

  public FlexRecognizer() {
    super(1.0, new FlexFootprint());
  }

}
