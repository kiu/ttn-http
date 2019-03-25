package de.schoar.ttnhttp.model;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Data {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	@JsonIgnore
	private Long dataId;

	@OneToOne
	private Uplink uplink;

	@OneToOne	
	private UplinkDetails uplinkDetails;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	private String ttn_app_id;
	private String ttn_dev_id;
	private String ttn_hardware_serial;

	private Boolean ttn_is_retry;

	private Instant time_converted;

	private Integer ttn_port;

	private String name;
	private Integer value_int;
	private Double value_double;
	private String value_text;

	public Data() {
		this.createdAt = Date.from(Instant.now(Clock.systemUTC()));
	}

	public Uplink getUplink() {
		return uplink;
	}

	public void setUplink(Uplink uplink) {
		this.uplink = uplink;
	}

	public UplinkDetails getUplinkDetails() {
		return uplinkDetails;
	}

	public void setUplinkDetails(UplinkDetails uplinkDetails) {
		this.uplinkDetails = uplinkDetails;
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

	public Boolean getTtn_is_retry() {
		return ttn_is_retry;
	}

	public void setTtn_is_retry(Boolean ttn_is_retry) {
		this.ttn_is_retry = ttn_is_retry;
	}

	public Instant getTime_converted() {
		return time_converted;
	}

	public void setTime_converted(Instant time_converted) {
		this.time_converted = time_converted;
	}

	public Integer getTtn_port() {
		return ttn_port;
	}

	public void setTtn_port(Integer ttn_port) {
		this.ttn_port = ttn_port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue_int() {
		return value_int;
	}

	public void setValue_int(Integer value_int) {
		this.value_int = value_int;
	}

	public Double getValue_double() {
		return value_double;
	}

	public void setValue_double(Double value_double) {
		this.value_double = value_double;
	}

	public String getValue_text() {
		return value_text;
	}

	public void setValue_text(String value_text) {
		this.value_text = value_text;
	}

	public Long getDataId() {
		return dataId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

}
