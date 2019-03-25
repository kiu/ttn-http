package de.schoar.ttnhttp;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.schoar.ttnhttp.model.Data;
import de.schoar.ttnhttp.model.DataRepository;
import de.schoar.ttnhttp.model.Uplink;
import de.schoar.ttnhttp.model.UplinkDetails;
import de.schoar.ttnhttp.model.UplinkDetailsRepository;
import de.schoar.ttnhttp.model.UplinkGateway;
import de.schoar.ttnhttp.model.UplinkGatewayRepository;
import de.schoar.ttnhttp.model.UplinkRepository;

@RestController
public class APIController {

	private static final Logger LOG = LoggerFactory.getLogger(APIController.class);

	@Autowired
	private UplinkDetailsRepository uplinkDetailsRepo;

	@Autowired
	private UplinkGatewayRepository uplinkGatewayRepo;

	@Autowired
	private UplinkRepository uplinkRepo;

	@Autowired
	private DataRepository dataRepo;

	// curl -i -H "Accept: application/json" -H "Content-Type:application/json" -X POST --data '{"app_id":"kiu-test","dev_id":"lofence-001","hardware_serial":"0004A30B00245E75","port":1,"counter":35,"payload_raw":"D+cihAAADWcAAA==","payload_fields":{"bat":4071,"fmmax":3431,"fmmin":0,"fpmax":8836,"fpmin":0},"metadata":{"time":"2019-03-24T13:08:00.809651225Z","frequency":868.1,"modulation":"LORA","data_rate":"SF12BW125","coding_rate":"4/5","gateways":[{"gtw_id":"eui-1402fcc23d0f505b","timestamp":1453485412,"time":"2019-03-24T13:08:00.791757Z","channel":0,"rssi":-115,"snr":-13.8,"rf_chain":1,"latitude":48.094894,"longitude":11.505935,"location_source":"registry"}]},"downlink_url":"https://integrations.thethingsnetwork.org/ttn-eu/api/v2/down/kiu-test/kiu-logpush?key=ttn-account-v2.xxxxxxxxx"}' "http://localhost:8080/ttn-http"
	@RequestMapping(value = "/ttn-http", method = RequestMethod.POST, consumes = "application/json")
	public void uplink(@RequestBody String json) {

		Uplink uplink = parseUplink(json);

		UplinkDetails uplinkDetails = parseUplinkDetails(uplink);

		List<UplinkGateway> uplinkGateways = parseUplinkGateways(uplink, uplinkDetails);

		List<Data> datas = parseData(uplink, uplinkDetails);
	}

	private Uplink parseUplink(String json) {
		Uplink uplink = new Uplink(json);
		uplinkRepo.save(uplink);
		LOG.debug("Saved new uplink: " + uplink.getUplinkId());
		return uplink;
	}

	private UplinkDetails parseUplinkDetails(Uplink uplink) {
		if (uplink == null || uplink.getJson() == null || uplink.getJson().isEmpty()) {
			LOG.error("Failed to parse details of uplink as something important is null.");
			return null;
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn = mapper.readTree(uplink.getJson());

			UplinkDetails uplinksDetails = new UplinkDetails();
			uplinksDetails.setUplink(uplink);

			uplinksDetails.setTtn_app_id(toText(jn, "app_id"));
			uplinksDetails.setTtn_dev_id(toText(jn, "dev_id"));
			uplinksDetails.setTtn_hardware_serial(toText(jn, "hardware_serial"));

			uplinksDetails.setTtn_counter(toInteger(jn, "counter"));
			uplinksDetails.setTtn_is_retry(toBooleanDefault(jn, "is_retry", false));

			uplinksDetails.setTtn_port(toInteger(jn, "port"));
			uplinksDetails.setTtn_payload_raw(toText(jn, "payload_raw"));
			uplinksDetails.setTtn_payload_fields(toString(jn, "payload_fields"));

			uplinksDetails.setTtn_time(toText(jn, "time"));

			uplinksDetails.setTtn_frequency(toDouble(jn, "frequency"));
			uplinksDetails.setTtn_modulation(toText(jn, "modulation"));
			uplinksDetails.setTtn_data_rate(toText(jn, "data_rate"));

			uplinksDetails.setTtn_downlink_url(toText(jn, "downlink_url"));

			uplinkDetailsRepo.save(uplinksDetails);
			LOG.debug("Saved new uplink details: " + uplinksDetails.getUplinkDetailsId());
			return uplinksDetails;
		} catch (IOException ex) {
			LOG.error("Failed to parse the uplink details: " + uplink.getJson(), ex);
		}

		return null;
	}

	private List<UplinkGateway> parseUplinkGateways(Uplink uplink, UplinkDetails uplinkDetails) {
		List<UplinkGateway> uplinkGateways = new LinkedList<UplinkGateway>();

		if (uplink == null) {
			LOG.error("Failed to parse gateways as uplink is null.");
			return uplinkGateways;
		}
		if (uplink.getJson() == null || uplink.getJson().isEmpty()) {
			LOG.error("Failed to parse gateways as uplink json is null or empty.");
			return uplinkGateways;
		}
		if (uplinkDetails == null) {
			LOG.error("Failed to parse gateways as uplinkDetails is null.");
			return uplinkGateways;
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn = mapper.readTree(uplink.getJson());

			JsonNode gws = jn.findValue("gateways");
			if (gws == null || gws.isNull()) {
				LOG.debug("Gateways are empty: " + uplink.getJson());
				return uplinkGateways;
			}

			Iterator<JsonNode> it = gws.elements();
			while (it.hasNext()) {
				jn = it.next();

				UplinkGateway gw = new UplinkGateway();

				gw.setUplink(uplink);
				gw.setUplinkDetails(uplinkDetails);

				gw.setTtn_gtw_id(toText(jn, "gtw_id"));

				gw.setTtn_timestamp(toLong(jn, "timestamp"));
				gw.setTtn_time(toText(jn, "time"));

				gw.setTtn_channel(toInteger(jn, "channel"));
				gw.setTtn_rssi(toInteger(jn, "rssi"));
				gw.setTtn_snr(toDouble(jn, "snr"));

				gw.setTtn_latitude(toDouble(jn, "latitude"));
				gw.setTtn_longitude(toDouble(jn, "longitude"));
				gw.setTtn_location_source(toText(jn, "location_source"));

				uplinkGatewayRepo.save(gw);
				LOG.debug("Saved new uplink gateway: " + gw.getUplinkGatewayId());
				uplinkGateways.add(gw);
			}
		} catch (IOException ex) {
			LOG.error("Failed to parse or store gateways: " + uplink.getJson(), ex);
		}

		return uplinkGateways;
	}

	private List<Data> parseData(Uplink uplink, UplinkDetails uplinkDetails) {
		List<Data> datas = new LinkedList<Data>();

		if (uplinkDetails == null) {
			LOG.error("Failed to parse data as uplinkDetails is empty.");
			return datas;
		}
		if (uplinkDetails.getTtn_payload_fields() == null || uplinkDetails.getTtn_payload_fields().isEmpty()) {
			LOG.error("Failed to parse data as payload fields are null or empty.");
			return datas;
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn = mapper.readTree(uplinkDetails.getTtn_payload_fields());

			Iterator<Entry<String, JsonNode>> it = jn.fields();
			while (it.hasNext()) {
				Entry<String, JsonNode> entry = it.next();
				String key = (String) entry.getKey();
				JsonNode value = entry.getValue();

				Data data = new Data();
				data.setUplink(uplink);
				data.setUplinkDetails(uplinkDetails);

				data.setTtn_app_id(uplinkDetails.getTtn_app_id());
				data.setTtn_dev_id(uplinkDetails.getTtn_dev_id());
				data.setTtn_hardware_serial(uplinkDetails.getTtn_hardware_serial());

				data.setTtn_is_retry(uplinkDetails.getTtn_is_retry());

				data.setTime_converted(uplinkDetails.getTime_converted());

				data.setTtn_port(uplinkDetails.getTtn_port());

				data.setName(key);
				if (value.isInt()) {
					data.setValue_int(value.asInt());
					data.setValue_double(Integer.valueOf(value.asInt()).doubleValue());
					data.setValue_text(String.valueOf(value.asInt()));
				}
				if (value.isDouble()) {
					data.setValue_int(Double.valueOf(value.asDouble()).intValue());
					data.setValue_double(value.asDouble());
					data.setValue_text(String.valueOf(value.asDouble()));
				}
				if (value.isTextual()) {
					data.setValue_text(value.asText());
				}

				dataRepo.save(data);
				LOG.debug("Saved new data: " + data.getDataId());
				datas.add(data);
			}
		} catch (IOException ex) {
			LOG.error("Failed to parse or store the event: " + uplinkDetails.getTtn_payload_fields(), ex);
		}

		return datas;
	}

	private String toText(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		if (jn.isTextual()) {
			return jn.asText();
		}
		return null;

	}

	private String toString(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		return jn.toString();
	}

	private Integer toInteger(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		if (jn.isInt()) {
			return jn.asInt();
		}
		return null;
	}

	private Long toLong(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		if (jn.isLong()) {
			return jn.asLong();
		}
		if (jn.isInt()) {
			return Long.valueOf(jn.asInt());
		}
		return null;
	}

	private Double toDouble(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		if (jn.isDouble()) {
			return jn.asDouble();
		}
		if (jn.isInt()) {
			return Double.valueOf(jn.asInt());
		}
		return null;
	}

	private Boolean toBoolean(JsonNode jn, String find) {
		if (jn == null || find == null || find.isEmpty()) {
			return null;
		}
		jn = jn.findValue(find);
		if (jn == null || jn.isNull()) {
			return null;
		}
		if (jn.isBoolean()) {
			return jn.asBoolean();
		}
		return null;
	}

	private Boolean toBooleanDefault(JsonNode jn, String find, Boolean def) {
		Boolean b = toBoolean(jn, find);
		if (b == null) {
			return def;
		}
		return b;
	}

}
