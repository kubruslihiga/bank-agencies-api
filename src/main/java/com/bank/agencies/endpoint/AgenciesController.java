    package com.bank.agencies.endpoint;

    import com.bank.agencies.domain.AgencyGatewayResponse;
    import com.bank.agencies.domain.AgencyResponse;
    import com.bank.agencies.usecase.FindAllAgenciesUseCase;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

    @RestController
    @RequestMapping(value = "/agencies", produces = MediaType.APPLICATION_JSON_VALUE)
    public class AgenciesController {

        private final FindAllAgenciesUseCase findAllAgenciesUseCase;

        public AgenciesController(FindAllAgenciesUseCase findAllAgenciesUseCase) {
            this.findAllAgenciesUseCase = findAllAgenciesUseCase;
        }

        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<List<AgencyResponse>> findAllAgencies() {

            List<AgencyGatewayResponse> agencies = findAllAgenciesUseCase.execute();

            List<AgencyResponse> agencyResponse = agencies.stream()
                    .map(agencyGateway -> AgencyResponse.AgencyResponseBuilder.anAgencyResponse()
                    .bank(agencyGateway.getBank())
                    .city(agencyGateway.getCity())
                    .name(agencyGateway.getName())
                    .state(agencyGateway.getState()).build())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(agencyResponse, HttpStatus.OK);
        }
        
        @GetMapping("/uf")
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<Map<String, List<AgencyResponse>>> findAgenciesGroupbedByUf() {

            List<AgencyGatewayResponse> agencies = findAllAgenciesUseCase.execute();

            Map<String, List<AgencyResponse>> collected = agencies.stream().sorted((a1, a2) -> a1.getCity().compareTo(a2.getCity()))
            		.collect(Collectors.groupingBy(a -> a.getState(), Collectors.mapping(AgencyGatewayResponse::getResponse, Collectors.toList())));

            return new ResponseEntity<>(collected, HttpStatus.OK);
        }
    }
