package jimsrobot.arduino.attiny85ad8495bt.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_profilelist{

public static void LS_general(java.util.HashMap<String, anywheresoftware.b4a.objects.ViewWrapper<?>> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
views.get("btnnewprofile").setLeft((int)((0d / 100 * width)));
views.get("btnnewprofile").setWidth((int)((20d / 100 * width)));
views.get("btnnewprofile").setHeight((int)((15d / 100 * height)));
views.get("btnnewprofile").setTop((int)((100d / 100 * height) - (views.get("btnnewprofile").getHeight())));
views.get("btndeleteprofile").setLeft((int)((20d / 100 * width)));
views.get("btndeleteprofile").setWidth((int)((20d / 100 * width)));
views.get("btndeleteprofile").setHeight((int)((15d / 100 * height)));
views.get("btndeleteprofile").setTop((int)((100d / 100 * height) - (views.get("btndeleteprofile").getHeight())));
views.get("btnrename").setLeft((int)((40d / 100 * width)));
views.get("btnrename").setWidth((int)((20d / 100 * width)));
views.get("btnrename").setHeight((int)((15d / 100 * height)));
views.get("btnrename").setTop((int)((100d / 100 * height) - (views.get("btnrename").getHeight())));
views.get("btneditstages").setLeft((int)((60d / 100 * width)));
views.get("btneditstages").setWidth((int)((20d / 100 * width)));
views.get("btneditstages").setHeight((int)((15d / 100 * height)));
views.get("btneditstages").setTop((int)((100d / 100 * height) - (views.get("btneditstages").getHeight())));
views.get("btnupload").setLeft((int)((80d / 100 * width)));
views.get("btnupload").setWidth((int)((20d / 100 * width)));
views.get("btnupload").setHeight((int)((15d / 100 * height)));
views.get("btnupload").setTop((int)((100d / 100 * height) - (views.get("btnupload").getHeight())));
views.get("scvprofiles").setTop((int)((0d / 100 * height)));
views.get("scvprofiles").setHeight((int)((40d / 100 * height)));
views.get("scvprofiles").setLeft((int)(0d));
views.get("scvprofiles").setWidth((int)((100d / 100 * width)));
views.get("txtlog").setLeft((int)(0d));
views.get("txtlog").setWidth((int)((50d / 100 * width)));
views.get("txtlog").setTop((int)((40d / 100 * height)));
views.get("txtlog").setHeight((int)((45d / 100 * height)));
views.get("lbltemperature").setLeft((int)((50d / 100 * width)));
views.get("lbltemperature").setWidth((int)((50d / 100 * width)));
views.get("lbltemperature").setTop((int)((40d / 100 * height)));
views.get("lbltemperature").setHeight((int)((10d / 100 * height)));
views.get("btngo").setLeft((int)((50d / 100 * width)));
views.get("btngo").setWidth((int)((50d / 100 * width)));
views.get("btngo").setHeight((int)((12d / 100 * height)));
views.get("btngo").setTop((int)((views.get("btnrename").getTop()) - (views.get("btngo").getHeight())));

}
}