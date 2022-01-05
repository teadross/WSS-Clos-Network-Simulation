package general;

import java.math.BigDecimal;

public class Constant {	
	public final static int N = 15;
	public static int M = 10;
	public static int M_TWC = 0;
	public final static int R = 15;
	public final static int W = 5;
	
	public final static int x = 5;// x*N ≈ M*W 由于 x <= W, 因此必须N >= M
	
	public final static double WaveCap = 	1.0;
	public final static double TrafficCap = 1.0;
		
	public final static int Undo = 		0;
	public final static int Doing = 	1;
	public final static int Done = 		2;
	public final static int Blocked = 	3;

	public final static int NOT = 		1;
	public final static int MBlock = 	2;
	public final static int MPass = 	3;


    public final static double rho = 1.0;
    public final static double Rho = CalculateCarriedLoad();
    
//	public static double lambda = 14.0 / (N * R);
	public static double lambda = 1.5;
	public final static double mu = 	1.0 / 1.0;


	public final static int TrafficNum = 10000;
	
	public final static int Arrival = 0;
	public final static int Leave = 1;
	public final static int Nothing = 2;


    private static double CalculateCarriedLoad(){
        BigDecimal rhoI = BigDecimal.valueOf(rho).multiply(BigDecimal.valueOf(1.0).subtract(Erlang(rho,x)));
        BigDecimal rhoE = rhoI.multiply(BigDecimal.valueOf(1.0).subtract(Erlang(rhoI.doubleValue(),x)));
//        BigDecimal rhoRoute = rhoE.divide(BigDecimal.valueOf(M*R),15,BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(N));
        BigDecimal rhoRoute = rhoE;

//        System.out.println("Port Carried Load: rho = "+rhoE.doubleValue()+".");
//        System.out.println("Network Carried Load: rho = "+rhoRoute.doubleValue()+".");
        return rhoRoute.doubleValue();
    }

    private static BigDecimal Erlang(double rho, int device){
        BigDecimal rhox = BigDecimal.valueOf(Math.pow(rho,device));
        BigDecimal xfac = getFactorial(device);
        BigDecimal rhox_xfac = rhox.divide(xfac,15,BigDecimal.ROUND_HALF_DOWN);
        BigDecimal sum = BigDecimal.valueOf(0.0);
        for (int k = 0; k <= device; k++) {
            BigDecimal rhox_tmp = BigDecimal.valueOf(Math.pow(rho,k));
            BigDecimal xfac_tmp = getFactorial(k);
            BigDecimal rhox_xfac_tmp = rhox_tmp.divide(xfac_tmp,15,BigDecimal.ROUND_HALF_DOWN);
            sum = sum.add(rhox_xfac_tmp);
        }
        return rhox_xfac.divide(sum,15,BigDecimal.ROUND_HALF_DOWN);
    }

    private static BigDecimal getFactorial(double number) {
        if (number <= 1)
            return BigDecimal.valueOf(1);
        else
            return BigDecimal.valueOf(number).multiply(getFactorial(number - 1));
    }

    private static BigDecimal getFactorial(double number1, double number2) {
        return (getFactorial(number1)).divide(getFactorial(number2),15,BigDecimal.ROUND_HALF_DOWN);
    }
	
}
