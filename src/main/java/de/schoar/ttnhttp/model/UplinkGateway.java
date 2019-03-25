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

@Entity
public class UplinkGateway {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long uplinkGatewayId;

	@OneToOne
	private Uplink uplink;

	@OneToOne
	private UplinkDetails uplinkDetails;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	private String ttn_gtw_id;

	private Long ttn_timestamp;
	private String ttn_time;
	private Instant time_converted;

	private Integer ttn_channel;
	private Integer ttn_rssi;
	private Double ttn_snr;

	private Double ttn_latitude;
	private Double ttn_longitude;
	private String ttn_location_source;

	public UplinkGateway() {
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

	public String getTtn_gtw_id() {
		return ttn_gtw_id;
	}

	public void setTtn_gtw_id(String ttn_gtw_id) {
		this.ttn_gtw_id = ttn_gtw_id;
	}

	public Long getTtn_timestamp() {
		return ttn_timestamp;
	}

	public void setTtn_timestamp(Long ttn_timestamp) {
		this.ttn_timestamp = ttn_timestamp;
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

	public Integer getTtn_channel() {
		return ttn_channel;
	}

	public void setTtn_channel(Integer ttn_channel) {
		this.ttn_channel = ttn_channel;
	}

	public Integer getTtn_rssi() {
		return ttn_rssi;
	}

	public void setTtn_rssi(Integer ttn_rssi) {
		this.ttn_rssi = ttn_rssi;
	}

	public Double getTtn_snr() {
		return ttn_snr;
	}

	public void setTtn_snr(Double ttn_snr) {
		this.ttn_snr = ttn_snr;
	}

	public Double getTtn_latitude() {
		return ttn_latitude;
	}

	public void setTtn_latitude(Double ttn_latitude) {
		this.ttn_latitude = ttn_latitude;
	}

	public Double getTtn_longitude() {
		return ttn_longitude;
	}

	public void setTtn_longitude(Double ttn_longitude) {
		this.ttn_longitude = ttn_longitude;
	}

	public String getTtn_location_source() {
		return ttn_location_source;
	}

	public void setTtn_location_source(String ttn_location_source) {
		this.ttn_location_source = ttn_location_source;
	}

	public Long getUplinkGatewayId() {
		return uplinkGatewayId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Instant getTime_converted() {
		return time_converted;
	}

}
