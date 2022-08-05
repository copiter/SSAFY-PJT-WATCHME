package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.*;
import com.A108.Watchme.DTO.group.GroupListReqDTO;
import com.A108.Watchme.Http.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
public class GroupService {

    public ApiResponse getGroupList(String category, String keyword, int page, int active, HttpServletRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse getGroup(Long groupId, String pwd) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse createGroup(GroupCreateReqDTO groupCreateReqDTO, MultipartFile image, HttpServletRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse updateGroup(Long groupId, GroupUpdateReqDTO groupUpdateReqDTO, MultipartFile image, HttpServletRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse deleteGroup(Long groupId) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse getApplyList(Long groupId) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse apply(Long groupId) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse cancelApply(Long groupId) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse acceptApply(Long groupId, AcceptApplyReqDTO acceptApplyReqDTO) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse declineApply(Long groupId, DeclineApplyReqDTO declineApplyReqDTO) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse leaveGroup(Long groupId) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse tossLeader(Long groupId, LeaderTossReqDTO leaderTossReqDTO) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse kickGroup(Long groupId, GroupKickReqDTO groupKickReqDTO) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }
}
