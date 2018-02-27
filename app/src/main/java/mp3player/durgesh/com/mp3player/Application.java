package mp3player.durgesh.com.mp3player;

import android.content.pm.PackageInfo;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rebelute13 on 19/2/18.
 */

public class Application extends android.app.Application {

    PackageInfo packageInfo;

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }

}
