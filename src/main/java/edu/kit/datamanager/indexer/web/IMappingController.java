/*
 * Copyright 2019 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.datamanager.indexer.web;

import edu.kit.datamanager.indexer.domain.MappingRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URISyntaxException;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 */
@ApiResponses(value = {
  @ApiResponse(responseCode = "401", description = "Unauthorized is returned if authorization in required but was not provided."),
  @ApiResponse(responseCode = "403", description = "Forbidden is returned if the caller has no sufficient privileges.")})
public interface IMappingController {

  @Operation(summary = "Create a new mapping for elasticsearch.", description = "This endpoint allows to create a new mapping as file upload. The record metadata mainly contains "
          + "the mapping identifier and (optional) the mapping type. As a starting point only gemma mappings are allowed. For future versions other mappings like handlebar or xslt might be provided also.",
          responses = {
            @ApiResponse(responseCode = "201", description = "Created is returned only if the record has been validated, persisted and the mapping document was successfully validated and stored.", content = @Content(schema = @Schema(implementation = MappingRecord.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request is returned if the provided metadata record or the mappong is invalid."),
            @ApiResponse(responseCode = "409", description = "A Conflict is returned, if there is already a record for the related mapping id.")})

  @RequestMapping(path = "", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @ResponseBody
  public ResponseEntity<MappingRecord> createMapping(
          @Parameter(description = "Json representation of the mapping record.", required = true) @RequestPart(name = "record", required = true) final MultipartFile record4Mapping,
          @Parameter(description = "The mapping document associated with the record. The document must match the mappingType selected by the record.", required = true) @RequestPart(name = "document", required = true) final MultipartFile document,
          final HttpServletRequest request,
          final HttpServletResponse response,
          final UriComponentsBuilder uriBuilder) throws URISyntaxException;

  @Operation(summary = "Get a mapping record by its identifiers (mappingId, mappingType).", description = "Obtain is single record by its identifiers. "
          + "Depending on a user's role, accessing a specific record may be allowed or forbidden.",
          responses = {
            @ApiResponse(responseCode = "200", description = "OK and the record is returned if the record exists and the user has sufficient permission.", content = @Content(schema = @Schema(implementation = MappingRecord.class))),
            @ApiResponse(responseCode = "404", description = "Not found is returned, if no record for the provided id or version was found.")})

  @RequestMapping(value = {"/{mappingId}/{mappingType}"}, method = {RequestMethod.GET}, produces = {"application/vnd.datamanager.mapping-record+json"})
  @ResponseBody
  public ResponseEntity<MappingRecord> getMappingById(
          @Parameter(description = "The schema linked to the mapping.", required = true) @PathVariable(value = "mappingId") String mappingId,
          @Parameter(description = "The type of the mapping.", required = true) @PathVariable(value = "mappingType") String mappingType,
          Pageable pgbl,
          WebRequest wr,
          HttpServletResponse hsr);

  @Operation(summary = "Get a mapping document by its mappingId and mappingType.", description = "Obtain is single mapping document identified by its identifiers. "
          + "Depending on a user's role, accessing a specific record may be allowed or forbidden. ",
          responses = {
            @ApiResponse(responseCode = "200", description = "OK and the mapping document is returned if the record exists and the user has sufficient permission."),
            @ApiResponse(responseCode = "404", description = "Not found is returned, if no record for the provided identifiers.")})

  @RequestMapping(value = {"/{mappingId}/{mappingType}"}, method = {RequestMethod.GET})
  @ResponseBody
  public ResponseEntity getMappingDocumentById(
          @Parameter(description = "The schema linked to the mapping.", required = true) @PathVariable(value = "mappingId") String mappingId,
          @Parameter(description = "The type of the mapping.", required = true) @PathVariable(value = "mappingType") String mappingType,
          WebRequest wr,
          HttpServletResponse hsr);

  @Operation(summary = "Get all records.", description = "List all records in a paginated and/or sorted form. The result can be refined by providing specific related resource id(s) and/or metadata schema id(s) valid records must match. "
          + "If both parameters are provided, a record matches if its related resource identifier AND the used metadata schema are matching. "
          + "Furthermore, the UTC time of the last update can be provided in three different fashions: 1) Providing only updateFrom returns all records updated at or after the provided date, 2) Providing only updateUntil returns all records updated before or "
          + "at the provided date, 3) Providing both returns all records updated within the provided date range."
          + "If no parameters are provided, all accessible records are listed. If versioning is enabled, only the most recent version is listed.",
          responses = {
            @ApiResponse(responseCode = "200", description = "OK and a list of records or an empty list of no record matches.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MappingRecord.class))))})
  @RequestMapping(value = {""}, method = {RequestMethod.GET})
  @PageableAsQueryParam
  @ResponseBody
  public ResponseEntity<List<MappingRecord>> getMappings(
          @Parameter(description = "The schema linked to the mapping.", required = false) @RequestParam(value = "mappingId") String mappingId,
          @Parameter(description = "The type of the mapping.", required = false) @RequestParam(value = "mappingType") String mappingType,
          Pageable pgbl,
          WebRequest wr,
          HttpServletResponse hsr,
          UriComponentsBuilder ucb);

  @Operation(summary = "Update a metadata record.", description = "Apply an update to the metadata record with the provided identifier and/or its accociated metadata document. The identifier can be either the numeric identifier or the related resource's identifier."
          + "If versioning is enabled, a new version of the record is created. Otherwise, the record and/or its metadata are overwritten.",
          responses = {
            @ApiResponse(responseCode = "200", description = "OK is returned in case of a successful update, e.g. the record (if provided) was in the correct format and the document (if provided) matches the provided schema id. The updated record is returned in the response.", content = @Content(schema = @Schema(implementation = MappingRecord.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request is returned if the provided metadata record is invalid or if the validation using the provided schema failed."),
            @ApiResponse(responseCode = "404", description = "Not Found is returned if no record for the provided id or no schema for the provided schema id was found.")})
  @RequestMapping(value = "/{mappingId}/{mappingType}", method = RequestMethod.PUT, produces = {"application/json"})
  @Parameters({
    @Parameter(name = "If-Match", description = "ETag of the object. Please use quotation marks!", required = true, in = ParameterIn.HEADER)
  })
  ResponseEntity<MappingRecord> updateMapping(
          @Parameter(description = "The schema linked to the mapping.", required = true) @PathVariable(value = "mappingId") String mappingId,
          @Parameter(description = "The type of the mapping.", required = true) @PathVariable(value = "mappingType") String mappingType,
          @Parameter(description = "JSON representation of the metadata record.", required = false) @RequestPart(name = "record", required = true) final MultipartFile record4Mapping,
          @Parameter(description = "The metadata document associated with the record. The document must match the schema defined in the record.", required = false) @RequestPart(name = "document", required = false) final MultipartFile document,
          final WebRequest request,
          final HttpServletResponse response,
          final UriComponentsBuilder uriBuilder
  );

  @Operation(summary = "Delete a mapping.", description = "Delete a single mapping record and the associated mapping document. "
          + "Deleting a record typically requires the caller to have special permissions. "
          + "In some cases, deleting a record can also be available for the owner or other privileged users or can be forbidden at all. ",
          responses = {
            @ApiResponse(responseCode = "204", description = "No Content is returned as long as no error occurs while deleting a record. Multiple delete operations to the same record will also return HTTP 204 even if the deletion succeeded in the first call.")})
  @RequestMapping(value = {"/{mappingId}/{mappingType}"}, method = {RequestMethod.DELETE})
  @Parameters({
    @Parameter(name = "If-Match", description = "ETag of the object. Please use quotation marks!", required = true, in = ParameterIn.HEADER)
  })
  @ResponseBody
  public ResponseEntity deleteMapping(
          @Parameter(description = "The schema linked to the mapping.", required = true) @PathVariable(value = "mappingId") String mappingId,
          @Parameter(description = "The type of the mapping.", required = true) @PathVariable(value = "mappingType") String mappingType,
          WebRequest wr,
          HttpServletResponse hsr);
}
