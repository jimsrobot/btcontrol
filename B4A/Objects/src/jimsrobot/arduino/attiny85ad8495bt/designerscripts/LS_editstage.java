package jimsrobot.arduino.attiny85ad8495bt.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_editstage{

public static void LS_general(java.util.HashMap<String, anywheresoftware.b4a.objects.ViewWrapper<?>> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 4;BA.debugLine="chkDoNothing.Top = 0%y"[editstage/General script]
views.get("chkdonothing").setTop((int)((0d / 100 * height)));
//BA.debugLineNum = 5;BA.debugLine="chkDoNothing.Left = 0%x"[editstage/General script]
views.get("chkdonothing").setLeft((int)((0d / 100 * width)));
//BA.debugLineNum = 6;BA.debugLine="chkDoNothing.Height = 50dip"[editstage/General script]
views.get("chkdonothing").setHeight((int)((50d * scale)));
//BA.debugLineNum = 8;BA.debugLine="chkIncreaseTemp.Top = 10%y"[editstage/General script]
views.get("chkincreasetemp").setTop((int)((10d / 100 * height)));
//BA.debugLineNum = 9;BA.debugLine="chkIncreaseTemp.Left = 0%x"[editstage/General script]
views.get("chkincreasetemp").setLeft((int)((0d / 100 * width)));
//BA.debugLineNum = 10;BA.debugLine="chkIncreaseTemp.Height = 50dip"[editstage/General script]
views.get("chkincreasetemp").setHeight((int)((50d * scale)));
//BA.debugLineNum = 12;BA.debugLine="chkBeep.Top = 20%y"[editstage/General script]
views.get("chkbeep").setTop((int)((20d / 100 * height)));
//BA.debugLineNum = 13;BA.debugLine="chkBeep.Left = 0%x"[editstage/General script]
views.get("chkbeep").setLeft((int)((0d / 100 * width)));
//BA.debugLineNum = 14;BA.debugLine="chkBeep.Height = 50dip"[editstage/General script]
views.get("chkbeep").setHeight((int)((50d * scale)));
//BA.debugLineNum = 16;BA.debugLine="Label1.Left = 0%x"[editstage/General script]
views.get("label1").setLeft((int)((0d / 100 * width)));
//BA.debugLineNum = 17;BA.debugLine="Label1.Width = 30%x"[editstage/General script]
views.get("label1").setWidth((int)((30d / 100 * width)));
//BA.debugLineNum = 18;BA.debugLine="Label1.Top = 45%y"[editstage/General script]
views.get("label1").setTop((int)((45d / 100 * height)));
//BA.debugLineNum = 19;BA.debugLine="Label1.Height = 50dip"[editstage/General script]
views.get("label1").setHeight((int)((50d * scale)));
//BA.debugLineNum = 21;BA.debugLine="Label2.Left = 0%x"[editstage/General script]
views.get("label2").setLeft((int)((0d / 100 * width)));
//BA.debugLineNum = 22;BA.debugLine="Label2.Width = 30%x"[editstage/General script]
views.get("label2").setWidth((int)((30d / 100 * width)));
//BA.debugLineNum = 23;BA.debugLine="Label2.Top = 57%y"[editstage/General script]
views.get("label2").setTop((int)((57d / 100 * height)));
//BA.debugLineNum = 24;BA.debugLine="Label2.Height = 50dip"[editstage/General script]
views.get("label2").setHeight((int)((50d * scale)));
//BA.debugLineNum = 26;BA.debugLine="Label3.Left = 0%x"[editstage/General script]
views.get("label3").setLeft((int)((0d / 100 * width)));
//BA.debugLineNum = 27;BA.debugLine="Label3.Width = 30%x"[editstage/General script]
views.get("label3").setWidth((int)((30d / 100 * width)));
//BA.debugLineNum = 28;BA.debugLine="Label3.Top = 69%y"[editstage/General script]
views.get("label3").setTop((int)((69d / 100 * height)));
//BA.debugLineNum = 29;BA.debugLine="Label3.Height = 50dip"[editstage/General script]
views.get("label3").setHeight((int)((50d * scale)));
//BA.debugLineNum = 31;BA.debugLine="txtDuration.Left = 40%x"[editstage/General script]
views.get("txtduration").setLeft((int)((40d / 100 * width)));
//BA.debugLineNum = 32;BA.debugLine="txtDuration.Width = 60%x"[editstage/General script]
views.get("txtduration").setWidth((int)((60d / 100 * width)));
//BA.debugLineNum = 33;BA.debugLine="txtDuration.Top = 45%y"[editstage/General script]
views.get("txtduration").setTop((int)((45d / 100 * height)));
//BA.debugLineNum = 34;BA.debugLine="txtDuration.Height = 50dip"[editstage/General script]
views.get("txtduration").setHeight((int)((50d * scale)));
//BA.debugLineNum = 36;BA.debugLine="txtTargetTemp.Left = 40%x"[editstage/General script]
views.get("txttargettemp").setLeft((int)((40d / 100 * width)));
//BA.debugLineNum = 37;BA.debugLine="txtTargetTemp.Width = 60%x"[editstage/General script]
views.get("txttargettemp").setWidth((int)((60d / 100 * width)));
//BA.debugLineNum = 38;BA.debugLine="txtTargetTemp.Top = 57%y"[editstage/General script]
views.get("txttargettemp").setTop((int)((57d / 100 * height)));
//BA.debugLineNum = 39;BA.debugLine="txtTargetTemp.Height = 50dip"[editstage/General script]
views.get("txttargettemp").setHeight((int)((50d * scale)));
//BA.debugLineNum = 41;BA.debugLine="txtRate.Left = 40%x"[editstage/General script]
views.get("txtrate").setLeft((int)((40d / 100 * width)));
//BA.debugLineNum = 42;BA.debugLine="txtRate.Width = 60%x"[editstage/General script]
views.get("txtrate").setWidth((int)((60d / 100 * width)));
//BA.debugLineNum = 43;BA.debugLine="txtRate.Top = 69%y"[editstage/General script]
views.get("txtrate").setTop((int)((69d / 100 * height)));
//BA.debugLineNum = 44;BA.debugLine="txtRate.Height = 50dip"[editstage/General script]
views.get("txtrate").setHeight((int)((50d * scale)));
//BA.debugLineNum = 47;BA.debugLine="btnEditStageOK.Left = 5%y"[editstage/General script]
views.get("btneditstageok").setLeft((int)((5d / 100 * height)));
//BA.debugLineNum = 48;BA.debugLine="btnEditStageOK.Width = 40%x"[editstage/General script]
views.get("btneditstageok").setWidth((int)((40d / 100 * width)));
//BA.debugLineNum = 49;BA.debugLine="btnEditStageOK.Bottom = 100%y"[editstage/General script]
views.get("btneditstageok").setTop((int)((100d / 100 * height) - (views.get("btneditstageok").getHeight())));
//BA.debugLineNum = 50;BA.debugLine="btnEditStageCancel.Left = 55%x"[editstage/General script]
views.get("btneditstagecancel").setLeft((int)((55d / 100 * width)));
//BA.debugLineNum = 51;BA.debugLine="btnEditStageCancel.Width = 40%x"[editstage/General script]
views.get("btneditstagecancel").setWidth((int)((40d / 100 * width)));
//BA.debugLineNum = 52;BA.debugLine="btnEditStageCancel.Bottom = 100%y"[editstage/General script]
views.get("btneditstagecancel").setTop((int)((100d / 100 * height) - (views.get("btneditstagecancel").getHeight())));

}
}