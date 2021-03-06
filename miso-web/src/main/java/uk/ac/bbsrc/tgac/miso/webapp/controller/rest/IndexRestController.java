package uk.ac.bbsrc.tgac.miso.webapp.controller.rest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import uk.ac.bbsrc.tgac.miso.core.data.Index;
import uk.ac.bbsrc.tgac.miso.core.data.type.PlatformType;
import uk.ac.bbsrc.tgac.miso.core.service.IndexService;
import uk.ac.bbsrc.tgac.miso.core.util.PaginatedDataSource;
import uk.ac.bbsrc.tgac.miso.core.util.PaginationFilter;
import uk.ac.bbsrc.tgac.miso.dto.DataTablesResponseDto;
import uk.ac.bbsrc.tgac.miso.dto.Dtos;
import uk.ac.bbsrc.tgac.miso.dto.IndexDto;

@Controller
@RequestMapping(value = "/rest/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class IndexRestController extends RestController {
  @Autowired
  private IndexService indexService;

  private final JQueryDataTableBackend<Index, IndexDto> jQueryBackend = new JQueryDataTableBackend<Index, IndexDto>() {

    @Override
    protected IndexDto asDto(Index model) {
      return Dtos.asDto(model);
    }

    @Override
    protected PaginatedDataSource<Index> getSource() throws IOException {
      return indexService;
    }
  };

  @GetMapping("/dt")
  @ResponseBody
  public DataTablesResponseDto<IndexDto> dataTable(HttpServletRequest request, HttpServletResponse response,
      UriComponentsBuilder uriBuilder)
      throws IOException {
    return jQueryBackend.get(request, response, uriBuilder);
  }

  @GetMapping("/dt/platform/{platform}")
  @ResponseBody
  public DataTablesResponseDto<IndexDto> dataTableByPlatform(@PathVariable("platform") String platform, HttpServletRequest request,
      HttpServletResponse response, UriComponentsBuilder uriBuilder)
      throws IOException {
    PlatformType platformType = PlatformType.valueOf(platform);
    if (platformType == null) {
      throw new RestException("Invalid platform type.", Status.BAD_REQUEST);
    }
    return jQueryBackend.get(request, response, uriBuilder, PaginationFilter.platformType(platformType), PaginationFilter.archived(false));
  }

  public static class IndexSearchRequest {

    private List<String> position1Indices;
    private List<String> position2Indices;

    public List<String> getPosition1Indices() {
      return position1Indices;
    }

    public void setPosition1Indices(List<String> position1Indices) {
      this.position1Indices = position1Indices;
    }

    public List<String> getPosition2Indices() {
      return position2Indices;
    }

    public void setPosition2Indices(List<String> position2Indices) {
      this.position2Indices = position2Indices;
    }

  }

  public static class IndexSearchResult {

    private String indexFamily;

    private long position1Matches;

    private long position2Matches;

    public String getIndexFamily() {
      return indexFamily;
    }

    public void setIndexFamily(String indexFamily) {
      this.indexFamily = indexFamily;
    }

    public long getPosition1Matches() {
      return position1Matches;
    }

    public void setPosition1Matches(long position1Matches) {
      this.position1Matches = position1Matches;
    }

    public long getPosition2Matches() {
      return position2Matches;
    }

    public void setPosition2Matches(long position2Matches) {
      this.position2Matches = position2Matches;
    }

  }

  @PostMapping("/search")
  public @ResponseBody List<IndexSearchResult> searchIndexFamilies(@RequestBody IndexSearchRequest request) {
    return indexService.getIndexFamilies().stream()
        .map(fam -> {
          IndexSearchResult result = new IndexSearchResult();
          result.setIndexFamily(fam.getName());
          result.setPosition1Matches(fam.getIndices().stream()
              .filter(i -> i.getPosition() == 1)
              .map(Index::getSequence)
              .filter(sequence -> request.getPosition1Indices().contains(sequence))
              .count());
          result.setPosition2Matches(fam.getIndices().stream()
              .filter(i -> i.getPosition() == 2)
              .map(Index::getSequence)
              .filter(sequence -> request.getPosition2Indices().contains(sequence))
              .count());
          return result;
        })
        .filter(result -> result.getPosition1Matches() > 0 || result.getPosition2Matches() > 0)
        .collect(Collectors.toList());
  }

}
