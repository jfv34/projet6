package com.vincler.jf.projet6.notifications;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

class NotificationsActivityPresenter {

    private final int REPEAT_INTERVAL_NOTIFICATIONS = 12;
    private byte error = 0;

    final PeriodicWorkRequest periodicWorkRequest =
            new PeriodicWorkRequest.Builder(NotificationsWorker.class, REPEAT_INTERVAL_NOTIFICATIONS, TimeUnit.HOURS)
                    .addTag("periodic_work")
                    .build();


    public byte getError() {
        return error;
    }


    public void sendPeriodicsNotifications() {
        WorkManager.getInstance().cancelAllWork();
        WorkManager.getInstance().enqueue(periodicWorkRequest);
    }

    public void stopNotifications() {
        WorkManager.getInstance().cancelAllWork();
    }
}
