package de.schoar.ttnhttp.model;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Uplink {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long uplinkId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Lob
	private String json;

	public Uplink(String json) {
		this.createdAt = Date.from(Instant.now(Clock.systemUTC()));
		this.json = json;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Long getUplinkId() {
		return uplinkId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

}
