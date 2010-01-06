/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.io;

import java.util.List;

public class ChannelDispatcher {
  private List<Channel> channels = null;

  public ChannelDispatcher(List<Channel> tokenizers) {
    this.channels = tokenizers;
  }

  public void read(CodeReader code) {
    while (code.peek() != -1) {
        nextDispatch:
      for (Channel channel : channels) {
        if (channel.read(code)) {
          continue nextDispatch;
        }
      }
      System.out.println("I am the Channel Dispatcher and I should not be here !");
      code.pop(null);
    }
  }
}
