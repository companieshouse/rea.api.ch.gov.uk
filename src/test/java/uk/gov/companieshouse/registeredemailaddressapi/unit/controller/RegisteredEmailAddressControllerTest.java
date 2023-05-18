package uk.gov.companieshouse.registeredemailaddressapi.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.validationstatus.ValidationStatusResponse;
import uk.gov.companieshouse.registeredemailaddressapi.controller.RegisteredEmailAddressController;
import uk.gov.companieshouse.registeredemailaddressapi.exception.ServiceException;
import uk.gov.companieshouse.registeredemailaddressapi.exception.SubmissionNotFoundException;
import uk.gov.companieshouse.registeredemailaddressapi.model.dto.RegisteredEmailAddressDTO;
import uk.gov.companieshouse.registeredemailaddressapi.service.RegisteredEmailAddressService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisteredEmailAddressControllerTest {

    private RegisteredEmailAddressDTO registeredEmailAddressDTO;
    private static final ResponseEntity<Object> ERROR_RESPONSE = ResponseEntity.internalServerError().build();
    private static final String REQUEST_ID = UUID.randomUUID().toString();
    private static final String TRANSACTION_ID = UUID.randomUUID().toString();
    private static final String USER_ID = UUID.randomUUID().toString();
    private static final String EMAIL_ADDRESS = "Test@Test.com";
    private static final String UNEXPECTED_ERROR = "UNEXPCTED ERROR - EXITING...";

    @Mock
    private RegisteredEmailAddressService registeredEmailAddressService;

    @Mock
    private Transaction transaction;

    @InjectMocks
    private RegisteredEmailAddressController registeredEmailAddressController;

    @BeforeEach
    void init() {
        registeredEmailAddressDTO = new RegisteredEmailAddressDTO();
        registeredEmailAddressDTO.setRegisteredEmailAddress(EMAIL_ADDRESS);
    }

    @Test
    void testCreateRegisteredEmailAddressSuccessTest() throws ServiceException {

        registeredEmailAddressDTO.setId(UUID.randomUUID().toString());

        when(this.registeredEmailAddressService.createRegisteredEmailAddress(
            transaction,
            registeredEmailAddressDTO,
            REQUEST_ID,
            USER_ID)
        ).thenReturn(registeredEmailAddressDTO);

        var createRegisteredEmailAddressResponse = registeredEmailAddressController.createRegisteredEmailAddress(
            transaction,
            registeredEmailAddressDTO,
            REQUEST_ID,
            USER_ID
        );

        assertEquals(HttpStatus.CREATED.value(), createRegisteredEmailAddressResponse.getStatusCodeValue());
        assertEquals(registeredEmailAddressDTO, createRegisteredEmailAddressResponse.getBody());

        verify(registeredEmailAddressService).createRegisteredEmailAddress(
                transaction,
                registeredEmailAddressDTO,
                REQUEST_ID,
                USER_ID);
    }

    @Test
    void testCreateRegisteredEmailAddressErrorTest() throws ServiceException {
        when(this.registeredEmailAddressService.createRegisteredEmailAddress(
            transaction,
            registeredEmailAddressDTO,
            REQUEST_ID,
            USER_ID)
        ).thenThrow(new RuntimeException(UNEXPECTED_ERROR));

        var createRegisteredEmailAddressResponse = registeredEmailAddressController.createRegisteredEmailAddress(
            transaction,
            registeredEmailAddressDTO,
            REQUEST_ID,
            USER_ID
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), createRegisteredEmailAddressResponse.getStatusCodeValue());
        assertEquals(ERROR_RESPONSE, createRegisteredEmailAddressResponse);

        verify(registeredEmailAddressService).createRegisteredEmailAddress(
                transaction,
                registeredEmailAddressDTO,
                REQUEST_ID,
                USER_ID);
    }

    @Test
    void testGetValidationStatusTest() throws SubmissionNotFoundException {
        ValidationStatusResponse validationStatusResponse = new ValidationStatusResponse();
        validationStatusResponse.setValid(true);

        registeredEmailAddressDTO.setId(UUID.randomUUID().toString());

        when(this.registeredEmailAddressService
                        .getValidationStatus(TRANSACTION_ID, REQUEST_ID)).thenReturn(validationStatusResponse);

        var response = registeredEmailAddressController.getValidationStatus(
                TRANSACTION_ID,
                REQUEST_ID
        );

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(validationStatusResponse, response.getBody());

        verify(registeredEmailAddressService).getValidationStatus(
                TRANSACTION_ID, REQUEST_ID);
    }

}