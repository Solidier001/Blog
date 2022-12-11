package com.sun.blog;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class Tasks {
    @Scheduled(cron = "0 0 0 * * * ")
    public void cronJob() {
        System.out.println(new Date() + " ...>>cron....");
    }
}
