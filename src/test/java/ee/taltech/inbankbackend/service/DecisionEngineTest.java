package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DecisionEngineTest {

    @InjectMocks
    private DecisionEngine decisionEngine;

    private Long debtorPersonalCode;
    private Long segment1PersonalCode;
    private Long segment2PersonalCode;
    private Long segment3PersonalCode;

    @BeforeEach
    void setUp() {
        debtorPersonalCode = 37605030299L;
        segment1PersonalCode = 50307172740L;
        segment2PersonalCode = 38411266610L;
        segment3PersonalCode = 35006069515L;
    }

    @Test
    void testDebtorPersonalCode() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(debtorPersonalCode, 4000L, 12));
    }

    @Test
    void testSegment1PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, 12);
        assertEquals(2000, decision.getLoanAmount());
        assertEquals(20, decision.getLoanPeriod());
    }

    @Test
    void testSegment2PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 4000L, 12);
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testSegment3PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment3PersonalCode, 4000L, 12);
        assertEquals(10000, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testInvalidPersonalCode() {
        Long invalidPersonalCode = 12345678901L;
        assertThrows(InvalidPersonalCodeException.class,
                () -> decisionEngine.calculateApprovedLoan(invalidPersonalCode, 4000L, 12));
    }

    @Test
    void testInvalidLoanAmount() {
        Long tooLowLoanAmount = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT - 1L;
        Long tooHighLoanAmount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT + 1L;

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, tooLowLoanAmount, 12));

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, tooHighLoanAmount, 12));
    }

    @Test
    void testInvalidLoanPeriod() {
        int tooShortLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD - 1;
        int tooLongLoanPeriod = DecisionEngineConstants.MAXIMUM_LOAN_PERIOD + 1;

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, tooShortLoanPeriod));

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000L, tooLongLoanPeriod));
    }

    @Test
    void testFindSuitableLoanPeriod() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 2000L, 12);
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testNoValidLoanFound() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(debtorPersonalCode, 10000L, 60));
    }

}

