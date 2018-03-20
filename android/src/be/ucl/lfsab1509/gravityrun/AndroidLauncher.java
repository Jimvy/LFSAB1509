package be.ucl.lfsab1509.gravityrun;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;
import be.ucl.lfsab1509.gravityrun.screens.AbstractMultiPlayScreen;
import be.ucl.lfsab1509.gravityrun.screens.MultiPlayFirstModeScreen;
import be.ucl.lfsab1509.gravityrun.tools.AndroidBluetoothManager;
import be.ucl.lfsab1509.gravityrun.tools.AndroidSensorHelper;
import be.ucl.lfsab1509.gravityrun.tools.BluetoothConstants;
import be.ucl.lfsab1509.gravityrun.tools.BluetoothFragment;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class AndroidLauncher extends AndroidApplication {

    public AbstractMultiPlayScreen multiPlayScreen;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothConstants.MESSAGE_DEVICE_NAME:
                    CharSequence connectedDevice = "Connected to " + msg.getData().getString(BluetoothConstants.DEVICE_NAME);
                    Toast.makeText(AndroidLauncher.this, connectedDevice, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothConstants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    multiPlayScreen.incomingMessage(readMessage);
                    break;
                case BluetoothConstants.MESSAGE_STATE_CHANGE:
                    if (AndroidBluetoothManager.getState() == BluetoothConstants.STATE_NONE)
                        multiPlayScreen.onDisconnect();
                    break;
                case BluetoothConstants.MESSAGE_TOAST:
                    CharSequence content = msg.getData().getString(BluetoothConstants.TOAST);
                    Toast.makeText(AndroidLauncher.this, content, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGyroscope = true;
        config.useAccelerometer = true;
        config.useCompass = false;

        BluetoothFragment bluetoothFragment = new BluetoothFragment(this, handler);

        Fabric.with(this, new Crashlytics());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        GravityRun gravityRun = new GravityRun(bluetoothFragment, new AndroidSensorHelper(this));

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