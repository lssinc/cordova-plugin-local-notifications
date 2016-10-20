/*
 * Copyright (c) 2013-2015 by appPlant UG. All rights reserved.
 *
 * @APPPLANT_LICENSE_HEADER_START@
 *
 * This file contains Original Code and/or Modifications of Original Code
 * as defined in and that are subject to the Apache License
 * Version 2.0 (the 'License'). You may not use this file except in
 * compliance with the License. Please obtain a copy of the License at
 * http://opensource.org/licenses/Apache-2.0/ and read it before using this
 * file.
 *
 * The Original Code and all software distributed under the License are
 * distributed on an 'AS IS' basis, WITHOUT WARRANTY OF ANY KIND, EITHER
 * EXPRESS OR IMPLIED, AND APPLE HEREBY DISCLAIMS ALL SUCH WARRANTIES,
 * INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, QUIET ENJOYMENT OR NON-INFRINGEMENT.
 * Please see the License for the specific language governing rights and
 * limitations under the License.
 *
 * @APPPLANT_LICENSE_HEADER_END@
 */

package de.appplant.cordova.plugin.localnotification;

import de.appplant.cordova.plugin.notification.Builder;
import de.appplant.cordova.plugin.notification.Notification;

import org.json.JSONObject;
import android.support.v4.app.NotificationCompat;
import java.util.Date;

/**
 * The alarm receiver is triggered when a scheduled alarm is fired. This class
 * reads the information in the intent and displays this information in the
 * Android notification bar. The notification uses the default notification
 * sound and it vibrates the phone.
 */
public class TriggerReceiver extends de.appplant.cordova.plugin.notification.TriggerReceiver {

    /**
     * Called when a local notification was triggered. Does present the local
     * notification, re-schedule the alarm if necessary and fire trigger event.
     *
     * @param notification
     *      Wrapper around the local notification
     * @param updated
     *      If an update has triggered or the original
     */

    static final int EVERY_WEEK = 604800;

    @Override
    public void onTrigger (Notification notification, boolean updated) {
        super.onTrigger(notification, updated);

        try {
            JSONObject jsonObject = notification.getOptions().getDict();
            int lt = Integer.parseInt(jsonObject.get("at").toString()) + EVERY_WEEK;
            jsonObject.put("at", lt);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(notification.getContext());
            Notification newNotification = new Notification(notification.getContext(), notification.getOptions().parse(jsonObject), builder, TriggerReceiver.class);
            newNotification.schedule();
            if (!updated) {
                LocalNotification.fireEvent("trigger", notification);
            }
        } catch (Exception e) {
            //do nothing as of now.
        }
    }

    /**
     * Build notification specified by options.
     *
     * @param builder
     *      Notification builder
     */
    @Override
    public Notification buildNotification (Builder builder) {
        return builder
                .setTriggerReceiver(TriggerReceiver.class)
                .setClickActivity(ClickActivity.class)
                .setClearReceiver(ClearReceiver.class)
                .build();
    }

}
