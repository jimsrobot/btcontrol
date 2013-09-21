package jimsrobot.arduino.attiny85ad8495bt.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_start{

public static void LS_general(java.util.HashMap<String, anywheresoftware.b4a.objects.ViewWrapper<?>> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
views.get("btnconnectsame").setTop((int)((1d / 100 * height)));
views.get("btnconnectsame").setLeft((int)((1d / 100 * width)));
views.get("btnconnectsame").setHeight((int)((30d / 100 * height)));
views.get("btnconnectsame").setWidth((int)((98d / 100 * width)));
views.get("btnsearchfordevices").setTop((int)((32d / 100 * height)));
views.get("btnsearchfordevices").setHeight((int)((30d / 100 * height)));
views.get("btnsearchfordevices").setLeft((int)((1d / 100 * width)));
views.get("btnsearchfordevices").setWidth((int)((98d / 100 * width)));
views.get("btnskip").setTop((int)((64d / 100 * height)));
views.get("btnskip").setHeight((int)((30d / 100 * height)));
views.get("btnskip").setLeft((int)((1d / 100 * width)));
views.get("btnskip").setWidth((int)((98d / 100 * width)));

}
}