package de.schoar.ttnhttp;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.schoar.ttnhttp.model.Data;
import de.schoar.ttnhttp.model.DataRepository;
import de.schoar.ttnhttp.model.Gateway;
import de.schoar.ttnhttp.model.GatewayRepository;
import de.schoar.ttnhttp.model.Uplink;
import de.schoar.ttnhttp.model.UplinkRepository;

@RestController
public class APIController {

	private static final Logger LOG = LoggerFactory.getLogger(APIController.class);

	@Autowired
	private UplinkRepository uplink2Repo;

	@Autowired
	private DataRepository data2Repo;

	@Autowired
	private GatewayRepository gateway2Repo;

	// curl -i -H "Accept: application/json" -H "Content-Type:application/json" -X
	// POST --data
	// '{"app_id":"kiu-test","dev_id":"lofence-001","hardware_serial":"0004A30B00245E75","port":1,"counter":35,"payload_raw":"D+cihAAADWcAAA==","payload_fields":{"bat":4071,"fmmax":3431,"fmmin":0,"fpmax":8836,"fpmin":0},"metadata":{"time":"2019-03-24T13:08:00.809651225Z","frequency":868.1,"modulation":"LORA","data_rate":"SF12BW125","coding_rate":"4/5","gateways":[{"gtw_id":"eui-1402fcc23d0f505b","timestamp":1453485412,"time":"2019-03-24T13:08:00.791757Z","channel":0,"rssi":-115,"snr":-13.8,"rf_chain":1,"latitude":48.094894,"longitude":11.505935,"location_source":"registry"}]},"downlink_url":"https://integrations.thethingsnetwork.org/ttn-eu/api/v2/down/kiu-test/kiu-logpush?key=ttn-account-v2.xxxxxxxxx"}'
	// "http://localhost:8080/ttn-http"
	@RequestMapping(value = "/ttn-http", method = RequestMethod.POST, consumes = "application/json")
	public void uplink(@RequestBody String json) {

		Date created = Date.from(Instant.now(Clock.systemUTC()));

		Uplink uplink = Uplink.parse(created, json);
		uplink2Repo.save(uplink);
		LOG.debug("Saved new uplink: " + uplink.getId());

		List<Data> datas = Data.parse(uplink);
		for (Data data : datas) {
			data2Repo.save(data);
			LOG.debug("Saved new data: " + data.getId());
		}

		List<Gateway> gateways = Gateway.parse(uplink);
		for (Gateway gateway : gateways) {
			gateway2Repo.save(gateway);
			LOG.debug("Saved new gateway: " + gateway.getId());
		}

		uplink.parse(gateways);
		uplink2Repo.save(uplink);
		LOG.debug("Updated uplink: " + uplink.getId());

	}

}
