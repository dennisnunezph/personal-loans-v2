package com.compareglobal.service.loans.personal.logic;

/**
 * Created by dennis on 4/28/15.
 */
public class RateCalculation {

    private RateCalculation() {}

    /**
     * A clone of the JavaScript Rate computation
     * @param paymentsPerYear
     * @param paymentAmount
     * @param presentValue
     * @param futureValue
     * @param dueEndOrBeginning
     * @param interest
     * @return
     */
    public static double jsRate(int paymentsPerYear,
                          double paymentAmount,
                          double presentValue,
                          double futureValue,
                          int dueEndOrBeginning,
                          double interest) {

        if (interest == 0){
            interest = 0.01;
        }

        if (futureValue == 0){
            futureValue = 0;
        }

        if (dueEndOrBeginning == 0) {
            dueEndOrBeginning = 0;
        }

        int FINANCIAL_MAX_ITERATIONS = 128;
        double FINANCIAL_PRECISION = 0.0000001;
        double y, y0, y1, x0, x1 = 0, f = 0, i = 0;
        double rate = interest;
        if (Math.abs(rate) < FINANCIAL_PRECISION) {
            y= (presentValue * ( 1+ paymentsPerYear * rate) + paymentAmount * (1 + rate*dueEndOrBeginning) *paymentsPerYear + futureValue);
        } else {
            f = Math.exp((paymentsPerYear * Math.log( 1 + rate)));
            y = presentValue * f + paymentAmount *( 1 / rate + dueEndOrBeginning) * ( f - 1 ) + futureValue;
        }
        y0 = presentValue + paymentAmount * paymentsPerYear + futureValue;
        y1 = presentValue * f + paymentAmount * ( 1 / rate + dueEndOrBeginning ) * ( f - 1) + futureValue;
        i = x0 = 0.0;
        x1 = rate;
        while((Math.abs( y0 - y1 ) > FINANCIAL_PRECISION)
                && (i < FINANCIAL_MAX_ITERATIONS)) {
            rate=( ( y1 * x0 ) - ( y0 * x1 ) ) / ( y1 - y0 );
            x0 = x1;
            x1 = rate;
            if (Math.abs(rate) < FINANCIAL_PRECISION) {
                y = (presentValue * (1 + paymentsPerYear * rate) + paymentAmount * (1 + rate * dueEndOrBeginning) * paymentsPerYear + futureValue);
            } else {
                f = Math.exp((paymentsPerYear * Math.log( 1 + rate)));
                y = presentValue * f + paymentAmount * ( 1 / rate + dueEndOrBeginning) * (f-1) + futureValue;
            }
            y0 = y1;
            y1 = y;
            ++i;
        }
        return rate;
    }
}
