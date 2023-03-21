package ee.taltech.inbankbackend.endpoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds the request data of the REST endpoint
 */
@Getter
@Setter
@AllArgsConstructor
public class DecisionRequest {
    private final Long personalCode;
    private final Long loanAmount;
    private final int loanPeriod;
}
