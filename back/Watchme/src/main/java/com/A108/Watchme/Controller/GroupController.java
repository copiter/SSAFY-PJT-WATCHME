package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.*;
import com.A108.Watchme.DTO.group.GroupCreateReqDTO;
import com.A108.Watchme.DTO.group.GroupKickReqDTO;
import com.A108.Watchme.DTO.group.GroupReqDTO;
import com.A108.Watchme.DTO.group.GroupUpdateReqDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public ApiResponse getGroupList(@RequestParam(value = "ctg",required = false) String ctgName, @RequestParam(value = "keyword",required = false) String keyword, @RequestParam(value = "page",required = false) Integer page, @RequestParam(value = "active", required = false) Integer active, HttpServletRequest request){
        return groupService.getGroupList(ctgName, keyword, page, active, request);
    }

    @PostMapping("/{groupId}")
    public ApiResponse getGroup(@RequestBody(required = false) GroupReqDTO groupReqDTO, @PathVariable(value = "groupId") Long groupId){
        if(groupReqDTO!=null){
            return groupService.getGroup(groupId, groupReqDTO.getPwd());
        }
        return groupService.getGroup(groupId, null);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse createGroup(@RequestPart GroupCreateReqDTO groupCreateReqDTO, @RequestPart(required = false) MultipartFile image, HttpServletRequest request){
        return groupService.createGroup(groupCreateReqDTO, image, request);
    }

    @PostMapping("/{groupId}/udpate")
    public ApiResponse updateGroup(@PathVariable(value = "groupId") Long groupId, @RequestPart GroupUpdateReqDTO groupUpdateReqDTO, @RequestPart(required = false) MultipartFile image, HttpServletRequest request) {
        return groupService.updateGroup(groupId, groupUpdateReqDTO, image, request);
    }

    @PostMapping("/{groupId}/delete")
    public ApiResponse deleteGroup(@PathVariable(value = "groupId") Long groupId, HttpServletRequest request) {
        return groupService.deleteGroup(groupId);
    }

    @GetMapping("/{groupId}/update-form")
    public ApiResponse updateForm(@PathVariable(value = "groupId") Long groupId, HttpServletRequest request){
        return groupService.updateForm(groupId,request);
    }

    @GetMapping("/{groupId}/applies")
    public ApiResponse getApplyList(@PathVariable(value = "groupId") Long groupId, HttpServletRequest request) {
        return groupService.getApplyList(groupId);
    }

    @GetMapping("/{groupId}/members")
    public ApiResponse getGroupMemberList(@PathVariable(value = "groupId") Long groupId, HttpServletRequest request) {
        return groupService.getApplyList(groupId);
    }

    @PostMapping("/{groupId}/applies")
    public ApiResponse apply(@PathVariable(value = "groupId") Long groupId, HttpServletRequest request) {
        return groupService.apply(groupId);
    }

    @PostMapping("/{groupId}/applies/cancel")
    public ApiResponse cancelApply(@PathVariable(value = "groupId") Long groupId, HttpServletRequest request) {
        return groupService.cancelApply(groupId);
    }

    @PostMapping("/{groupId}/applies/accept")
    public ApiResponse acceptApply(@PathVariable(value = "groupId") Long groupId, @RequestBody AcceptApplyReqDTO acceptApplyReqDTO, HttpServletRequest request) {
        return groupService.acceptApply(groupId, acceptApplyReqDTO);
    }

    @PostMapping("/{groupId}/applies/decline")
    public ApiResponse declineApply(@PathVariable(value = "groupId") Long groupId, @RequestBody DeclineApplyReqDTO declineApplyReqDTO, HttpServletRequest request) {
        return groupService.declineApply(groupId, declineApplyReqDTO);
    }

    @PostMapping("/{groupId}/leave")
    public ApiResponse leaveGroup(@PathVariable(value = "groupId") Long groupId, HttpServletRequest request) {
        return groupService.leaveGroup(groupId);
    }

    @PostMapping("/{groupId}/leader-toss")
    public ApiResponse tossLeader(@PathVariable(value = "groupId") Long groupId, @RequestBody LeaderTossReqDTO leaderTossReqDTO, HttpServletRequest request) {
        return groupService.tossLeader(groupId, leaderTossReqDTO);
    }

    @PostMapping("/{groupId}/kick")
    public ApiResponse kickGroup(@PathVariable(value = "groupId") Long groupId, @RequestBody GroupKickReqDTO groupKickReqDTO, HttpServletRequest request) {
        return groupService.kickGroup(groupId, groupKickReqDTO);
    }


}
