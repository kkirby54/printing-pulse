package com.minhub.printing_pulse.api;

import com.minhub.printing_pulse.client.dto.ThingiverseDtos.ThingDto;
import com.minhub.printing_pulse.service.ThingImportService;
import com.minhub.printing_pulse.service.ThingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/things")
@RequiredArgsConstructor
public class ThingEntityController {
    private final ThingImportService importService;
    private final ThingQueryService queryService;

    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importThings() {
        var items = importService.importRandomFive();
        var dtos = queryService.getAllThings();
        return ResponseEntity.ok(Map.of(
            "imported", true,
            "count", items.size(),
            "items", dtos
        ));
    }

    @GetMapping
    public ResponseEntity<List<ThingDto>> getAllThings() {
        List<ThingDto> items = queryService.getAllThings();
        return ResponseEntity.ok(items);
    }
}
