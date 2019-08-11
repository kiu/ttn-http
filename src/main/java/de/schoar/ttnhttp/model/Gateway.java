package de.schoar.ttnhttp.model;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.schoar.ttnhttp.JsonHelper;

@Entity
public class Gateway {

	private static final Logger LOG = LoggerFactory.getLogger(Gateway.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@OneToOne
	private Uplink uplink;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;

	private String gtw_id_ttn;

	private Long timestamp_ttn;
	private String time_ttn;
	private Instant time_converted;

	private Integer channel_ttn;
	private Integer rssi_ttn;
	private Double snr_ttn;
	private Integer rf_chain_ttn;

	private Double latitude_ttn;
	private Double longitude_ttn;
	private String location_source_ttn;

	private String msg_app_id_ttn;
	private String msg_dev_id_ttn;
	private String msg_hardware_serial_ttn;

	private Integer msg_port_ttn;
	private Integer msg_counter_ttn;
	private Boolean msg_is_retry_ttn;

	public Gateway(Uplink uplink) {
		this.created_at = uplink.getCreated_at();
		this.uplink = uplink;

		this.msg_app_id_ttn = uplink.getMsg_app_id_ttn();
		this.msg_dev_id_ttn = uplink.getMsg_dev_id_ttn();
		this.msg_hardware_serial_ttn = uplink.getMsg_hardware_serial_ttn();
		this.msg_port_ttn = uplink.getMsg_port_ttn();
		this.msg_counter_ttn = uplink.getMsg_counter_ttn();
		this.msg_is_retry_ttn = uplink.getMsg_is_retry_ttn();
	}

	public static List<Gateway> parse(Uplink uplink) {
		List<Gateway> gateways = new LinkedList<Gateway>();

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn = mapper.readTree(uplink.getJson_ttn());

			JsonNode gws = jn.findValue("gateways");
			if (gws == null || gws.isNull()) {
				LOG.debug("Gateways are empty: " + uplink.getJson_ttn());
				return gateways;
			}

			Iterator<JsonNode> it = gws.elements();
			while (it.hasNext()) {
				jn = it.next();

				Gateway gw = new Gateway(uplink);

				gw.gtw_id_ttn = JsonHelper.toText(jn, "gtw_id");

				gw.timestamp_ttn = JsonHelper.toLong(jn, "timestamp");
				gw.time_ttn = JsonHelper.toText(jn, "time");
				if (gw.time_ttn != null && !gw.time_ttn.isEmpty()) {
					gw.time_converted = Instant.parse(gw.time_ttn);
				}

				gw.channel_ttn = JsonHelper.toInteger(jn, "channel");
				gw.rssi_ttn = JsonHelper.toInteger(jn, "rssi");
				gw.snr_ttn = JsonHelper.toDouble(jn, "snr");
				gw.rf_chain_ttn = JsonHelper.toInteger(jn, "rf_chain");

				gw.latitude_ttn = JsonHelper.toDouble(jn, "latitude");
				gw.longitude_ttn = JsonHelper.toDouble(jn, "longitude");
				gw.location_source_ttn = JsonHelper.toText(jn, "location_source");

				gateways.add(gw);
			}
		} catch (IOException ex) {
			LOG.error("Failed to parse gateways: " + uplink.getJson_ttn(), ex);
		}

		return gateways;
	}

	public Long getId() {
		return id;
	}

	public Uplink getUplink() {
		return uplink;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public String getGtw_id_ttn() {
		return gtw_id_ttn;
	}

	public Long getTimestamp_ttn() {
		return timestamp_ttn;
	}

	public String getTime_ttn() {
		return time_ttn;
	}

	public Instant getTime_converted() {
		return time_converted;
	}

	public Integer getChannel_ttn() {
		return channel_ttn;
	}

	public Integer getRssi_ttn() {
		return rssi_ttn;
	}

	public Double getSnr_ttn() {
		return snr_ttn;
	}

	public Integer getRf_chain_ttn() {
		return rf_chain_ttn;
	}

	public Double getLatitude_ttn() {
		return latitude_ttn;
	}

	public Double getLongitude_ttn() {
		return longitude_ttn;
	}

	public String getLocation_source_ttn() {
		return location_source_ttn;
	}

	public String getMsg_app_id_ttn() {
		return msg_app_id_ttn;
	}

	public String getMsg_dev_id_ttn() {
		return msg_dev_id_ttn;
	}

	public String getMsg_hardware_serial_ttn() {
		return msg_hardware_serial_ttn;
	}

	public Integer getMsg_port_ttn() {
		return msg_port_ttn;
	}

	public Integer getMsg_counter_ttn() {
		return msg_counter_ttn;
	}

	public Boolean getMsg_is_retry_ttn() {
		return msg_is_retry_ttn;
	}

}
