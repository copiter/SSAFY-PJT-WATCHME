package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.Sprint.SprintPostDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SprintController {
    @Autowired
    private SprintService sprintService;

    @PostMapping("/sprints/{sprintId}/delete")
    ApiResponse deleteSprint(@PathVariable(value="sprintId") int sprintId){
        Long id = Long.valueOf(sprintId);

        return sprintService.deleteSprint(id);
    }
    @GetMapping("/sprints/{groupId}")
    ApiResponse getSprintList(@PathVariable(value = "groupId") int groupId){
        Long id = Long.valueOf(groupId);
    return sprintService.getSprints(id);
    }

    // 스프린트 생성
    @PostMapping("/sprints/{groupId}")
    ApiResponse createSprint(@PathVariable(value = "groupId") int groupId, @RequestPart(value="sprintPostDTO") SprintPostDTO sprintPostDTO,
                             @RequestPart(required = false, value = "images") MultipartFile images){
        Long id = Long.valueOf(groupId);
        return sprintService.createSprints(id,images, sprintPostDTO);
    }

    // 스프린트 참가
    @PostMapping("sprints/{sprintId}/join")
    ApiResponse joinSprint(@PathVariable(value="sprintId") int sprintId){
        Long sid = Long.valueOf(sprintId);
        return sprintService.joinSprints(sid);
    }

    // 스프린트 시작
    @PostMapping("sprints/{sprintId}/start")
    ApiResponse startSprint(@PathVariable(value="sprintId") int sprintId){
        Long sid = Long.valueOf(sprintId);
        return sprintService.startSprints(sid);
    }

//    @PostMapping("sprints/{sprintId}/delete")
//    ApiResponse deleteSprint(@PathVariable(value="sprintId") int sprintId){
//        Long sid = Long.valueOf(sprintId);
//        return sprintService.startSprints(sid);
//    }
}
