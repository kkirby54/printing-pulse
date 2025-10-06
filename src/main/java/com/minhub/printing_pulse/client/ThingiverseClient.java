package com.minhub.printing_pulse.client;

import com.minhub.printing_pulse.client.dto.ThingiverseDtos.ThingDto;
import com.minhub.printing_pulse.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "thingiverse", url = "${thingiverse.api.base-url}", configuration = FeignConfig.class)
public interface ThingiverseClient {
    @GetMapping("/things/0/random")
    List<ThingDto> getRandomThings(); // Expect ~5 items; limit to 5 in service
}
