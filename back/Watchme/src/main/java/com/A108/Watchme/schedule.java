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
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
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
//    @Scheduled(fixedRateString = "5000", initialDelay = 10000)
    public void studyTime() {

        studyTimeReset();

    }

    @Transactional(rollbackFor = {Exception.class})
    public void studyTimeReset() {

        System.out.println("studyTime-Day");

        List<MemberInfo> memberInfoList = memberInfoRepository.findAll();

        for (MemberInfo mI : memberInfoList) {

            Integer studyTime = 0;
            Integer studyTimeDay = 0;
            Integer studyTimeWeek = 0;
            Integer studyTimeMonth = 0;

            // month
            List<MemberRoomLog> memberRoomLogList = mrlRepository.findAllByMemberId(mI.getId()).stream()
                    .filter(mrl -> mrl.getJoinedAt().after(java.sql.Date.valueOf(LocalDate.now().minusMonths(1))) && mrl.getStudyTime() != null).collect(Collectors.toList());

            for (MemberRoomLog mrl : memberRoomLogList) {
                studyTimeMonth += mrl.getStudyTime();
            }

            // week
            memberRoomLogList = memberRoomLogList.stream()
                    .filter(mrl -> mrl.getJoinedAt().after(java.sql.Date.valueOf(LocalDate.now().minusWeeks(1))) && mrl.getStudyTime() != null).collect(Collectors.toList());

            for (MemberRoomLog mrl : memberRoomLogList) {
                studyTimeWeek += mrl.getStudyTime();
            }

            // day
            memberRoomLogList = memberRoomLogList.stream()
                    .filter(mrl -> mrl.getJoinedAt().after(java.sql.Date.valueOf(LocalDate.now().minusDays(1))) && mrl.getStudyTime() != null).collect(Collectors.toList());

            for (MemberRoomLog mrl : memberRoomLogList) {
                studyTimeDay += mrl.getStudyTime();
            }


            mI.setStudyTime(studyTime);
            mI.setStudyTimeDay(studyTimeDay);
            mI.setStudyTimeWeek(studyTimeWeek);
            mI.setStudyTimeMonth(studyTimeMonth);


            memberInfoRepository.save(mI);

        }

    }


    @Scheduled(cron = "0 * * * * *")
//    @Scheduled(fixedRateString = "5000", initialDelay = 10000)
    public void startSprint() {

        System.out.println("start-Sprint");

        Calendar cal = Calendar.getInstance();

        cal.setTime(currTS);
        cal.add(Calendar.DATE, -1);

        currTS.setTime(cal.getTime().getTime());

        Date curr = java.sql.Date.valueOf(LocalDate.now());

        // 시작 전 스프린트
        List<Sprint> sprintListYes = sprintRepository.findAllByStatus(Status.YES);
        sprintListYes.stream()
                .filter(x -> x.getSprintInfo().getStartAt().before(curr) && x.getSprintInfo().getEndAt().after(curr))
                .forEach(x -> x.setStatus(Status.ING));

        sprintRepository.saveAll(sprintListYes);


        // 진행 중 스프린트
        List<Sprint> sprintListIng = sprintRepository.findAllByStatus(Status.ING);
        sprintListIng.stream()
                .filter(x -> x.getSprintInfo().getEndAt().before(new Date())).forEach(x -> x.setStatus(Status.NO));

        sprintRepository.saveAll(sprintListIng);
    }

}
