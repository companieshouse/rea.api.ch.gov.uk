package uk.gov.companieshouse.registeredemailaddressapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class RegisteredEmailAddressDTO {
    @NotBlank(message = "registered_email_address must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~\\-]+@([^.@][^@\\s]+)$",
            message ="registered_email_address must have a valid email format" )
    @JsonProperty("registered_email_address")
    @Valid
    private String registeredEmailAddress;
    @JsonProperty("accept_appropriate_email_address_statement")
    private boolean acceptAppropriateEmailAddressStatement;

    public String getRegisteredEmailAddress() {
        return registeredEmailAddress;
    }

    public void setRegisteredEmailAddress(String registeredEmailAddress) {
        this.registeredEmailAddress = registeredEmailAddress;
    }

    public boolean isAcceptAppropriateEmailAddressStatement() {
        return acceptAppropriateEmailAddressStatement;
    }

    public void setAcceptAppropriateEmailAddressStatement(boolean acceptAppropriateEmailAddressStatement) {
        this.acceptAppropriateEmailAddressStatement = acceptAppropriateEmailAddressStatement;
    }
}

