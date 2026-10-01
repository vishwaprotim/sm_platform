package com.protim.service.user.api;

import com.protim.service.user.dto.AddressDto;
import com.protim.service.user.dto.UserProfileDto;
import com.protim.service.user.dto.UserProfileUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Tag(name = "User Profile Management", description = "APIs for managing user account profiles")
public interface UserApi {

    @Operation(
            summary = "Create a new user profile",
            description = "Registers a new user profile, hashes their password, validates the inputs, and maps the primary address."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User profile successfully created",
                    content = @Content(schema = @Schema(implementation = UserProfileDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input details or JSON parsing error",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict: Username or Email already exists",
                    content = @Content
            )
    })
    UserProfileDto createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the user profile to be created",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserProfileDto.class),
                    examples = @ExampleObject(
                            name = "Valid User Profile Request",
                            summary = "Example of a fully compliant registration payload",
                            value = """
                                    {
                                      "userName": "johndoe11",
                                      "passwordHash": "mySecurePassword123",
                                      "firstName": "John",
                                      "middleName": "Robert",
                                      "lastName": "Doe",
                                      "dateOfBirth": "15-01-1995",
                                      "contactNumber": "+1-555-0199",
                                      "email": "johndoe@example.com",
                                      "bio": "Software Engineer passionate about backend systems.",
                                      "primaryAddress": {
                                        "addressType": "RESIDENTIAL",
                                        "isPrimary": true,
                                        "addressLine1": "123 Main Street",
                                        "addressLine2": "Apartment 4B",
                                        "addressLine3": "Near Central Park",
                                        "zipCode": "73301",
                                        "city": "Austin",
                                        "state": "Texas",
                                        "country": "USA"
                                      }
                                    }
                                    """
                    )
            )
    ) UserProfileDto user);


    @Operation(
            summary = "Get user names by status",
            description = """
                    Returns a paginated list of user names filtered by status. If no status is provided, returns all users. If an invalid status is provided, returns an empty list.
                    <br>
                    <br>Example: /api/v1/user/ids?status=active&page=2&size=10&sort=id,asc
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the list of user names",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
            )
    })
    Page<String> getAllUserNamesByStatus(
            @Parameter(
                    description = "Filter users by status (e.g., active, inactive). Leave empty to fetch all.",
                    example = "active"
            )
            String status,
            @ParameterObject Pageable pageable);


    @Operation(
            summary = "Get user profile by username",
            description = "Retrieves the full profile details for a specific user using their unique username."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User profile found and retrieved successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User profile not found for the provided username.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))
            )
    })
    UserProfileDto getUser(
            @Parameter(
                    description = "The unique username of the user.",
                    example = "johndoe123",
                    required = true
            ) String userName);


    @Operation(
            summary = "Update user profile",
            description = "Applies partial updates to a user profile by ignoring null values."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully", content = @Content(schema = @Schema(implementation = UserProfileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid validation constraints provided", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    UserProfileDto updateUser(
            @Parameter(description = "The unique username", example = "johndoe11", required = true) String userName,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fields to update", required = true) UserProfileUpdateDto updateDto);


    @Operation(
            summary = "Suspend user account",
            description = "Changes the status of a specific user to SUSPENDED."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully suspended", content = @Content(schema = @Schema(implementation = UserProfileDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    UserProfileDto suspendUser(
            @Parameter(description = "The unique username to suspend", required = true) String userName);


    @Operation(
            summary = "Deactivate user account",
            description = "Changes the status of a specific user to DEACTIVATED."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deactivated", content = @Content(schema = @Schema(implementation = UserProfileDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    UserProfileDto deactivateUser(
            @Parameter(description = "The unique username to deactivate", required = true) String userName);


    @Operation(
            summary = "Restore user account",
            description = "Changes the status of a specific user back to ACTIVE."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully restored to active", content = @Content(schema = @Schema(implementation = UserProfileDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    UserProfileDto restoreUser(
            @Parameter(description = "The unique username to restore", required = true) String userName);


    @Operation(
            summary = "Add new address",
            description = "Adds a new address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address successfully added", content = @Content(schema = @Schema(implementation = AddressDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed for address", content = @Content)
    })
    AddressDto addAddress(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Address details", required = true) AddressDto addressDto);


    @Operation(
            summary = "Get all addresses for user",
            description = "Retrieves all addresses associated with a specific user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved addresses", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AddressDto.class)))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    List<AddressDto> getAddressesForUser(
            @Parameter(description = "The unique username", required = true) String userName);


    @Operation(
            summary = "Get primary address for user",
            description = "Retrieves the primary address designated for a specific user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved primary address", content = @Content(schema = @Schema(implementation = AddressDto.class))),
            @ApiResponse(responseCode = "404", description = "User or address not found", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    AddressDto getPrimaryAddressesForUser(
            @Parameter(description = "The unique username", required = true) String userName);


    @Operation(
            summary = "Delete an address",
            description = "Deletes a specific address belonging to a user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address successfully deleted", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or address not found", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    BaseResponse deleteAddress(
            @Parameter(description = "The unique username", required = true) String userName,
            @Parameter(description = "The ID of the address to delete", required = true) String addressId);


    @Operation(
            summary = "Set address as primary",
            description = "Updates the specified address to become the primary address for the user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address successfully set to primary", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or address not found", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    BaseResponse setPrimary(
            @Parameter(description = "The unique username", required = true) String userName,
            @Parameter(description = "The ID of the address to set as primary", required = true) String addressId);
}