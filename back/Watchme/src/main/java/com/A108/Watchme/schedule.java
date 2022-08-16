package com.A108.Watchme;

import com.A108.Watchme.Repository.MRLRepository;
import com.A108.Watchme.Repository.MemberInfoRepository;
import com.A108.Watchme.Repository.SprintRepository;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import com.A108.Watchme.VO.Entity.member.MemberInfo;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class schedule {

    @Autowired
    MemberInfoRepository memberInfoRepository;
    @Autowired
    MRLRepository mrlRepository;
    @Autowired
    SprintRepository sprintRepository;

    Timestamp currTS = new Timestamp(System.currentTimeMillis());

    @Scheduled(cron = "0 * * * * *")
    public void studyTimeResetDay() {
        System.out.println("studyTime");

        List<MemberInfo> memberInfoList = memberInfoRepository.findAll();


        Calendar cal = Calendar.getInstance();
        cal.setTime(currTS);
        cal.add(Calendar.DATE, -1);
        currTS.setTime(cal.getTime().getTime());

        System.out.println(currTS.toString());

        for (MemberInfo mI : memberInfoList) {
            Integer studyTimeDay = 0;

            List<MemberRoomLog> memberRoomLogList = mrlRepository.findAllByMemberId(mI.getId())
                    .stream().filter((m) -> m.getJoinedAt().after(currTS) && m.getStudyTime() != null).collect(Collectors.toList()
                    );

            for (MemberRoomLog mrl : memberRoomLogList) {
                System.out.println("-----------------");
                System.out.println(mI.getId());
                System.out.println(mrl.getId());
                System.out.println(mrl.getStudyTime());
                System.out.println("-----------------");

                studyTimeDay += mrl.getStudyTime();
            }

            mI.setStudyTimeDay(studyTimeDay);

            memberInfoRepository.save(mI);

        }

    }

    @Scheduled(cron = "0 17 18 * * *")
    public void studyTimeResetWeek() {

        List<MemberInfo> memberInfoList = memberInfoRepository.findAll();

        Calendar cal = Calendar.getInstance();
        cal.setTime(currTS);
        cal.add(Calendar.DATE, -7);
        currTS.setTime(cal.getTime().getTime());

        for (MemberInfo mI : memberInfoList) {
            List<MemberRoomLog> memberRoomLogList = mrlRepository.findAllByMemberId(mI.getId())
                    .stream().filter((m) -> m.getJoinedAt().after(currTS)).collect(Collectors.toList());

            Integer studyTimeDay = 0;

            for (MemberRoomLog mrl :
                    memberRoomLogList) {
                studyTimeDay += mrl.getStudyTime();
            }

            mI.setStudyTimeDay(studyTimeDay);

            memberInfoRepository.save(mI);

        }

    }

    @Scheduled(cron = "0 17 18 * * *")
    public void studyTimeResetMonth() {

        List<MemberInfo> memberInfoList = memberInfoRepository.findAll();

        Calendar cal = Calendar.getInstance();
        cal.setTime(currTS);
        cal.add(Calendar.MONTH, -1);
        currTS.setTime(cal.getTime().getTime());

        for (MemberInfo mI : memberInfoList) {
            List<MemberRoomLog> memberRoomLogList = mrlRepository.findAllByMemberId(mI.getId())
                    .stream().filter((m) -> m.getJoinedAt().after(currTS)).collect(Collectors.toList());

            Integer studyTimeDay = 0;

            for (MemberRoomLog mrl :
                    memberRoomLogList) {
                studyTimeDay += mrl.getStudyTime();
            }

            mI.setStudyTimeDay(studyTimeDay);

            memberInfoRepository.save(mI);

        }

    }

    @Scheduled(cron = "0 17 18 * * *")
    public void startSprint() {
        Calendar cal = Calendar.getInstance();

        cal.setTime(currTS);
        cal.add(Calendar.DATE, -1);

        currTS.setTime(cal.getTime().getTime());

        // 시작 전 스프린트
        List<Sprint> sprintListYes = sprintRepository.findAllByStatus(Status.YES);
        sprintListYes.stream().filter(x -> x.getSprintInfo().getStartAt().after(new Date())).forEach(x -> x.setStatus(Status.ING));

        sprintRepository.saveAll(sprintListYes);


        // 진행 중 스프린트
        List<Sprint> sprintListIng = sprintRepository.findAllByStatus(Status.ING);
        sprintListIng.stream().filter(x -> x.getSprintInfo().getEndAt().before(new Date())).forEach(x -> x.setStatus(Status.NO));

        sprintRepository.saveAll(sprintListIng);
    }

}
