package xyz.codevomit.demostreamer.conf;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author massa
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "xyz.codevomit.demostreamer.javascript")
public class JavaScriptConstants implements Serializable {

	private static final long serialVersionUID = 3733023647934975214L;

	
	/* From battlefield3d.js
	 *     var BULLET_VELOCITY_SCALE_FACTOR = 40;
	 *     var PAWN_MASS = 1;
	 *     var BULLET_MASS = 10;
	 */
	
	@JsonProperty("BULLET_VELOCITY_SCALE_FACTOR")
	private BigDecimal bulletVelocityScaleFactor;
	
	@JsonProperty("PAWN_MASS")
	private BigDecimal pawnMass;
	
	@JsonProperty("BULLET_MASS")
	private BigDecimal bulletMass;
	
	
	/* From battlefieldMain.js
	 * 		var SYNC_TIMEOUT_MILLIS = 2000;
	 */
	
	@JsonProperty("SYNC_TIMEOUT_MILLIS")
	private int syncTimeoutMillis;
	
	
    @Value("${server.contextPath:}")
    @JsonProperty("SERVER_CONTEXT_PATH")
    private String serverContextPath;
	
}
