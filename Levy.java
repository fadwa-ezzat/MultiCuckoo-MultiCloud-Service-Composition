
import ec.util.MersenneTwisterFast;

/**
 * 
 * @author Fadwa Ezzat This class uses the Mason library to simulate the Levy
 *         flight of cuckoo
 */
public class Levy {
	private static MersenneTwisterFast rng = new MersenneTwisterFast();

	private static double bounded_uniform(double low, double high) {
		double x = rng.nextDouble(false, false);

		double range = high - low;
		x *= range;
		x += low;

		return x;
	}

	public static double sample(double mu) {
		double X = bounded_uniform(-1.570796326794897D, 1.570796326794897D);

		double Y = -Math.log(rng.nextDouble(false, false));
		double alpha = mu - 1.0D;

		double Z = Math.sin(alpha * X) / Math.pow(Math.cos(X), 1.0D / alpha)
				* Math.pow(Math.cos((1.0D - alpha) * X) / Y, (1.0D - alpha) / alpha);
		return Z;
	}

	public static double sample_positive(double mu, double scale) {
		double l = sample(mu) * scale;
		if (l < 0.0D) {
			return -1.0D * l;
		}
		return l;
	}

	public static double sample_positive(double mu) {
		return sample_positive(mu, 1.0D);
	}
}
