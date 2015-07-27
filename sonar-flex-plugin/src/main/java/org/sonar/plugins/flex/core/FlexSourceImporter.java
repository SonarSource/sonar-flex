/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.flex.core;

import java.io.File;
import java.util.List;

import org.sonar.api.batch.AbstractSourceImporter;
import org.sonar.api.batch.Phase;
import org.sonar.api.resources.Resource;

/**
 * Source code importer for Flex projects.
 *
 * While importing files, it automatically fills the {@link FlexResourceBridge}.
 *
 */
@Phase(name = Phase.Name.PRE)
public class FlexSourceImporter extends AbstractSourceImporter {

  private FlexResourceBridge resourceBridge;

  /**
   * Instantiates a new {@link FlexSourceImporter}.
   * <br/>
   * <b>Do not call, this constructor is called by Pico container.</b>
   * @param flex the language
   * @param resourceBridge the resource bridge
   */
  public FlexSourceImporter(Flex flex, FlexResourceBridge resourceBridge) {
    super(flex);
    this.resourceBridge = resourceBridge;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Resource createResource(File file, List<File> sourceDirs, boolean unitTest) {
    Resource<?> resource = super.createResource(file, sourceDirs, unitTest);
    resourceBridge.indexFile((org.sonar.api.resources.File) resource);
    return resource;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onFinished() {
    super.onFinished();
    resourceBridge.lock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "Flex Source Importer";
  }
}
