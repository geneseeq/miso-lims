/*
 * Copyright (c) 2012. The Genome Analysis Centre, Norwich, UK
 * MISO project contacts: Robert Davey @ TGAC
 * *********************************************************************
 *
 * This file is part of MISO.
 *
 * MISO is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MISO is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MISO.  If not, see <http://www.gnu.org/licenses/>.
 *
 * *********************************************************************
 */

package uk.ac.bbsrc.tgac.miso.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.eaglegenomics.simlims.core.Note;
import com.eaglegenomics.simlims.core.User;

import uk.ac.bbsrc.tgac.miso.core.data.Project;
import uk.ac.bbsrc.tgac.miso.core.data.impl.ProjectOverview;

public interface ProjectService extends ProviderService<Project> {

  // SAVES
  public long saveProject(Project project) throws IOException;

  public long saveProjectOverview(ProjectOverview overview) throws IOException;

  public void saveProjectOverviewNote(ProjectOverview overview, Note note) throws IOException;


  // GETS
  public Project getProjectByAlias(String projectAlias) throws IOException;

  public Project getProjectByShortName(String projectShortName) throws IOException;

  public ProjectOverview getProjectOverviewById(long overviewId) throws IOException;

  // LISTS
  /**
   * Obtain a list of all the projects the user has access to. Access is defined as either read or write access.
   */
  public Collection<Project> listAllProjects() throws IOException;

  public Collection<Project> listAllProjectsWithLimit(long limit) throws IOException;

  public Collection<Project> listAllProjectsBySearch(String query) throws IOException;

  /**
   * Obtain a list of all the projects the user has access to, sorted alphabetically by shortname.
   * Used for displaying lists in Detailed Sample mode.
   * 
   * @return Collection of Projects sorted alphabetically by shortname.
   * @throws IOException upon failure to access Projects
   */
  public Collection<Project> listAllProjectsByShortname() throws IOException;

  public Collection<ProjectOverview> listAllOverviewsByProjectId(long projectId) throws IOException;

  // DELETES

  public void deleteProjectOverviewNote(ProjectOverview projectOverview, Long noteId) throws IOException;

  public Map<String, Integer> getProjectColumnSizes() throws IOException;

  void addProjectWatcher(Project project, User watcher) throws IOException;

  void removeProjectWatcher(Project project, User watcher) throws IOException;

}
