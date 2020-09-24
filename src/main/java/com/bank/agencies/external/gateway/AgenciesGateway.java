package com.bank.agencies.external.gateway;

import java.util.List;

import com.bank.agencies.domain.AgencyGatewayResponse;

public interface AgenciesGateway {
    List<AgencyGatewayResponse> findAllAgencies();
    
    List<AgencyGatewayResponse> findAsyncAgenciesByUf();
}
