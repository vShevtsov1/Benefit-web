package com.example.Redi.users.services;

import com.example.Redi.logs.data.Logs;
import com.example.Redi.logs.data.Points;
import com.example.Redi.logs.enums.LogType;
import com.example.Redi.logs.service.LogsService;
import com.example.Redi.logs.service.PointsService;
import com.example.Redi.users.data.User;
import com.example.Redi.users.enums.EmploymentType;
import com.example.Redi.users.enums.UpdatePointType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BonusService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private LogsService logsService;

    @Autowired
    private PointsService pointsService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateBonuses() {
        List<User> users = userRepository.findAll();
        Date today = new Date();
        Calendar cal = Calendar.getInstance();

        for (User user : users) {
            cal.setTime(user.getHireDate());
            int hireYear = cal.get(Calendar.YEAR);
            int hireMonth = cal.get(Calendar.MONTH);
            int hireDay = cal.get(Calendar.DAY_OF_MONTH);

            cal.setTime(today);
            int currentYear = cal.get(Calendar.YEAR);
            int currentMonth = cal.get(Calendar.MONTH);
            int currentDay = cal.get(Calendar.DAY_OF_MONTH);

            if (hireMonth == currentMonth && hireDay == currentDay) {
                int years = currentYear - hireYear;
                int count = calculateBonus(years);
                if(user.getEmploymentType().equals(EmploymentType.PART_TIME)){
                    count += count/2;
                }
                user.setBonusCount(user.getBonusCount() + count);
                userRepository.save(user);
                String logMessage = "System updated points for user with ID: " + user.getId() + "\n";
                logMessage += "Message: " + "automatic accrual on anniversary" + "\n";
                logMessage += "Count: " + count + "\n";

                logsService.createLog(new Logs(LogType.POINTS,null, LocalDateTime.now(),logMessage));
                pointsService.createPoints(new Points(null, user, count, UpdatePointType.INCREASE, "automatic accrual on anniversary", LocalDateTime.now()));

            }
        }
    }

    private int calculateBonus(int years) {
    if (years <= 0) return 0;
    switch (years) {
        case 1: return 10000;
        case 2: return 20000;
        case 3: return 30000;
        case 4: return 40000;
        case 5: return 60000;
        case 6: return 100000;
        case 7: return 140000;
        case 8: return 180000;
        case 9: return 220000;
        case 10: return 260000;
        default: return 260000;
    }
}

}
