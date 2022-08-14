package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.Sprint.SprintPostDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.SprintService;
import com.A108.Watchme.utils.AuthUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
public class SprintController {
    @Autowired
    private SprintService sprintService;
    @Autowired
    private AuthUtil authUtil;
    @PostMapping("/sprints/{sprintId}/delete")
    ApiResponse deleteSprint(@PathVariable(value="sprintId") int sprintId){
        Long memberId = authUtil.memberAuth();
        Long id = Long.valueOf(sprintId);
        return sprintService.deleteSprint(id, memberId);
    }

    @GetMapping("/sprints/{groupId}")
    ApiResponse getSprintList(@PathVariable(value = "groupId") int groupId){
        Long id = Long.valueOf(groupId);
    return sprintService.getSprints(id);
    }

    // 스프린트 생성
    @PostMapping(value = "/sprints/{groupId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    ApiResponse createSprint(@PathVariable(value = "groupId") int groupId,@Valid @RequestPart(value="sprintPostDTO") SprintPostDTO sprintPostDTO,
                             @RequestPart(required = false, value = "images") MultipartFile images){
        Long memberId = authUtil.memberAuth();
        Long id = Long.valueOf(groupId);
        return sprintService.createSprints(id,images, sprintPostDTO, memberId);
    }

    // 스프린트 참가
    @PostMapping("sprints/{sprintId}/join")
    ApiResponse joinSprint(@PathVariable(value="sprintId") int sprintId){
        Long memberId = authUtil.memberAuth();
        Long sid = Long.valueOf(sprintId);
        return sprintService.joinSprints(sid, memberId);
    }

    // 스프린트 시작
    @PostMapping("sprints/{sprintId}/start")
    ApiResponse startSprint(@PathVariable(value="sprintId") int sprintId){
        Long memberId = authUtil.memberAuth();
        Long sid = Long.valueOf(sprintId);
        return sprintService.startSprints(sid, memberId);
    }

//    @PostMapping("sprints/{sprintId}/delete")
//    ApiResponse deleteSprint(@PathVariable(value="sprintId") int sprintId){
//        Long sid = Long.valueOf(sprintId);
//        return sprintService.startSprints(sid);
//    }
}
