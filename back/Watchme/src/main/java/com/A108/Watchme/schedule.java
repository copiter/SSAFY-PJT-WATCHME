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

    Timestamp ttime = new Timestamp(System.currentTimeMillis());

    @Scheduled(cron = "0 0 0 * * *")
    public void studyTimeResetDay(){

        List<MemberInfo> memberInfoList = memberInfoRepository.findAll();


        Calendar cal = Calendar.getInstance();
        cal.setTime(ttime);
        cal.add(Calendar.DATE, -1);
        ttime.setTime(cal.getTime().getTime());

        for (MemberInfo mI : memberInfoList) {
            List<MemberRoomLog> memberRoomLogList = mrlRepository.findByMemberId(mI.getId()).stream().filter((m)->m.getJoinedAt().before(ttime)).collect(Collectors.toList());

            Integer studyTimeDay = 0;

            for (MemberRoomLog mrl :
                    memberRoomLogList) {
                studyTimeDay+= mrl.getStudyTime();
            }

            mI.setStudyTimeDay(studyTimeDay);

            memberInfoRepository.save(mI);

        }

    }

    @Scheduled(cron = "0 0 0 * * 0")
    public void studyTimeResetWeek(){

        List<MemberInfo> memberInfoList = memberInfoRepository.findAll();

        Calendar cal = Calendar.getInstance();
        cal.setTime(ttime);
        cal.add(Calendar.DATE, -7);
        ttime.setTime(cal.getTime().getTime());

        for (MemberInfo mI : memberInfoList) {
            List<MemberRoomLog> memberRoomLogList = mrlRepository.findByMemberId(mI.getId()).stream().filter((m)->m.getJoinedAt().before(ttime)).collect(Collectors.toList());

            Integer studyTimeDay = 0;

            for (MemberRoomLog mrl :
                    memberRoomLogList) {
                studyTimeDay+= mrl.getStudyTime();
            }

            mI.setStudyTimeDay(studyTimeDay);

            memberInfoRepository.save(mI);

        }

    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void studyTimeResetMonth(){

        List<MemberInfo> memberInfoList = memberInfoRepository.findAll();

        Calendar cal = Calendar.getInstance();
        cal.setTime(ttime);
        cal.add(Calendar.MONTH, -1);
        ttime.setTime(cal.getTime().getTime());

        for (MemberInfo mI : memberInfoList) {
            List<MemberRoomLog> memberRoomLogList = mrlRepository.findByMemberId(mI.getId()).stream().filter((m)->m.getJoinedAt().before(ttime)).collect(Collectors.toList());

            Integer studyTimeDay = 0;

            for (MemberRoomLog mrl :
                    memberRoomLogList) {
                studyTimeDay+= mrl.getStudyTime();
            }

            mI.setStudyTimeDay(studyTimeDay);

            memberInfoRepository.save(mI);

        }

    }

    @Scheduled(cron = "0 0 0 * * *")
    public void startSprint(){
        Calendar cal = Calendar.getInstance();

        cal.setTime(ttime);
        cal.add(Calendar.DATE, -1);

        ttime.setTime(cal.getTime().getTime());

        // 시작 전 스프린트
        List<Sprint> sprintListNo = sprintRepository.findAllByStatus(Status.NO);
        sprintListNo.stream().filter(x->x.getSprintInfo().getStartAt().before(new Date())).forEach(x->x.setStatus(Status.ING));

        sprintRepository.saveAll(sprintListNo);


        // 진행 중 스프린트
        List<Sprint> sprintListIng = sprintRepository.findAllByStatus(Status.ING);
        sprintListIng.stream().filter(x->x.getSprintInfo().getEndAt().before(new Date())).forEach(x->x.setStatus(Status.NO));

        sprintRepository.saveAll(sprintListIng);
        }

    }
