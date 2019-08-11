package de.schoar.ttnhttp.model;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
public class Data {

	private static final Logger LOG = LoggerFactory.getLogger(Data.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	@JsonIgnore
	private Long id;

	@OneToOne
	private Uplink uplink;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;

	private String meta_time_ttn;
	private Instant meta_time_converted;

	private String msg_app_id_ttn;
	private String msg_dev_id_ttn;
	private String msg_hardware_serial_ttn;

	private Integer msg_port_ttn;
	private Integer msg_counter_ttn;
	private Boolean msg_is_retry_ttn;

	private String data_name;
	private Integer data_int;
	private Double data_double;
	private String data_text;

	private Data(Uplink uplink) {
		this.created_at = uplink.getCreated_at();
		this.uplink = uplink;

		this.msg_app_id_ttn = uplink.getMsg_app_id_ttn();
		this.msg_dev_id_ttn = uplink.getMsg_dev_id_ttn();
		this.msg_hardware_serial_ttn = uplink.getMsg_hardware_serial_ttn();
		this.msg_port_ttn = uplink.getMsg_port_ttn();
		this.msg_counter_ttn = uplink.getMsg_counter_ttn();
		this.msg_is_retry_ttn = uplink.getMsg_is_retry_ttn();

		this.meta_time_ttn = uplink.getMeta_time_ttn();
		this.meta_time_converted = uplink.getMeta_time_converted();
	}

	public static List<Data> parse(Uplink uplink) {
		List<Data> datas = new LinkedList<Data>();

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn = mapper.readTree(uplink.getMsg_payload_fields_ttn());

			Iterator<Entry<String, JsonNode>> it = jn.fields();
			while (it.hasNext()) {
				Entry<String, JsonNode> entry = it.next();
				String key = (String) entry.getKey();
				JsonNode value = entry.getValue();

				Data data = new Data(uplink);
				data.data_name = key;
				if (value.isInt()) {
					data.data_int = value.asInt();
					data.data_double = Integer.valueOf(value.asInt()).doubleValue();
					data.data_text = String.valueOf(value.asInt());
				}
				if (value.isDouble()) {
					data.data_int = Double.valueOf(value.asDouble()).intValue();
					data.data_double = value.asDouble();
					data.data_text = String.valueOf(value.asDouble());
				}
				if (value.isTextual()) {
					data.data_text = value.asText();
				}
				datas.add(data);
			}
		} catch (IOException ex) {
			LOG.error("Failed to parse data: " + uplink.getMsg_payload_fields_ttn(), ex);
		}

		return datas;
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

	public String getMeta_time_ttn() {
		return meta_time_ttn;
	}

	public Instant getMeta_time_converted() {
		return meta_time_converted;
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

	public String getData_name() {
		return data_name;
	}

	public Integer getData_int() {
		return data_int;
	}

	public Double getData_double() {
		return data_double;
	}

	public String getData_text() {
		return data_text;
	}

}
