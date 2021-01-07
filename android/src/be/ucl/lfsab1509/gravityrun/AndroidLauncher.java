package be.ucl.lfsab1509.gravityrun;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;
import be.ucl.lfsab1509.gravityrun.gpgs.Gpgs;
import be.ucl.lfsab1509.gravityrun.screens.AbstractMultiPlayScreen;
import be.ucl.lfsab1509.gravityrun.screens.MultiPlayFirstModeScreen;
import be.ucl.lfsab1509.gravityrun.tools.AndroidBluetoothManager;
import be.ucl.lfsab1509.gravityrun.tools.BluetoothConstants;
import be.ucl.lfsab1509.gravityrun.tools.BluetoothFragment;
import be.ucl.lfsab1509.gravityrun.tools.sensors.AndroidSensorHelper;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;

public class AndroidLauncher extends AndroidApplication {

    public AbstractMultiPlayScreen multiPlayScreen;
    private Gpgs gpgs;

    private static class MyHandler extends Handler {
        AndroidLauncher launcher;
        MyHandler(AndroidLauncher launcher) {
            super(Looper.myLooper());
            this.launcher = launcher;
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothConstants.MESSAGE_DEVICE_NAME:
                    CharSequence connectedDevice = "Connected to " + msg.getData().getString(BluetoothConstants.DEVICE_NAME);
                    Toast.makeText(launcher, connectedDevice, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothConstants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    launcher.multiPlayScreen.incomingMessage(readMessage);
                    break;
                case BluetoothConstants.MESSAGE_STATE_CHANGE:
                    if (AndroidBluetoothManager.getState() == BluetoothConstants.STATE_NONE)
                        launcher.multiPlayScreen.onDisconnect();
                    break;
                case BluetoothConstants.MESSAGE_TOAST:
                    CharSequence content = msg.getData().getString(BluetoothConstants.TOAST);
                    Toast.makeText(launcher, content, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    private final Handler handler = new MyHandler(this);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        gpgs.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = true;
        config.useCompass = false;
        config.useGyroscope = true;

        BluetoothFragment bluetoothFragment = new BluetoothFragment(this, handler);

        //Fabric.with(this, new Crashlytics());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        gpgs = new Gpgs(this, handler);

        GravityRun gravityRun = new GravityRun(bluetoothFragment, gpgs, new AndroidSensorHelper(this));

        multiPlayScreen = new MultiPlayFirstModeScreen(gravityRun, false);

        initialize(gravityRun, config);
    }

    @Override
    protected void onDestroy() {
        System.out.println("Activity going to dispose");
        super.onDestroy();
        System.out.println("Activity disposed");
    }

}
