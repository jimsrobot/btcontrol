package jimsrobot.arduino.attiny85ad8495bt;

import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "jimsrobot.arduino.attiny85ad8495bt", "jimsrobot.arduino.attiny85ad8495bt.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
		BA.handler.postDelayed(new WaitForLayout(), 5);

	}
	private static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "jimsrobot.arduino.attiny85ad8495bt", "jimsrobot.arduino.attiny85ad8495bt.main");
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
		return true;
	}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true)
				return true;
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
		this.setIntent(intent);
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}

public static class _nameandmac{
public boolean IsInitialized;
public String Name;
public String Mac;
public void Initialize() {
IsInitialized = true;
Name = "";
Mac = "";
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Serial.BluetoothAdmin _admin = null;
public static anywheresoftware.b4a.objects.Serial _serial1 = null;
public static anywheresoftware.b4a.objects.collections.List _founddevices = null;
public static jimsrobot.arduino.attiny85ad8495bt.main._nameandmac _connecteddevice = null;
public static anywheresoftware.b4a.sql.SQL _sql1 = null;
public static String _default_mac_id = "";
public static boolean _skipbt = false;
public anywheresoftware.b4a.objects.ButtonWrapper _btnsearchfordevices = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnskip = null;
public static boolean _bsamebtdevice = false;
public static boolean _bmatcheddevice = false;
public jimsrobot.arduino.attiny85ad8495bt.profilelist _profilelist = null;
public jimsrobot.arduino.attiny85ad8495bt.editstage _editstage = null;
public jimsrobot.arduino.attiny85ad8495bt.stagelist _stagelist = null;
public static String  _activity_create(boolean _firsttime) throws Exception{
		Debug.PushSubsStack("Activity_Create (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("FirstTime", _firsttime);
 BA.debugLineNum = 32;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
Debug.ShouldStop(-2147483648);
 BA.debugLineNum = 33;BA.debugLine="If FirstTime Then";
Debug.ShouldStop(1);
if (_firsttime) { 
 BA.debugLineNum = 34;BA.debugLine="admin.Initialize(\"admin\")";
Debug.ShouldStop(2);
_admin.Initialize(processBA,"admin");
 BA.debugLineNum = 35;BA.debugLine="serial1.Initialize(\"serial1\")";
Debug.ShouldStop(4);
Debug.DebugWarningEngine.CheckInitialize(_serial1);_serial1.Initialize("serial1");
 BA.debugLineNum = 36;BA.debugLine="SQL1.Initialize(File.DirDefaultExternal, \"wirelessreflow.db\", True)";
Debug.ShouldStop(8);
Debug.DebugWarningEngine.CheckInitialize(_sql1);_sql1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"wirelessreflow.db",anywheresoftware.b4a.keywords.Common.True);
 BA.debugLineNum = 37;BA.debugLine="LoadSQLTables";
Debug.ShouldStop(16);
_loadsqltables();
 };
 BA.debugLineNum = 39;BA.debugLine="Activity.LoadLayout(\"start\")";
Debug.ShouldStop(64);
mostCurrent._activity.LoadLayout("start",mostCurrent.activityBA);
 BA.debugLineNum = 40;BA.debugLine="End Sub";
Debug.ShouldStop(128);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _activity_pause(boolean _userclosed) throws Exception{
		Debug.PushSubsStack("Activity_Pause (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("UserClosed", _userclosed);
 BA.debugLineNum = 69;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
Debug.ShouldStop(16);
 BA.debugLineNum = 70;BA.debugLine="If UserClosed = True Then";
Debug.ShouldStop(32);
if (_userclosed==anywheresoftware.b4a.keywords.Common.True) { 
 BA.debugLineNum = 71;BA.debugLine="serial1.Disconnect";
Debug.ShouldStop(64);
_serial1.Disconnect();
 };
 BA.debugLineNum = 73;BA.debugLine="End Sub";
Debug.ShouldStop(256);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _activity_resume() throws Exception{
		Debug.PushSubsStack("Activity_Resume (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 51;BA.debugLine="Sub Activity_Resume";
Debug.ShouldStop(262144);
 BA.debugLineNum = 52;BA.debugLine="btnSearchForDevices.Enabled = False";
Debug.ShouldStop(524288);
mostCurrent._btnsearchfordevices.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 BA.debugLineNum = 53;BA.debugLine="If admin.IsEnabled = False Then";
Debug.ShouldStop(1048576);
if (_admin.IsEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
 BA.debugLineNum = 54;BA.debugLine="If admin.Enable = False Then";
Debug.ShouldStop(2097152);
if (_admin.Enable()==anywheresoftware.b4a.keywords.Common.False) { 
 BA.debugLineNum = 55;BA.debugLine="ToastMessageShow(\"Error enabling Bluetooth adapter.\", True)";
Debug.ShouldStop(4194304);
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error enabling Bluetooth adapter.",anywheresoftware.b4a.keywords.Common.True);
 }else {
 BA.debugLineNum = 57;BA.debugLine="ToastMessageShow(\"Enabling Bluetooth adapter...\", False)";
Debug.ShouldStop(16777216);
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Enabling Bluetooth adapter...",anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 BA.debugLineNum = 61;BA.debugLine="Admin_StateChanged(admin.STATE_ON, 0)";
Debug.ShouldStop(268435456);
_admin_statechanged(_admin.STATE_ON,(int)(0));
 };
 BA.debugLineNum = 63;BA.debugLine="End Sub";
Debug.ShouldStop(1073741824);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _admin_devicefound(String _name,String _macaddress) throws Exception{
		Debug.PushSubsStack("Admin_DeviceFound (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
jimsrobot.arduino.attiny85ad8495bt.main._nameandmac _nm = null;
Debug.locals.put("Name", _name);
Debug.locals.put("MacAddress", _macaddress);
 BA.debugLineNum = 131;BA.debugLine="Sub Admin_DeviceFound (Name As String, MacAddress As String)";
Debug.ShouldStop(4);
 BA.debugLineNum = 132;BA.debugLine="Log(Name & \":\" & MacAddress)";
Debug.ShouldStop(8);
anywheresoftware.b4a.keywords.Common.Log(_name+":"+_macaddress);
 BA.debugLineNum = 133;BA.debugLine="Dim nm As NameAndMac";
Debug.ShouldStop(16);
_nm = new jimsrobot.arduino.attiny85ad8495bt.main._nameandmac();Debug.locals.put("nm", _nm);
 BA.debugLineNum = 134;BA.debugLine="nm.Name = Name";
Debug.ShouldStop(32);
_nm.Name = _name;Debug.locals.put("nm", _nm);
 BA.debugLineNum = 135;BA.debugLine="nm.Mac = MacAddress";
Debug.ShouldStop(64);
_nm.Mac = _macaddress;Debug.locals.put("nm", _nm);
 BA.debugLineNum = 136;BA.debugLine="foundDevices.Add(nm)";
Debug.ShouldStop(128);
_founddevices.Add((Object)(_nm));
 BA.debugLineNum = 138;BA.debugLine="If nm.Mac = default_mac_id AND bSameBTDevice = True Then";
Debug.ShouldStop(512);
if ((_nm.Mac).equals(_default_mac_id) && _bsamebtdevice==anywheresoftware.b4a.keywords.Common.True) { 
 BA.debugLineNum = 139;BA.debugLine="bMatchedDevice = True";
Debug.ShouldStop(1024);
_bmatcheddevice = anywheresoftware.b4a.keywords.Common.True;
 BA.debugLineNum = 140;BA.debugLine="admin.CancelDiscovery";
Debug.ShouldStop(2048);
_admin.CancelDiscovery();
 }else {
 BA.debugLineNum = 142;BA.debugLine="ProgressDialogShow(\"Searching for devices (~ device found)...\".Replace(\"~\", foundDevices.Size))";
Debug.ShouldStop(8192);
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Searching for devices (~ device found)...".replace("~",BA.NumberToString(_founddevices.getSize())));
 };
 BA.debugLineNum = 144;BA.debugLine="End Sub";
Debug.ShouldStop(32768);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _admin_discoveryfinished() throws Exception{
		Debug.PushSubsStack("Admin_DiscoveryFinished (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
int _i = 0;
jimsrobot.arduino.attiny85ad8495bt.main._nameandmac _nm = null;
anywheresoftware.b4a.objects.collections.List _l = null;
int _res = 0;
 BA.debugLineNum = 95;BA.debugLine="Sub Admin_DiscoveryFinished";
Debug.ShouldStop(1073741824);
 BA.debugLineNum = 96;BA.debugLine="ProgressDialogHide";
Debug.ShouldStop(-2147483648);
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 BA.debugLineNum = 97;BA.debugLine="If foundDevices.Size = 0 Then";
Debug.ShouldStop(1);
if (_founddevices.getSize()==0) { 
 BA.debugLineNum = 98;BA.debugLine="ToastMessageShow(\"No device found.\", True)";
Debug.ShouldStop(2);
anywheresoftware.b4a.keywords.Common.ToastMessageShow("No device found.",anywheresoftware.b4a.keywords.Common.True);
 }else 
{ BA.debugLineNum = 99;BA.debugLine="Else If bMatchedDevice = True AND bSameBTDevice = True Then";
Debug.ShouldStop(4);
if (_bmatcheddevice==anywheresoftware.b4a.keywords.Common.True && _bsamebtdevice==anywheresoftware.b4a.keywords.Common.True) { 
 BA.debugLineNum = 100;BA.debugLine="For i = 0 To foundDevices.Size - 1";
Debug.ShouldStop(8);
{
final double step73 = 1;
final double limit73 = (int)(_founddevices.getSize()-1);
for (_i = (int)(0); (step73 > 0 && _i <= limit73) || (step73 < 0 && _i >= limit73); _i += step73) {
Debug.locals.put("i", _i);
 BA.debugLineNum = 101;BA.debugLine="Dim nm As NameAndMac";
Debug.ShouldStop(16);
_nm = new jimsrobot.arduino.attiny85ad8495bt.main._nameandmac();Debug.locals.put("nm", _nm);
 BA.debugLineNum = 102;BA.debugLine="nm = foundDevices.Get(i)";
Debug.ShouldStop(32);
_nm = (jimsrobot.arduino.attiny85ad8495bt.main._nameandmac)(_founddevices.Get(_i));Debug.locals.put("nm", _nm);
 BA.debugLineNum = 103;BA.debugLine="If nm.Mac = default_mac_id Then";
Debug.ShouldStop(64);
if ((_nm.Mac).equals(_default_mac_id)) { 
 BA.debugLineNum = 104;BA.debugLine="connectedDevice = nm";
Debug.ShouldStop(128);
_connecteddevice = _nm;
 BA.debugLineNum = 105;BA.debugLine="ProgressDialogShow(\"Trying to connect to: \" & connectedDevice.Name & \" (\" & connectedDevice.Mac & \")\")";
Debug.ShouldStop(256);
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Trying to connect to: "+_connecteddevice.Name+" ("+_connecteddevice.Mac+")");
 BA.debugLineNum = 106;BA.debugLine="serial1.Connect(connectedDevice.Mac)";
Debug.ShouldStop(512);
_serial1.Connect(processBA,_connecteddevice.Mac);
 };
 }
}Debug.locals.put("i", _i);
;
 }else {
 BA.debugLineNum = 110;BA.debugLine="Dim l As List";
Debug.ShouldStop(8192);
_l = new anywheresoftware.b4a.objects.collections.List();Debug.locals.put("l", _l);
 BA.debugLineNum = 111;BA.debugLine="l.Initialize";
Debug.ShouldStop(16384);
_l.Initialize();
 BA.debugLineNum = 112;BA.debugLine="For i = 0 To foundDevices.Size - 1";
Debug.ShouldStop(32768);
{
final double step85 = 1;
final double limit85 = (int)(_founddevices.getSize()-1);
for (_i = (int)(0); (step85 > 0 && _i <= limit85) || (step85 < 0 && _i >= limit85); _i += step85) {
Debug.locals.put("i", _i);
 BA.debugLineNum = 113;BA.debugLine="Dim nm As NameAndMac";
Debug.ShouldStop(65536);
_nm = new jimsrobot.arduino.attiny85ad8495bt.main._nameandmac();Debug.locals.put("nm", _nm);
 BA.debugLineNum = 114;BA.debugLine="nm = foundDevices.Get(i)";
Debug.ShouldStop(131072);
_nm = (jimsrobot.arduino.attiny85ad8495bt.main._nameandmac)(_founddevices.Get(_i));Debug.locals.put("nm", _nm);
 BA.debugLineNum = 115;BA.debugLine="l.Add(nm.Name)";
Debug.ShouldStop(262144);
_l.Add((Object)(_nm.Name));
 }
}Debug.locals.put("i", _i);
;
 BA.debugLineNum = 117;BA.debugLine="Dim res As Int";
Debug.ShouldStop(1048576);
_res = 0;Debug.locals.put("res", _res);
 BA.debugLineNum = 118;BA.debugLine="res = InputList(l, \"Choose device to connect\", -1)";
Debug.ShouldStop(2097152);
_res = anywheresoftware.b4a.keywords.Common.InputList(_l,"Choose device to connect",(int)(-1),mostCurrent.activityBA);Debug.locals.put("res", _res);
 BA.debugLineNum = 119;BA.debugLine="If res <> DialogResponse.CANCEL Then";
Debug.ShouldStop(4194304);
if (_res!=anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 BA.debugLineNum = 120;BA.debugLine="connectedDevice = foundDevices.Get(res)";
Debug.ShouldStop(8388608);
_connecteddevice = (jimsrobot.arduino.attiny85ad8495bt.main._nameandmac)(_founddevices.Get(_res));
 BA.debugLineNum = 121;BA.debugLine="ProgressDialogShow(\"Trying to connect to: \" & connectedDevice.Name & \" (\" & connectedDevice.Mac & \")\")";
Debug.ShouldStop(16777216);
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Trying to connect to: "+_connecteddevice.Name+" ("+_connecteddevice.Mac+")");
 BA.debugLineNum = 122;BA.debugLine="serial1.Connect(connectedDevice.Mac)";
Debug.ShouldStop(33554432);
_serial1.Connect(processBA,_connecteddevice.Mac);
 BA.debugLineNum = 124;BA.debugLine="SQL1.ExecNonQuery(\"DELETE FROM default_device\")";
Debug.ShouldStop(134217728);
_sql1.ExecNonQuery("DELETE FROM default_device");
 BA.debugLineNum = 125;BA.debugLine="SQL1.ExecNonQuery(\"INSERT INTO default_device VALUES('\" & connectedDevice.Mac & \"')\")";
Debug.ShouldStop(268435456);
_sql1.ExecNonQuery("INSERT INTO default_device VALUES('"+_connecteddevice.Mac+"')");
 };
 }};
 BA.debugLineNum = 129;BA.debugLine="End Sub";
Debug.ShouldStop(1);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _admin_statechanged(int _newstate,int _oldstate) throws Exception{
		Debug.PushSubsStack("Admin_StateChanged (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("NewState", _newstate);
Debug.locals.put("OldState", _oldstate);
 BA.debugLineNum = 65;BA.debugLine="Sub Admin_StateChanged (NewState As Int, OldState As Int)";
Debug.ShouldStop(1);
 BA.debugLineNum = 66;BA.debugLine="btnSearchForDevices.Enabled = (NewState = admin.STATE_ON)";
Debug.ShouldStop(2);
mostCurrent._btnsearchfordevices.setEnabled((_newstate==_admin.STATE_ON));
 BA.debugLineNum = 67;BA.debugLine="End Sub";
Debug.ShouldStop(4);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btnconnectsame_click() throws Exception{
		Debug.PushSubsStack("btnConnectSame_Click (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 75;BA.debugLine="Sub btnConnectSame_Click";
Debug.ShouldStop(1024);
 BA.debugLineNum = 76;BA.debugLine="bSameBTDevice = True";
Debug.ShouldStop(2048);
_bsamebtdevice = anywheresoftware.b4a.keywords.Common.True;
 BA.debugLineNum = 77;BA.debugLine="StartSearch";
Debug.ShouldStop(4096);
_startsearch();
 BA.debugLineNum = 78;BA.debugLine="End Sub";
Debug.ShouldStop(8192);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btnsearchfordevices_click() throws Exception{
		Debug.PushSubsStack("btnSearchForDevices_Click (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 80;BA.debugLine="Sub btnSearchForDevices_Click";
Debug.ShouldStop(32768);
 BA.debugLineNum = 81;BA.debugLine="bSameBTDevice = False";
Debug.ShouldStop(65536);
_bsamebtdevice = anywheresoftware.b4a.keywords.Common.False;
 BA.debugLineNum = 82;BA.debugLine="StartSearch";
Debug.ShouldStop(131072);
_startsearch();
 BA.debugLineNum = 83;BA.debugLine="End Sub";
Debug.ShouldStop(262144);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _btnskip_click() throws Exception{
		Debug.PushSubsStack("btnSkip_Click (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 158;BA.debugLine="Sub btnSkip_Click";
Debug.ShouldStop(536870912);
 BA.debugLineNum = 159;BA.debugLine="SkipBT = True";
Debug.ShouldStop(1073741824);
_skipbt = anywheresoftware.b4a.keywords.Common.True;
 BA.debugLineNum = 160;BA.debugLine="StartActivity(ProfileList)";
Debug.ShouldStop(-2147483648);
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._profilelist.getObject()));
 BA.debugLineNum = 161;BA.debugLine="End Sub";
Debug.ShouldStop(1);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}

public static void initializeProcessGlobals() {
    if (mostCurrent != null && mostCurrent.activityBA != null) {
Debug.StartDebugging(mostCurrent.activityBA, 21127, new int[] {6, 13, 7, 14}, "e44a082b-2dbf-48a0-b0db-cedefbed58d8");}

    if (processGlobalsRun == false) {
	    processGlobalsRun = true;
		try {
		        main._process_globals();
profilelist._process_globals();
editstage._process_globals();
stagelist._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (profilelist.mostCurrent != null);
vis = vis | (editstage.mostCurrent != null);
vis = vis | (stagelist.mostCurrent != null);
return vis;}

public static void killProgram() {
    
            if (main.previousOne != null) {
				Activity a = main.previousOne.get();
				if (a != null)
					a.finish();
			}


            if (profilelist.previousOne != null) {
				Activity a = profilelist.previousOne.get();
				if (a != null)
					a.finish();
			}


            if (editstage.previousOne != null) {
				Activity a = editstage.previousOne.get();
				if (a != null)
					a.finish();
			}


            if (stagelist.previousOne != null) {
				Activity a = stagelist.previousOne.get();
				if (a != null)
					a.finish();
			}

}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Dim btnSearchForDevices As Button";
mostCurrent._btnsearchfordevices = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim btnSkip As Button";
mostCurrent._btnskip = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim btnSearchForDevices As Button";
mostCurrent._btnsearchfordevices = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim bSameBTDevice As Boolean 			:bSameBTDevice  = False";
_bsamebtdevice = false;
 //BA.debugLineNum = 27;BA.debugLine="Dim bSameBTDevice As Boolean 			:bSameBTDevice  = False";
_bsamebtdevice = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 28;BA.debugLine="Dim bMatchedDevice As Boolean 		:bMatchedDevice = False";
_bmatcheddevice = false;
 //BA.debugLineNum = 28;BA.debugLine="Dim bMatchedDevice As Boolean 		:bMatchedDevice = False";
_bmatcheddevice = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _loadsqltables() throws Exception{
		Debug.PushSubsStack("LoadSQLTables (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 43;BA.debugLine="Sub LoadSQLTables";
Debug.ShouldStop(1024);
 BA.debugLineNum = 44;BA.debugLine="SQL1.ExecNonQuery(\"CREATE TABLE IF NOT EXISTS default_device (mac_id TEXT)\")";
Debug.ShouldStop(2048);
_sql1.ExecNonQuery("CREATE TABLE IF NOT EXISTS default_device (mac_id TEXT)");
 BA.debugLineNum = 47;BA.debugLine="default_mac_id = SQL1.ExecQuerySingleResult (\"SELECT * FROM default_device\")";
Debug.ShouldStop(16384);
_default_mac_id = _sql1.ExecQuerySingleResult("SELECT * FROM default_device");
 BA.debugLineNum = 48;BA.debugLine="End Sub";
Debug.ShouldStop(32768);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 13;BA.debugLine="Dim admin As BluetoothAdmin";
_admin = new anywheresoftware.b4a.objects.Serial.BluetoothAdmin();
 //BA.debugLineNum = 14;BA.debugLine="Dim serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 15;BA.debugLine="Dim foundDevices As List";
_founddevices = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 16;BA.debugLine="Type NameAndMac (Name As String, Mac As String)";
;
 //BA.debugLineNum = 17;BA.debugLine="Dim connectedDevice As NameAndMac";
_connecteddevice = new jimsrobot.arduino.attiny85ad8495bt.main._nameandmac();
 //BA.debugLineNum = 18;BA.debugLine="Dim SQL1 As SQL";
_sql1 = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 19;BA.debugLine="Dim default_mac_id As String";
_default_mac_id = "";
 //BA.debugLineNum = 20;BA.debugLine="Dim SkipBT As Boolean";
_skipbt = false;
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _serial1_connected(boolean _success) throws Exception{
		Debug.PushSubsStack("Serial1_Connected (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
Debug.locals.put("Success", _success);
 BA.debugLineNum = 146;BA.debugLine="Sub Serial1_Connected (Success As Boolean)";
Debug.ShouldStop(131072);
 BA.debugLineNum = 147;BA.debugLine="ProgressDialogHide";
Debug.ShouldStop(262144);
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 BA.debugLineNum = 148;BA.debugLine="Log(\"connected: \" & Success)";
Debug.ShouldStop(524288);
anywheresoftware.b4a.keywords.Common.Log("connected: "+String.valueOf(_success));
 BA.debugLineNum = 149;BA.debugLine="If Success = False Then";
Debug.ShouldStop(1048576);
if (_success==anywheresoftware.b4a.keywords.Common.False) { 
 BA.debugLineNum = 150;BA.debugLine="Log(LastException.Message)";
Debug.ShouldStop(2097152);
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage());
 BA.debugLineNum = 151;BA.debugLine="ToastMessageShow(\"Error connecting: \" & LastException.Message, True)";
Debug.ShouldStop(4194304);
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error connecting: "+anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage(),anywheresoftware.b4a.keywords.Common.True);
 }else {
 BA.debugLineNum = 153;BA.debugLine="SkipBT = False";
Debug.ShouldStop(16777216);
_skipbt = anywheresoftware.b4a.keywords.Common.False;
 BA.debugLineNum = 154;BA.debugLine="StartActivity(ProfileList)";
Debug.ShouldStop(33554432);
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._profilelist.getObject()));
 };
 BA.debugLineNum = 156;BA.debugLine="End Sub";
Debug.ShouldStop(134217728);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
public static String  _startsearch() throws Exception{
		Debug.PushSubsStack("StartSearch (main) ","main",0,mostCurrent.activityBA,mostCurrent);
try {
 BA.debugLineNum = 85;BA.debugLine="Sub StartSearch";
Debug.ShouldStop(1048576);
 BA.debugLineNum = 86;BA.debugLine="foundDevices.Initialize";
Debug.ShouldStop(2097152);
_founddevices.Initialize();
 BA.debugLineNum = 88;BA.debugLine="If admin.StartDiscovery	= False Then";
Debug.ShouldStop(8388608);
if (_admin.StartDiscovery()==anywheresoftware.b4a.keywords.Common.False) { 
 BA.debugLineNum = 89;BA.debugLine="ToastMessageShow(\"Error starting discovery process.\", True)";
Debug.ShouldStop(16777216);
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Error starting discovery process.",anywheresoftware.b4a.keywords.Common.True);
 }else {
 BA.debugLineNum = 91;BA.debugLine="ProgressDialogShow(\"Searching for devices...\")";
Debug.ShouldStop(67108864);
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Searching for devices...");
 };
 BA.debugLineNum = 93;BA.debugLine="End Sub";
Debug.ShouldStop(268435456);
return "";
}
catch (Exception e) {
			Debug.ErrorCaught(e);
			throw e;
		} 
finally {
			Debug.PopSubsStack();
		}}
  public Object[] GetGlobals() {
		return new Object[] {"Activity",_activity,"admin",_admin,"serial1",_serial1,"foundDevices",_founddevices,"connectedDevice",_connecteddevice,"SQL1",_sql1,"default_mac_id",_default_mac_id,"SkipBT",_skipbt,"btnSearchForDevices",_btnsearchfordevices,"btnSkip",_btnskip,"bSameBTDevice",_bsamebtdevice,"bMatchedDevice",_bmatcheddevice,"ProfileList",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.profilelist.class),"EditStage",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.editstage.class),"StageList",Debug.moduleToString(jimsrobot.arduino.attiny85ad8495bt.stagelist.class)};
}
}
