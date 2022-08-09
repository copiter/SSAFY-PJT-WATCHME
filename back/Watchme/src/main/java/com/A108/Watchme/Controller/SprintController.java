package com.A108.Watchme.Controller;

import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    return null;
//        return sprintService.getSprints(groupId);
    }
}
