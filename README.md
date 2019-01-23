# RFID 扫描ID/IC模块


##  配置maven库

```aidl
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "http://maven.bigoat.com/repository/android-releases/"
        }
    }
}
```

##  添加依赖

```aidl

 implementation 'cc.lecent.component:rfid:0.0.6'
 
```
##  初始化

```java

    @Override
    protected void onStart() {
        super.onStart();
        RFID.getInstance().init(new OnScanListener() {
            @Override
            public void onScan(String s) {
                Log.e("onScan","-------------"+s);
            }

            @Override
            public void initSuccess() {
                Log.e("onScan","initSuccess");
            }

            @Override
            public void initFail() {
                Log.e("onScan","initFail");
            }
        }).start();
    }
```
## 关闭操作

```java
    @Override
    protected void onPause() {
        super.onPause();
        RFID.getInstance().setclose();
    }
```


## 实现接口
```java
public interface OnScanListener {
    void onScan(String cardId);
    void initSuccess();
    void initFail();
}
```
## c71设备初始化是自动开启扫描

## 真对C72设备需要手动开启扫描
```java

    RFID.getInstance().openOrClose();

```

