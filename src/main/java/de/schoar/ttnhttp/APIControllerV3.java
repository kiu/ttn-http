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
public class APIControllerV3 {

	private static final Logger LOG = LoggerFactory.getLogger(APIControllerV3.class);

	@Autowired
	private UplinkRepository uplink2Repo;

	@Autowired
	private DataRepository data2Repo;

	@Autowired
	private GatewayRepository gateway2Repo;
		
	@RequestMapping(value = "/ttn-http-v3", method = RequestMethod.POST, consumes = "application/json")
	public void uplink(@RequestBody String json) {

		Date created = Date.from(Instant.now(Clock.systemUTC()));

		Uplink uplink = Uplink.parseV3(created, json);
		uplink2Repo.save(uplink);
		LOG.debug("Saved new V3 uplink: " + uplink.getId());

		List<Data> datas = Data.parse(uplink);
		for (Data data : datas) {
			data2Repo.save(data);
			LOG.debug("Saved new V3 data: " + data.getId());
		}

		List<Gateway> gateways = Gateway.parseV3(uplink);
		for (Gateway gateway : gateways) {
			gateway2Repo.save(gateway);
			LOG.debug("Saved new V3 gateway: " + gateway.getId());
		}

		uplink.parse(gateways);
		uplink2Repo.save(uplink);
		LOG.debug("Updated V3 uplink: " + uplink.getId());

	}

}
