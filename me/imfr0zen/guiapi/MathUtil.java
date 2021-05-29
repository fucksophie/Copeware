package me.imfr0zen.guiapi;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathUtil {
	
	private MathUtil() {}

	public static float round(double d, double round) {
		return new BigDecimal(d).setScale((int) round, RoundingMode.HALF_EVEN).floatValue();
	}
    
    public static double round(double f, int i) {
		return new BigDecimal(f).setScale(i, RoundingMode.HALF_EVEN).doubleValue();
	}	
	
}
