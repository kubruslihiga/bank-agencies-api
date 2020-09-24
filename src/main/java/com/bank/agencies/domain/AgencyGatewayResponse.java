package com.bank.agencies.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgencyGatewayResponse implements Serializable {

    private static final long serialVersionUID = -1491071762041597641L;

    @JsonProperty("NomeAgencia")
    private String name;
    @JsonProperty("NomeIf")
    private String bank;
    @JsonProperty("Municipio")
    private String city;
    @JsonProperty("UF")
    private String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public AgencyResponse getResponse() {
    	return AgencyResponse.AgencyResponseBuilder.anAgencyResponse()
        .bank(this.getBank())
        .city(this.getCity())
        .name(this.getName())
        .state(this.getState()).build();
    }
    public static final class AgencyGatewayResponseBuilder {
        private String name;
        private String bank;
        private String city;
        private String state;

        private AgencyGatewayResponseBuilder() {
        }

        public static AgencyGatewayResponseBuilder anAgency() {
            return new AgencyGatewayResponseBuilder();
        }

        public AgencyGatewayResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AgencyGatewayResponseBuilder bank(String bank) {
            this.bank = bank;
            return this;
        }

        public AgencyGatewayResponseBuilder city(String city) {
            this.city = city;
            return this;
        }

        public AgencyGatewayResponseBuilder state(String state) {
            this.state = state;
            return this;
        }

        public AgencyGatewayResponse build() {
            AgencyGatewayResponse agency = new AgencyGatewayResponse();
            agency.setName(name);
            agency.setBank(bank);
            agency.setCity(city);
            agency.setState(state);
            return agency;
        }
    }
}
