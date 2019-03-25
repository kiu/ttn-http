package de.schoar.ttnhttp.model;

import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.tomcat.util.buf.HexUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UplinkDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	@JsonIgnore
	private Long uplinkDetailsId;

	@OneToOne
	private Uplink uplink;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	private String ttn_app_id;
	private String ttn_dev_id;
	private String ttn_hardware_serial;

	private Integer ttn_counter;
	private Boolean ttn_is_retry;

	private Integer ttn_port;
	private String ttn_payload_raw;
	private String payload_raw_converted_hex;
	private byte[] payload_raw_converted_byte;
	private String ttn_payload_fields;

	private String ttn_time;
	private Instant time_converted;

	private Double ttn_frequency;
	private String ttn_modulation;
	private String ttn_data_rate;

	private String ttn_downlink_url;

	public UplinkDetails() {
		this.createdAt = Date.from(Instant.now(Clock.systemUTC()));
	}

	public Long getUplinkDetailsId() {
		return uplinkDetailsId;
	}

	public void setUplinkDetailsId(Long uplinkDetailsId) {
		this.uplinkDetailsId = uplinkDetailsId;
	}

	public Uplink getUplink() {
		return uplink;
	}

	public void setUplink(Uplink uplink) {
		this.uplink = uplink;
	}

	public String getTtn_app_id() {
		return ttn_app_id;
	}

	public void setTtn_app_id(String ttn_app_id) {
		this.ttn_app_id = ttn_app_id;
	}

	public String getTtn_dev_id() {
		return ttn_dev_id;
	}

	public void setTtn_dev_id(String ttn_dev_id) {
		this.ttn_dev_id = ttn_dev_id;
	}

	public String getTtn_hardware_serial() {
		return ttn_hardware_serial;
	}

	public void setTtn_hardware_serial(String ttn_hardware_serial) {
		this.ttn_hardware_serial = ttn_hardware_serial;
	}

	public Integer getTtn_counter() {
		return ttn_counter;
	}

	public void setTtn_counter(Integer ttn_counter) {
		this.ttn_counter = ttn_counter;
	}

	public Boolean getTtn_is_retry() {
		return ttn_is_retry;
	}

	public void setTtn_is_retry(Boolean ttn_is_retry) {
		this.ttn_is_retry = ttn_is_retry;
	}

	public Integer getTtn_port() {
		return ttn_port;
	}

	public void setTtn_port(Integer ttn_port) {
		this.ttn_port = ttn_port;
	}

	public String getTtn_payload_raw() {
		return ttn_payload_raw;
	}

	public void setTtn_payload_raw(String ttn_payload_raw) {
		this.ttn_payload_raw = ttn_payload_raw;
		if (ttn_payload_raw == null) {
			return;
		}
		payload_raw_converted_byte = Base64.getDecoder().decode(ttn_payload_raw);
		payload_raw_converted_hex = HexUtils.toHexString(payload_raw_converted_byte);
	}

	public String getTtn_payload_fields() {
		return ttn_payload_fields;
	}

	public void setTtn_payload_fields(String ttn_payload_fields) {
		this.ttn_payload_fields = ttn_payload_fields;
	}

	public String getTtn_time() {
		return ttn_time;
	}

	public void setTtn_time(String ttn_time) {
		this.ttn_time = ttn_time;
		if (ttn_time != null && !ttn_time.isEmpty()) {
			this.time_converted = Instant.parse(ttn_time);
		}
	}

	public Double getTtn_frequency() {
		return ttn_frequency;
	}

	public void setTtn_frequency(Double ttn_frequency) {
		this.ttn_frequency = ttn_frequency;
	}

	public String getTtn_modulation() {
		return ttn_modulation;
	}

	public void setTtn_modulation(String ttn_modulation) {
		this.ttn_modulation = ttn_modulation;
	}

	public String getTtn_data_rate() {
		return ttn_data_rate;
	}

	public void setTtn_data_rate(String ttn_data_rate) {
		this.ttn_data_rate = ttn_data_rate;
	}

	public String getTtn_downlink_url() {
		return ttn_downlink_url;
	}

	public void setTtn_downlink_url(String ttn_downlink_url) {
		this.ttn_downlink_url = ttn_downlink_url;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public String getPayload_raw_converted_hex() {
		return payload_raw_converted_hex;
	}

	public byte[] getPayload_raw_converted_byte() {
		return payload_raw_converted_byte;
	}

	public Instant getTime_converted() {
		return time_converted;
	}

}
