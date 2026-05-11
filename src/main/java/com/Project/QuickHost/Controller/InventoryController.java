package com.Project.QuickHost.Controller;

import com.Project.QuickHost.Dto.InventoryDto;
import com.Project.QuickHost.Dto.UpdateInventoryRequestDto;
import com.Project.QuickHost.Service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService invService;
    @GetMapping("/rooms/{roomId}")
    @Operation(summary = "Get Inventory by roomId", tags = {"Admin Inventory"})
    public List<InventoryDto> getInventoryofRoom(@PathVariable  Long roomId)
    {
        return invService.getInventoryOfRoom(roomId);
    }

    @PatchMapping("/rooms/{roomId}")
    @Operation(summary = "Update Inventory by roomId", tags = {"Admin Inventory"})

    public ResponseEntity<Void> UpdateInventoryofRoom(@PathVariable  Long roomId, @RequestBody UpdateInventoryRequestDto updateInvDto)
    {

                 invService.UpdateInventoryOfRoom(roomId,updateInvDto);
                 return ResponseEntity.noContent().build();

    }

}
