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

package uk.ac.bbsrc.tgac.miso.webapp.controller.rest;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import uk.ac.bbsrc.tgac.miso.core.data.SampleGroupId;
import uk.ac.bbsrc.tgac.miso.dto.Dtos;
import uk.ac.bbsrc.tgac.miso.dto.SampleGroupDto;
import uk.ac.bbsrc.tgac.miso.service.SampleGroupService;

@Controller
@RequestMapping("/rest")
@SessionAttributes("samplegroup")
public class SampleGroupController extends RestController {

  protected static final Logger log = LoggerFactory.getLogger(SampleGroupController.class);

  @Autowired
  private SampleGroupService sampleGroupService;

  @GetMapping(value = "/samplegroup/{id}", produces = { "application/json" })
  @ResponseBody
  public SampleGroupDto getSampleGroup(@PathVariable("id") Long id, UriComponentsBuilder uriBuilder, HttpServletResponse response)
      throws IOException {
    SampleGroupId sampleGroup = sampleGroupService.get(id);
    if (sampleGroup == null) {
      throw new RestException("No sample group found with ID: " + id, Status.NOT_FOUND);
    } else {
      SampleGroupDto dto = Dtos.asDto(sampleGroup);
      dto.writeUrls(uriBuilder);
      return dto;
    }
  }

  @GetMapping(value = "/samplegroups", produces = { "application/json" })
  @ResponseBody
  public Set<SampleGroupDto> getSampleGroups(UriComponentsBuilder uriBuilder, HttpServletResponse response) throws IOException {
    Set<SampleGroupId> sampleGroups = sampleGroupService.getAll();
    Set<SampleGroupDto> sampleGroupDtos = Dtos.asSampleGroupDtos(sampleGroups);
    for (SampleGroupDto sampleGroupDto : sampleGroupDtos) {
      sampleGroupDto.writeUrls(uriBuilder);
    }
    return sampleGroupDtos;
  }

  @PostMapping(value = "/samplegroup", headers = { "Content-type=application/json" })
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public SampleGroupDto createSampleGroup(@RequestBody SampleGroupDto sampleGroupDto, UriComponentsBuilder b,
      HttpServletResponse response) throws IOException {
    SampleGroupId sampleGroup = Dtos.to(sampleGroupDto);
    Long id = sampleGroupService.create(sampleGroup, sampleGroupDto.getProjectId(), sampleGroupDto.getSubprojectId());
    return Dtos.asDto(sampleGroupService.get(id));
  }

  @PutMapping(value = "/samplegroup/{id}", headers = { "Content-type=application/json" })
  @ResponseBody
  public SampleGroupDto updateSampleGroup(@PathVariable("id") Long id, @RequestBody SampleGroupDto sampleGroupDto,
      HttpServletResponse response) throws IOException {
    SampleGroupId sampleGroup = Dtos.to(sampleGroupDto);
    sampleGroup.setId(id);
    sampleGroupService.update(sampleGroup);
    return Dtos.asDto(sampleGroupService.get(id));
  }

}