package com.compareglobal.service.loans.personal.logic.fi;

/**
 * Created by Luis Miguel Osorio.
 */
public class PLMath {
    /**
     * @param nper The number of periods over which the loan or investment is to be paid
     * @param pmt  The (fixed) payment amount per period
     * @param pv   The present value of the loan / investment
     * @return
     */
    public static double rate(final double nper, final double pmt, final double pv) {

        final double error = 0.0000001;
        double high = 1.00;
        double low = 0.00;

        double rate = (2.0 * (nper * pmt - pv)) / (pv * nper);

        while (true) {
            // check for error margin
            double calc = Math.pow(1 + rate, nper);
            calc = (rate * calc) / (calc - 1.0);
            calc -= pmt / pv;

            if (calc > error) {
                // guess too high, lower the guess
                high = rate;
                rate = (high + low) / 2;
            } else if (calc < -error) {
                // guess too low, higher the guess
                low = rate;
                rate = (high + low) / 2;
            } else {
                // acceptable guess
                break;
            }
        }

        return rate;
    }
}
