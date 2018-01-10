/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package push.jerry.cn.scan;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * Simple listener used to exit the app in a few cases.
 *
 * @author Sean Owen
 */
public final class FinishListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

    private final Activity activityToFinish;

    public FinishListener(Activity activityToFinish) {
        this.activityToFinish = activityToFinish;
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        run();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        toSetAppinfoPage();
        run();
    }

    private void run() {
        activityToFinish.finish();
    }

    /**
     * 跳转到应用管理界面
     */
    private void toSetAppinfoPage() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        String pkg = "com.android.settings";
        String cls = "com.android.settings.applications.InstalledAppDetails";
        intent.setComponent(new ComponentName(pkg, cls));
        intent.setData(Uri.parse("package:" + activityToFinish.getPackageName()));
        activityToFinish.startActivity(intent);
    }

}
