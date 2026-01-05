package org.creatorledger.reporting.application;

import org.creatorledger.reporting.domain.TaxYear;
import org.creatorledger.user.api.UserId;

public record GenerateTaxYearSummaryCommand(
    UserId userId,
    TaxYear taxYear
) {

    public GenerateTaxYearSummaryCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (taxYear == null) {
            throw new IllegalArgumentException("Tax year cannot be null");
        }
    }
}
