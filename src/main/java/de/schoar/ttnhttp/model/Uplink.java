package de.schoar.ttnhttp.model;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.schoar.ttnhttp.JsonHelper;

@Entity
public class Uplink {

	private static final Logger LOG = LoggerFactory.getLogger(Uplink.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;

	@Lob
	private String json_ttn;

	private String msg_app_id_ttn;
	private String msg_dev_id_ttn;
	private String msg_hardware_serial_ttn;

	private Integer msg_port_ttn;
	private Integer msg_counter_ttn;
	private Boolean msg_is_retry_ttn;

	private String msg_payload_raw_ttn;
	private String msg_payload_hex;
	private byte[] msg_payload_byte;
	private String msg_payload_fields_ttn;

	private String meta_time_ttn;
	private Instant meta_time_converted;

	private Double meta_frequency_ttn;
	private String meta_modulation_ttn;
	private String meta_data_rate_ttn;
	private String meta_coding_rate_ttn;
	private String meta_downlink_url_ttn;

	private Integer gw_count;
	private Integer gw_rssi_best;
	private Integer gw_rssi_worst;
	private Double gw_snr_best;
	private Double gw_snr_worst;

	private Uplink(Date created_at) {
	}

	public static Uplink parse(Date created_at, String json) {
		Uplink uplink = new Uplink(null);

		uplink.created_at = created_at;
		uplink.json_ttn = json;

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn = mapper.readTree(json);

			uplink.msg_app_id_ttn = JsonHelper.toText(jn, "app_id");
			uplink.msg_dev_id_ttn = JsonHelper.toText(jn, "dev_id");
			uplink.msg_hardware_serial_ttn = JsonHelper.toText(jn, "hardware_serial");
			uplink.msg_port_ttn = JsonHelper.toInteger(jn, "port");
			uplink.msg_counter_ttn = JsonHelper.toInteger(jn, "counter");
			uplink.msg_is_retry_ttn = JsonHelper.toBooleanDefault(jn, "is_retry", false);
			uplink.msg_payload_raw_ttn = JsonHelper.toText(jn, "payload_raw");
			if (uplink.msg_payload_raw_ttn != null && uplink.msg_payload_raw_ttn.length() != 0) {
				uplink.msg_payload_byte = Base64.getDecoder().decode(uplink.msg_payload_raw_ttn);
			}
			if (uplink.msg_payload_byte != null && uplink.msg_payload_byte.length != 0) {
				uplink.msg_payload_hex = HexUtils.toHexString(uplink.msg_payload_byte);
			}
			uplink.msg_payload_fields_ttn = JsonHelper.toString(jn, "payload_fields");

			uplink.meta_time_ttn = JsonHelper.toText(jn, "time");
			if (uplink.meta_time_ttn != null && !uplink.meta_time_ttn.isEmpty()) {
				uplink.meta_time_converted = Instant.parse(uplink.meta_time_ttn);
			}
			uplink.meta_frequency_ttn = JsonHelper.toDouble(jn, "frequency");
			uplink.meta_modulation_ttn = JsonHelper.toText(jn, "modulation");
			uplink.meta_data_rate_ttn = JsonHelper.toText(jn, "data_rate");
			uplink.meta_coding_rate_ttn = JsonHelper.toText(jn, "coding_rate");
			uplink.meta_downlink_url_ttn = JsonHelper.toText(jn, "downlink_url");
		} catch (IOException ex) {
			LOG.error("Failed to parse uplink: " + json, ex);
		}

		return uplink;
	}

	public void parse(List<Gateway> gateways) {
		this.gw_count = 0;
		this.gw_rssi_best = null;
		this.gw_rssi_worst = null;
		this.gw_snr_best = null;
		this.gw_snr_worst = null;

		if (gateways == null) {
			return;
		}

		this.gw_count = gateways.size();
		Integer rssi_best = null;
		Integer rssi_worst = null;
		Double snr_best = null;
		Double snr_worst = null;

		for (Gateway gw : gateways) {
			if (rssi_best == null) {
				rssi_best = gw.getRssi_ttn();
			}
			rssi_best = Math.max(rssi_best, gw.getRssi_ttn());

			if (rssi_worst == null) {
				rssi_worst = gw.getRssi_ttn();
			}
			rssi_worst = Math.min(rssi_worst, gw.getRssi_ttn());

			if (snr_best == null) {
				snr_best = gw.getSnr_ttn();
			}
			snr_best = Math.max(snr_best, gw.getSnr_ttn());

			if (snr_worst == null) {
				snr_worst = gw.getSnr_ttn();
			}
			snr_worst = Math.min(snr_worst, gw.getSnr_ttn());
		}

		this.gw_rssi_best = rssi_best;
		this.gw_rssi_worst = rssi_worst;
		this.gw_snr_best = snr_best;
		this.gw_snr_worst = snr_worst;

	}

	public Long getId() {
		return id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public String getJson_ttn() {
		return json_ttn;
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

	public String getMsg_payload_raw_ttn() {
		return msg_payload_raw_ttn;
	}

	public String getMsg_payload_hex() {
		return msg_payload_hex;
	}

	public byte[] getMsg_payload_byte() {
		return msg_payload_byte;
	}

	public String getMsg_payload_fields_ttn() {
		return msg_payload_fields_ttn;
	}

	public String getMeta_time_ttn() {
		return meta_time_ttn;
	}

	public Instant getMeta_time_converted() {
		return meta_time_converted;
	}

	public Double getMeta_frequency_ttn() {
		return meta_frequency_ttn;
	}

	public String getMeta_modulation_ttn() {
		return meta_modulation_ttn;
	}

	public String getMeta_data_rate_ttn() {
		return meta_data_rate_ttn;
	}

	public String getMeta_coding_rate_ttn() {
		return meta_coding_rate_ttn;
	}

	public String getMeta_downlink_url_ttn() {
		return meta_downlink_url_ttn;
	}

	public Integer getGw_count() {
		return gw_count;
	}

	public Integer getGw_rssi_best() {
		return gw_rssi_best;
	}

	public Integer getGw_rssi_worst() {
		return gw_rssi_worst;
	}

	public Double getGw_snr_best() {
		return gw_snr_best;
	}

	public Double getGw_snr_worst() {
		return gw_snr_worst;
	}

}
