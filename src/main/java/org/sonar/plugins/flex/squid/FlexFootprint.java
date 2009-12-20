package org.sonar.plugins.flex.squid;

import org.sonar.squid.recognizer.Detector;
import org.sonar.squid.recognizer.LanguageFootprint;

import java.util.Collections;
import java.util.Set;

public class FlexFootprint implements LanguageFootprint {

  public Set<Detector> getDetectors() {
    return Collections.emptySet();
  }
}

