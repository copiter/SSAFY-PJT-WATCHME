package com.A108.Watchme;

import com.A108.Watchme.Repository.MRLRepository;
import com.A108.Watchme.Repository.MemberInfoRepository;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import com.A108.Watchme.VO.Entity.member.MemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class schedule {

    @Autowired
    MemberInfoRepository memberInfoRepository;
    @Autowired
    MRLRepository mrlRepository;

    Timestamp ttime = new Timestamp(System.currentTimeMillis());

    @Scheduled(cron = "0 0 0 * * *")
    public void studyTimeResetDay(){

        List<MemberInfo> memberInfoList = memberInfoRepository.findAll();


        Calendar cal = Calendar.getInstance();
        cal.setTime(ttime);
        cal.add(Calendar.DATE, -1);
        ttime.setTime(cal.getTime().getTime());

        for (MemberInfo mI : memberInfoList) {
            List<MemberRoomLog> memberRoomLogList = mrlRepository.findBymember_id(mI.getId()).stream().filter((m)->m.getStartAt().before(ttime)).collect(Collectors.toList());

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
            List<MemberRoomLog> memberRoomLogList = mrlRepository.findBymember_id(mI.getId()).stream().filter((m)->m.getStartAt().before(ttime)).collect(Collectors.toList());

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
            List<MemberRoomLog> memberRoomLogList = mrlRepository.findBymember_id(mI.getId()).stream().filter((m)->m.getStartAt().before(ttime)).collect(Collectors.toList());

            Integer studyTimeDay = 0;

            for (MemberRoomLog mrl :
                    memberRoomLogList) {
                studyTimeDay+= mrl.getStudyTime();
            }

            mI.setStudyTimeDay(studyTimeDay);

            memberInfoRepository.save(mI);

        }

    }


}
