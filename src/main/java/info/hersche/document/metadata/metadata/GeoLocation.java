/**
 * 
 */
package info.hersche.document.metadata.metadata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author herscju
 */
@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true, builderMethodName = "toBuilder")
public class GeoLocation
{
	private final static Pattern DMS_PATTERN = Pattern
			.compile("(-?)([0-9]{1,2})°([0-5]?[0-9])'([0-5]?[0-9])\"([NS])\\s*(-?)([0-1]?[0-9]{1,2})°([0-5]?[0-9])'([0-5]?[0-9])\"([EW])");

	@Builder.Default
	private Boolean configured = Boolean.FALSE;

	/**
	 * Required / optional values
	 */
	private String latitudeRef;
	private String latitudeVal;
	private String longitudeRef;
	private String longitudeVal;
	private String altitudeRef;
	private String altitudeVal;
	
	/**
	 * Default constructor
	 */
	public GeoLocation()
	{
		// Nothing to do here
	}

	public String toDmsCoordinates()
	{
		if (!this.configured.booleanValue())
		{
			return "";
		}
	
		
		return new StringBuilder().append(this.latitudeVal).append(this.latitudeRef).append(" ").append(this.longitudeVal).append(this.longitudeRef).toString();
	}

	/**
	 * @param dms
	 * @return
	 */
	public double[] convertDegreeToDecimalCoordinates(String dms)
	{
		Matcher matcher = DMS_PATTERN.matcher(dms.trim());

		if (matcher.matches())
		{
			double latitude = this.toDouble(matcher, 0);
			double longitude = this.toDouble(matcher, 5);

			if ((Math.abs(latitude) > 90) || (Math.abs(longitude) > 180))
			{
				throw new NumberFormatException("Invalid latitude or longitude!");
			}

			return new double[] { latitude, longitude };
		}
		else
		{
			throw new NumberFormatException("Malformed degrees/minutes/seconds/direction coordinates! DMS is '" + dms + "'." );
		}
	}

	/**
	 * @param matcher
	 * @param offset
	 * @return
	 */
	private double toDouble(Matcher matcher, int offset)
	{
		int sign = "".equals(matcher.group(1 + offset)) ? 1 : -1;
		double degrees = Double.parseDouble(matcher.group(2 + offset));
		double minutes = Double.parseDouble(matcher.group(3 + offset));
		double seconds = Double.parseDouble(matcher.group(4 + offset));
		int direction = "NE".contains(matcher.group(5 + offset)) ? 1 : -1;

		return sign * direction * (degrees + minutes / 60 + seconds / 3600);
	}
}
